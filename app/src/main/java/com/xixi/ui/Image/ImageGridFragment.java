package com.xixi.ui.image;

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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;

public class ImageGridFragment extends Fragment {

    public HashMap<Long, String> checkedMap;

    private List<ImageItem> imageList;
    private long bucketId;
    private int maxImageCount;

    private Context context;
    private LayoutInflater inflater;
    private ContentResolver cr;
    private ImageGridAdapter imageGridAdapter;
    private GridView gridView;

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
    public  void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        cr = context.getContentResolver();
        inflater = LayoutInflater.from(context);

        imageGridAdapter = new ImageGridAdapter();
        gridView.setAdapter(imageGridAdapter);
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

    private class ImageGridAdapter extends BaseAdapter {

        private final int CAPACITY = 48;
        Set<Long> taskSet = new TreeSet<>();
        ArrayBlockingQueue<Long> queue = new ArrayBlockingQueue<>(CAPACITY);
        HashMap<Long, Bitmap> thumbMap = new HashMap<>();

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
                holder.imImage.setImageBitmap(null);
            }

            // if the number of images exceeds CAPACITY, remove the earliest one
            if (queue.size() >= CAPACITY - 1) {
                Long earliestId = queue.poll();
                thumbMap.remove(earliestId);
            }

            long imageId = imageItem.imageId;
            holder.imImage.setTag(imageId);

            // get bitmap
            Bitmap bitmap;
            if (thumbMap.containsKey(imageId)) {
                bitmap = thumbMap.get(imageId);
                holder.imImage.setImageBitmap(bitmap);
            } else if (!taskSet.contains(imageId)) {
                taskSet.add(imageId);
                new ThumbnailTask().execute(imageId);
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

        /**
         * AsyncTask: Retrieve thumbnail
         */
        private class ThumbnailTask extends AsyncTask<Long, Void, Bitmap> {

            Long imageId;

            @Override
            protected Bitmap doInBackground(Long... params) {
                imageId = params[0];
                return Thumbnails.getThumbnail(cr, params[0], bucketId, Thumbnails.MINI_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                queue.offer(imageId);
                thumbMap.put(imageId, result);
                taskSet.remove(imageId);
                ImageView imageView = (ImageView) gridView.findViewWithTag(imageId);
                if (imageView != null) {
                    imageView.setImageBitmap(result);
                }
            }
        }
    }

}
