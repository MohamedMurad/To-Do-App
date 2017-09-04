package com.example.a3rb.do_to_list_project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static boolean back_status = false;
    static String task = new String("");

    static ArrayList<String> listItems ;
    ArrayAdapter<String> adapter;
    ImageView imageView;
    TextView textView;
    ActionBar actionBar;
    ListView listView;

    public static boolean edit_status = false;
    public static boolean edit_status_one_time = false;
    static int edit_position;
    public static String[] edit_data = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.back_image_id);
        textView = (TextView) findViewById(R.id.back_text_id);
        listView = (ListView) findViewById(R.id.list_view_id);
        registerForContextMenu(listView);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));

        //loading the data.
        String data = loadInternal();
        listItems = new ArrayList<String>();
        String[] data_split = new String[]{};
        //to check if there is saved data or not.
        if (data.contains("-----")) {
            data_split = data.split("-----");
            //we rewrite "task" so that the inner resume method codes can work.
            task="there is data";
            back_status=true;
        }
        // -1 as we store " " after the last "-----" in the last of data file so that we can split.
        for (int i = 0; i < data_split.length - 1 ; i++) {
            listItems.add(data_split[i]);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!task.trim().equals("") && back_status) {
            back_status = false ;
            if (task != "there is data" && !edit_status) {
                listItems.add(task.toString());
            }else if (edit_status){
                listItems.set(edit_position,task.toString());
                edit_status = false ;
            }
            showTasksNumber();
            adapter = new ArrayAdapter<String>(this,
                    R.layout.them_layout, R.id.them_id, listItems);
            listView.setAdapter(adapter);
            setBackgroundVis();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listItems.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "task completed", Toast.LENGTH_SHORT).show();
                    setBackgroundVis();
                    showTasksNumber();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StringBuffer buffe = new StringBuffer();
        for (int i=0 ; i<listItems.size() ; i++){
            buffe.append(listItems.get(i)+"-----");
        }
        buffe.append(" ");
        saveInternal(buffe.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
    =================
    DATABASE METHODS
    =================
     */

    /*
    ======
    SAVING
    ======
     */

    private void dataStore(String data, File file1) {
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(file1);
            fileOutputStream.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveInternal(String data){
        File file=getCacheDir();
        File file1=new File(file,"To-Do Data.txt");
        dataStore(data, file1);
    }

  /*
    =======
    LOADING
    =======
     */

    private String getData(File file1) {
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=new FileInputStream(file1);
        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this,"No Tasks",Toast.LENGTH_SHORT).show();
        }
        int read=-1;
        StringBuffer buffer=new StringBuffer();
        try {
            while ((read=fileInputStream.read())!=-1){
                buffer.append((char) read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fileInputStream!=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    public String loadInternal(){
        String data;
        try {
            File file=getCacheDir();
            File file1=new File(file,"To-Do Data.txt");
            data= getData(file1);
        }catch (RuntimeException e){
            data = "";
        }
        return data;
    }


    /*
    =============
    OTHER METHODS
    =============
     */

    //this method working on click the + button to add new task.
    public void newTask(View view){
        Intent intent=new Intent(MainActivity.this,NewTask.class);
        startActivity(intent);
    }

    //to show number of tasks as a sub title.
    public void showTasksNumber(){
        if (listItems.size() > 1){
            actionBar.setSubtitle("You have "+listItems.size()+" tasks");
        }else if (listItems.size()==1){
            actionBar.setSubtitle("You have "+listItems.size()+" task");
        }else {
            actionBar.setSubtitle("You have No tasks");
        }
    }

    //to set the visibility of the background of no tasks.
    protected void setBackgroundVis(){
        if (listItems.size() == 0){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
    }


    /*
    ====================
    CONTEXT MENU METHODS
    ====================
     */

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.completed_id:
                listItems.remove(info.position);
                adapter.notifyDataSetChanged();
                setBackgroundVis();
                showTasksNumber();
                Toast.makeText(MainActivity.this, "task completed", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edit_id:
                edit_status = edit_status_one_time = true ;
                edit_position = info.position;
                String edit_task = new String(listItems.get(edit_position));
                edit_data[0] = edit_task.substring(edit_task.lastIndexOf(" name :                      ")+29,edit_task.indexOf("\n Description :           "));
                edit_data[1] = edit_task.substring(edit_task.lastIndexOf("\n Description :           ")+26,edit_task.indexOf("\n Date :                        "));
                edit_data[2] = edit_task.substring(edit_task.lastIndexOf("\n Date :                        ")+32);
                Intent intent=new Intent(MainActivity.this,NewTask.class);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}