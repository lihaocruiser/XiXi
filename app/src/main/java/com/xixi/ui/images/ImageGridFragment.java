package com.xixi.ui.images;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.xixi.R;
import com.xixi.util.Image.ImageBucket;
import com.xixi.util.Image.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class ImageGridFragment extends Fragment {

    public HashMap<Long, String> checkedMap;

    private List<ImageItem> imageList;
    private long bucketId;
    private int maxImageCount;

    private LayoutInflater inflater;
    private ContentResolver cr;
    private ImageGridAdapter imageGridAdapter;
    private GridView gridView;

    public boolean isFirstEnter = true;

    public ImageGridFragment() {
        imageList = new ArrayList<>();
        checkedMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_image_grid, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getActivity();
        cr = context.getContentResolver();
        inflater = LayoutInflater.from(context);

        imageGridAdapter = new ImageGridAdapter();
        gridView.setAdapter(imageGridAdapter);
        gridView.setOnScrollListener(imageGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem imageItem = imageList.get(position);
                long imageId = imageItem.imageId;
                String imagePath = imageItem.imagePath;
                if (checkedMap.containsKey(imageId)) {
                    checkedMap.remove(imageId);
                } else if (checkedMap.size() >= maxImageCount) {
                    Toast.makeText(getActivity(), R.string.error_image_exceed, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    checkedMap.put(imageId, imagePath);
                }
                imageGridAdapter.notifyDataSetChanged();
            }
        });

    }

    public void setImageBucket(ImageBucket imageBucket) {
        imageList = imageBucket.imageList;
        bucketId = imageBucket.bucketId;
        imageGridAdapter.notifyDataSetChanged();
    }


    public void setMaxImageCount(int maxImageCount) {
        this.maxImageCount = maxImageCount;
    }

    private class ImageGridAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

        private final int CAPACITY = 24;
        private int firstVisibleItem;
        private int visibleItemCount;

        Set<ThumbnailTask> taskSet;
        HashMap<Long, Bitmap> thumbMap;
        ArrayBlockingQueue<Long> queue;

        public ImageGridAdapter() {
            taskSet = new HashSet<>();
            queue = new ArrayBlockingQueue<>(CAPACITY);
            thumbMap = new HashMap<>();
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ImageItem imageItem = imageList.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gv_image_grid_item, null);
                holder = new ViewHolder();
                holder.imImage = (ImageView) convertView.findViewById(R.id.im_image);
                holder.imCheck = (ImageView) convertView.findViewById(R.id.im_check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // if the number of images exceeds CAPACITY, remove the earliest one
//            Long earliestId = queue.peek();
//            if (queue.size() >= CAPACITY - 1 && inScreen(earliestId)) {
//                queue.poll();
//                thumbMap.remove(earliestId);
//            }

            long imageId = imageItem.imageId;
            holder.imImage.setTag(imageId);

            if (thumbMap.containsKey(imageId)) {    // in cache: fetch it
                Bitmap bitmap = thumbMap.get(imageId);
                holder.imImage.setImageBitmap(bitmap);
            } else {
                holder.imImage.setImageBitmap(null);
            }

                // deal with imCheck
            if (checkedMap.containsKey(imageId)) {
                holder.imCheck.setVisibility(View.VISIBLE);
            } else {
                holder.imCheck.setVisibility(View.GONE);
            }

            return convertView;
        }

        private class ViewHolder {
            ImageView imImage;
            ImageView imCheck;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                Long earliestId = queue.peek();
                if (queue.size() >= CAPACITY - 1 && inScreen(earliestId)) {
                    queue.poll();
                    thumbMap.remove(earliestId);
                }
                loadBitmaps(firstVisibleItem, visibleItemCount);
            } else {
                cancelAllTasks();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;

            if (isFirstEnter && visibleItemCount > 0) {
                isFirstEnter = false;
                taskSet.clear();
                loadBitmaps(firstVisibleItem, visibleItemCount);
            }
        }

        private void loadBitmaps(int firstItem, int itemCount) {
            ImageItem imageItem;
            Long imageId;
            for (int i = firstItem; i < firstItem + itemCount; i++) {
                imageItem = imageList.get(i);
                imageId = imageItem.imageId;
                if (thumbMap.containsKey(imageId)) {
                    ImageView imageView = (ImageView) gridView.findViewWithTag(imageId);
                    if (imageView != null) {
                        imageView.setImageBitmap(thumbMap.get(imageId));
                    }
                } else if (getTaskById(imageId) == null) {   // neither in cache nor in taskSet: start task
                    ThumbnailTask task = new ThumbnailTask();
                    task.execute(imageId);
                    taskSet.add(task);
                }
            }
        }

        /**
         * AsyncTask: Retrieve thumbnail
         */
        private class ThumbnailTask extends AsyncTask<Long, Void, Bitmap> {

            Long imageId;

            @Override
            protected Bitmap doInBackground(Long... id) {
                if (isCancelled()) {
                    return null;
                }
                imageId = id[0];
                return Thumbnails.getThumbnail(cr, id[0], bucketId, Thumbnails.MINI_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                taskSet.remove(getTaskById(imageId));
                if (result == null) {
                    return;
                }
                queue.offer(imageId);
                thumbMap.put(imageId, result);
                ImageView imageView = (ImageView) gridView.findViewWithTag(imageId);
                if (imageView != null) {
                    imageView.setImageBitmap(result);
                }
            }
        }

        private ThumbnailTask getTaskById(Long imageId) {
            for (ThumbnailTask task : taskSet) {
                if (imageId.equals(task.imageId)) {
                    return task;
                }
            }
            return null;
        }

        private void cancelAllTasks() {
            for (ThumbnailTask task : taskSet) {
                task.cancel(false);
            }
            taskSet.clear();
        }

        private boolean inScreen(Long imageId) {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                if (imageId.equals(imageList.get(i).imageId)) {
                    return true;
                }
            }
            return false;
        }

    }

}
