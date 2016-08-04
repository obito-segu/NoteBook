package com.example.eric.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContentActivity extends Activity implements View.OnClickListener{

    private String val;
    private Button saveBtn,cancelBtn;
    private EditText et;
    private ImageView c_img;
    private VideoView c_video;

    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private File phoneFile,videoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        val=getIntent().getStringExtra("flag");
        saveBtn= (Button) findViewById(R.id.save);
        cancelBtn= (Button) findViewById(R.id.cancel);
        et= (EditText) findViewById(R.id.et);
        c_img= (ImageView) findViewById(R.id.c_img);
        c_video= (VideoView) findViewById(R.id.c_video);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        notesDB=new NotesDB(this);
        dbWriter=notesDB.getWritableDatabase();
        initView();

    }

    private void addDB() {
        ContentValues cv=new ContentValues();
        cv.put(NotesDB.CONTENT,et.getText().toString());
        cv.put(NotesDB.CONTENT,getTime());
        cv.put(NotesDB.PATH,phoneFile+"");
        cv.put(NotesDB.VIDEO,videoFile+"");
        dbWriter.insert(NotesDB.TABLE_NAME,null,cv);
    }
    private String getTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date=new Date();
        String str=sdf.format(date);
        return str;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                addDB();
                finish();
                break;

            case R.id.cancel:
                finish();
                break;
        }

    }

    public void initView(){
        if (val!=null&&val.equals("1")){
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.GONE);
        }
        if (val!=null&&val.equals("2")){
            c_img.setVisibility(View.VISIBLE);
            c_video.setVisibility(View.GONE);
            Intent iimg=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+getTime()+".jpg");
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
            startActivityForResult(iimg,1);
        }
        if (val!=null&&val.equals("3")){
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.VISIBLE);
            Intent ivideo=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+getTime()+".mp4");
            ivideo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(ivideo,2);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            Bitmap bitmap= BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
            c_img.setImageBitmap(bitmap);
        }
        if (requestCode==2){
            c_video.setVideoURI(Uri.fromFile(videoFile));
            c_video.start();
        }
    }
}
