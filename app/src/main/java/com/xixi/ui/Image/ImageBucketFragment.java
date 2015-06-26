package com.xixi.ui.image;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.util.Image.BucketManager;
import com.xixi.util.Image.ImageBucket;

import java.util.HashMap;
import java.util.List;

public class ImageBucketFragment extends Fragment {

    Context context;
    ContentResolver cr;
    LayoutInflater inflater;
    public ListView listView;

    BucketManager bucketManager;
    public List<ImageBucket> bucketList;

    OnBucketClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_image_bucket, container, false);
        listView = (ListView) rootView.findViewById(R.id.image_bucket_listview);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        cr = context.getContentResolver();
        inflater = LayoutInflater.from(getActivity());

        bucketManager = new BucketManager(getActivity().getApplicationContext());
        bucketList = bucketManager.buildBucket();

        ImageBucketAdapter imageBucketAdapter = new ImageBucketAdapter();
        listView.setAdapter(imageBucketAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onBucketClick(position);
                }
            }
        });
    }


    public interface OnBucketClickListener {
        public void onBucketClick(int position);
    }

    public void setOnBucketClickedListener(OnBucketClickListener listener) {
        this.listener = listener;
    }


    private class ImageBucketAdapter extends BaseAdapter {

        HashMap<Long, Bitmap> thumbMap = new HashMap<>();

        @Override
        public int getCount() {
            return bucketList.size();
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
            ImageBucket imageBucket = bucketList.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lv_image_bucket_item, null);
                holder = new ViewHolder();
                holder.imThumbnail = (ImageView) convertView.findViewById(R.id.im_thumbnail);
                holder.tvBucketName = (TextView) convertView.findViewById(R.id.tv_bucket_name);
                convertView.setTag(holder);
            } else {
                holder =  (ViewHolder) convertView.getTag();
            }

            holder.tvBucketName.setText(imageBucket.bucketName);

            long imageId = imageBucket.imageList.get(0).imageId;
            long bucketId = imageBucket.bucketId;
            Bitmap bitmap;
            if (thumbMap.containsKey(imageId)) {
                bitmap = thumbMap.get(imageId);
            } else {
                bitmap = Thumbnails.getThumbnail(cr, imageId, bucketId, Thumbnails.MINI_KIND , null);
                thumbMap.put(imageId, bitmap);
            }
            holder.imThumbnail.setImageBitmap(bitmap);

            return convertView;
        }

        public class ViewHolder {
            ImageView imThumbnail;
            TextView tvBucketName;
        }
    }

}
