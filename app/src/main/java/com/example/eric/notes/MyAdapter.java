package com.example.eric.notes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by eric on 16-8-4.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout linearLayout;
    public MyAdapter(Context context,Cursor cursor){
        this.context=context;
        this.cursor=cursor;
    }
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from(context);
        linearLayout= (LinearLayout) inflater.inflate(R.layout.cell,null);
        TextView contenttv= (TextView) linearLayout.findViewById(R.id.list_content);
        TextView timetv= (TextView) linearLayout.findViewById(R.id.list_time);
        ImageView imgiv= (ImageView) linearLayout.findViewById(R.id.list_img);
        ImageView videoiv= (ImageView) linearLayout.findViewById(R.id.list_video);
        cursor.moveToPosition(i);
        String content=cursor.getString(cursor.getColumnIndex("content"));
        String time=cursor.getString(cursor.getColumnIndex("time"));
        String url=cursor.getString(cursor.getColumnIndex("path"));
        String videourl=cursor.getString(cursor.getColumnIndex("video"));
        contenttv.setText(content);
        timetv.setText(time);
        videoiv.setImageBitmap(getVideoThumbnail(videourl,200,200, MediaStore.Images.Thumbnails.MICRO_KIND));
        imgiv.setImageBitmap(getImageThumbnail(url,200,200));
        return linearLayout;
    }

    private Bitmap getVideoThumbnail(String videourl, int width, int height,int kind) {
        Bitmap bitmap=null;
        bitmap=ThumbnailUtils.createVideoThumbnail(videourl,kind);
        bitmap=ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;

    }

    public Bitmap getImageThumbnail(String uri,int width,int height){
        Bitmap bitmap=null;
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        bitmap=BitmapFactory.decodeFile(uri,options);
        options.inJustDecodeBounds=false;
        int bewidth=options.outWidth/width;
        int beheight=options.outHeight/height;
        int be=1;
        if (bewidth<beheight){
            be=bewidth;
        }else {
            be=beheight;
        }
        if (be<=0){
            be=1;
        }
        options.inSampleSize=be;
        bitmap=BitmapFactory.decodeFile(uri,options);
        bitmap= ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;

    }
}
