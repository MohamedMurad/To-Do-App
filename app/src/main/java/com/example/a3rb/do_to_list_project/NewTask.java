package com.example.a3rb.do_to_list_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewTask extends AppCompatActivity {

    EditText name , description ;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        name = (EditText) findViewById(R.id.name_id) ;
        description = (EditText) findViewById(R.id.description_id) ;
        date = (TextView) findViewById(R.id.date_id) ;

        if (MainActivity.edit_status_one_time){
            name.setText(MainActivity.edit_data[0]);
            description.setText(MainActivity.edit_data[1]);
            date.setText(MainActivity.edit_data[2]);
            //we use to different boolean so that we can save changes out in resume.
            MainActivity.edit_status_one_time = false ;
        }else {
            MainActivity.edit_status = false ;
        }

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("new task");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));
    }

    public void getDate (View view){
        DateDialoge dateDialoge=new DateDialoge();
        dateDialoge.show(getFragmentManager(),"date_picker");
    }

    public void setDate(String date1){
        date.setText(date1);
    }

    public void makeTask(View view){
        String s_name = new String(name.getText().toString());
        String s_description = new String(description.getText().toString());
        String s_date = new String(date.getText().toString());
        String task;
        if (s_name.equals("") || s_date.equals("") || s_description.equals("")){
            Toast.makeText(NewTask.this,"YOU SHOULD FILL ALL FIELDS",Toast.LENGTH_SHORT).show();
        }else {
            task = new String(" name :                      "+s_name+
                    "\n Description :           "+s_description+"\n Date :                        "+s_date);
            MainActivity.task = task;
            MainActivity.back_status = true ;
            hideKeyborad();
            finish();
        }
    }

    //immediately hide the keyboard.
    public void hideKeyborad(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
