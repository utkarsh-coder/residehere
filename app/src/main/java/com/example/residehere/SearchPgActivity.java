package com.example.residehere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;

public class SearchPgActivity extends AppCompatActivity {

    private String[] itemsAc = {"AC Room", "Non AC Room"};
    private String[] itemsAb = {"Attached Bathroom", "Separate Bathroom"};
    private String[] itemsSeater = {"1", "2", "3", "4", "5"};

    private String[] acSearchValue = {"true", "false"};
    private String[] abSearchValue = {"true", "false"};
    private String[] seaterSearchValue = {"1", "2", "3", "4", "5"};

    private AutoCompleteTextView autoCompleteTextViewRoomType;
    private AutoCompleteTextView autoCompleteTextViewBathroomType;
    private AutoCompleteTextView autoCompleteTextViewSeaterType;
    private TextView timestampTextView;
    private Button nextBtn;

    private ArrayAdapter<String> adapterItemsAc;
    private ArrayAdapter<String> adapterItemsAb;
    private ArrayAdapter<String> adapterItemsSeater;

    private String valueAc;
    private String valueAb;
    private String valueSeater;
    private Timestamp valueTimestamp;
    private String oneTimeDate;

    private DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pg);

        valueAc = null;
        valueAb = null;
        valueSeater = null;
        valueTimestamp = null;

        autoCompleteTextViewRoomType = findViewById(R.id.auto_complete_text_room_type);
        autoCompleteTextViewBathroomType = findViewById(R.id.auto_complete_text_bathroom_type);
        autoCompleteTextViewSeaterType = findViewById(R.id.auto_complete_text_seater_type);
        timestampTextView = findViewById(R.id.timestamp_click_text);
        nextBtn = findViewById(R.id.nextBtn);

        adapterItemsAc = new ArrayAdapter<String>(this, R.layout.drop_down_single_item_layout, itemsAc);
        adapterItemsAb = new ArrayAdapter<String>(this, R.layout.drop_down_single_item_layout, itemsAb);
        adapterItemsSeater = new ArrayAdapter<String>(this, R.layout.drop_down_single_item_layout, itemsSeater);

        autoCompleteTextViewRoomType.setAdapter(adapterItemsAc);
        autoCompleteTextViewBathroomType.setAdapter(adapterItemsAb);
        autoCompleteTextViewSeaterType.setAdapter(adapterItemsSeater);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, day + 30);
        maxDate.set(Calendar.MONTH, month);
        maxDate.set(Calendar.YEAR, year);

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d("testing", "working for now");
                Date date = new Date(year-1900, month-1, dayOfMonth);
                valueTimestamp = new Timestamp(date);
                if((dayOfMonth>=1 && dayOfMonth <=9) && (month>=1 && month<=9))
                    oneTimeDate  = "0"+dayOfMonth+"-"+"0"+month+"-"+year;
                else if(dayOfMonth>=1 && dayOfMonth <=9)
                    oneTimeDate = "0"+dayOfMonth+"-"+month+"-"+year;
                else if(month>=1 && month <=9)
                    oneTimeDate = dayOfMonth+"-"+"0"+month+"-"+year;
                else
                    oneTimeDate = dayOfMonth+"-"+month+"-"+year;
                timestampTextView.setText("Date -> "+oneTimeDate);
                Log.d("testing", "timestamp for future:  "+valueTimestamp);
                Log.d("testing", "timestamp for future:  "+valueTimestamp.toDate());
            }
        };

        timestampTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SearchPgActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener, year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });



        autoCompleteTextViewRoomType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                valueAc = acSearchValue[position];
            }
        });

        autoCompleteTextViewBathroomType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                valueAb = abSearchValue[position];
            }
        });

        autoCompleteTextViewSeaterType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                valueSeater = seaterSearchValue[position];
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valueAc != null && valueAb != null && valueSeater != null && valueTimestamp != null){
                    Intent intent = new Intent(SearchPgActivity.this, HomeScreen.class);
                    intent.putExtra("valueAc", valueAc);
                    intent.putExtra("valueAb", valueAb);
                    intent.putExtra("valueSeater", valueSeater);
                    intent.putExtra("oneTimeDateText", oneTimeDate);
                    intent.putExtra("valueTimestampSecond", valueTimestamp.getSeconds());
                    intent.putExtra("valueTimestampNanoSecond", valueTimestamp.getNanoseconds());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SearchPgActivity.this, "Select all options", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}