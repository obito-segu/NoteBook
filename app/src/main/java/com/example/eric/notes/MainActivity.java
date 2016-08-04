package com.example.eric.notes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private Button textBtn,imgBtn,videoBtn;
    private ListView lv;
    private Intent intent;
    private MyAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        lv= (ListView) findViewById(R.id.lv);
        textBtn= (Button) findViewById(R.id.text);
        imgBtn= (Button) findViewById(R.id.pic);
        videoBtn= (Button) findViewById(R.id.video);
        textBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        notesDB=new NotesDB(this);
        dbReader=notesDB.getReadableDatabase();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, SelectActivity.class);
                i.putExtra(NotesDB.ID,
                        cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor
                        .getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME,
                        cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH,
                        cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                i.putExtra(NotesDB.VIDEO,
                        cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(i);
            }
        });
    }


    public void selectDB(){
        cursor=dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        adapter=new MyAdapter(this,cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }

    @Override
    public void onClick(View view) {
        intent=new Intent(this,AddContentActivity.class);
        switch (view.getId()){
            case R.id.text:
                intent.putExtra("flag",1);
                startActivity(intent);
                break;
            case R.id.pic:
                intent.putExtra("flag",2);
                startActivity(intent);
                break;
            case R.id.video:
                intent.putExtra("flag",3);
                startActivity(intent);
                break;
        }

    }
}
