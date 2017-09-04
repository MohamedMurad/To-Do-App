package com.example.a3rb.do_to_list_project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Created by 3rb on 24/08/2016.
 */
public class DateDialoge extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),this,year,month,day);
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date=""+year+" / "+(++monthOfYear)+" / "+dayOfMonth ;
        Toast.makeText(getActivity(),"date selected is : "+date,Toast.LENGTH_SHORT).show();
        ((NewTask)(getActivity())).setDate(date);
    }
}