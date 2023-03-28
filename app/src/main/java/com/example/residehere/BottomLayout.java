package com.example.residehere;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;

public class BottomLayout extends BottomSheetDialogFragment {

    private FilterValuesUpdate updateContext;
    private String[] itemsAc = {"AC Room", "Non AC Room"};
    private String[] itemsAb = {"Attached Bathroom", "Separate Bathroom"};
    private String[] itemsFood = {"Food Facility available", "Food facility not available", "ignore Food facility"};
    private String[] itemsLaundry = {"Laundry Facility available", "Laundry facility not available", "ignore Laundry facility"};
    private String[] itemsSeater = {"1", "2", "3", "4", "5"};

    private String[] acSearchValue = {"true", "false"};
    private String[] abSearchValue = {"true", "false"};
    private String[] foodSearchValue = {"true", "false", null};
    private String[] laundrySearchValue = {"true", "false", null};
    private String[] seaterSearchValue = {"1", "2", "3", "4", "5"};

    private AutoCompleteTextView autoCompleteTextViewRoomType;
    private AutoCompleteTextView autoCompleteTextViewBathroomType;
    private AutoCompleteTextView autoCompleteTextViewSeaterType;
    private AutoCompleteTextView autoCompleteTextViewFoodType;
    private AutoCompleteTextView autoCompleteTextViewLaundryType;
    private TextView timestampTextView;
    private Button applyBtn;

    private ArrayAdapter<String> adapterItemsAc;
    private ArrayAdapter<String> adapterItemsAb;
    private ArrayAdapter<String> adapterItemsFood;
    private ArrayAdapter<String> adapterItemsLaundry;
    private ArrayAdapter<String> adapterItemsSeater;

    private DatePickerDialog.OnDateSetListener setListener;

    private String valueAc;
    private String valueAb;
    private String valueSeater;
    private String valueFood;
    private String valueLaundry;
    private Timestamp valueTimestamp;
    private long valueTimestampSecond;
    private int valueTimestampNanoSecond;
    private String oneTimeDateText;

    public BottomLayout(FilterValuesUpdate updateContext, String valueAc, String valueAb, String valueSeater, String valueFood, String valueLaundry, long valueTimestampSecond, int valueTimestampNanoSecond, String oneTimeDateText) {
        this.updateContext = updateContext;
        this.valueAc = valueAc;
        this.valueAb = valueAb;
        this.valueSeater = valueSeater;
        this.valueFood = valueFood;
        this.valueLaundry = valueLaundry;
        this.valueTimestampSecond = valueTimestampSecond;
        this.valueTimestampNanoSecond = valueTimestampNanoSecond;
        this.oneTimeDateText = oneTimeDateText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_bottom_layout, container, false);

        autoCompleteTextViewRoomType = view.findViewById(R.id.auto_complete_text_room_type);
        autoCompleteTextViewBathroomType = view.findViewById(R.id.auto_complete_text_bathroom_type);
        autoCompleteTextViewSeaterType = view.findViewById(R.id.auto_complete_text_seater_type);
        autoCompleteTextViewFoodType = view.findViewById(R.id.auto_complete_text_food_type);
        autoCompleteTextViewLaundryType = view.findViewById(R.id.auto_complete_text_laundry_type);
        timestampTextView = view.findViewById(R.id.timestamp_click_text);
        applyBtn = view.findViewById(R.id.apply_btn);

        autoCompleteTextViewRoomType.setText(itemsAc[Boolean.parseBoolean(valueAc)?0:1]);
        autoCompleteTextViewBathroomType.setText(itemsAb[Boolean.parseBoolean(valueAb)?0:1]);
        autoCompleteTextViewSeaterType.setText(valueSeater);
        timestampTextView.setText("Date -> "+oneTimeDateText);

        String[] dateFragmentArr = oneTimeDateText.split("-");
        Calendar calendar = Calendar.getInstance();
        final int year = Integer.parseInt(dateFragmentArr[2]);
        final int month = Integer.parseInt(dateFragmentArr[1])-1;
        final int day = Integer.parseInt(dateFragmentArr[0]);

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 30);
        maxDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        maxDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

        if (valueFood == null)
            autoCompleteTextViewFoodType.setText(itemsFood[2]);
        else
            autoCompleteTextViewFoodType.setText(itemsFood[Boolean.parseBoolean(valueFood) ? 0 : 1]);

        if (valueLaundry == null)
            autoCompleteTextViewLaundryType.setText(itemsLaundry[2]);
        else
            autoCompleteTextViewLaundryType.setText(itemsLaundry[Boolean.parseBoolean(valueLaundry)?0:1]);

        adapterItemsAc = new ArrayAdapter<String>(getContext(), R.layout.drop_down_single_item_layout, itemsAc);
        adapterItemsAb = new ArrayAdapter<String>(getContext(), R.layout.drop_down_single_item_layout, itemsAb);
        adapterItemsSeater = new ArrayAdapter<String>(getContext(), R.layout.drop_down_single_item_layout, itemsSeater);
        adapterItemsFood = new ArrayAdapter<String>(getContext(), R.layout.drop_down_single_item_layout, itemsFood);
        adapterItemsLaundry = new ArrayAdapter<String>(getContext(), R.layout.drop_down_single_item_layout, itemsLaundry);

        autoCompleteTextViewRoomType.setAdapter(adapterItemsAc);
        autoCompleteTextViewBathroomType.setAdapter(adapterItemsAb);
        autoCompleteTextViewSeaterType.setAdapter(adapterItemsSeater);
        autoCompleteTextViewFoodType.setAdapter(adapterItemsFood);
        autoCompleteTextViewLaundryType.setAdapter(adapterItemsLaundry);

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d("testing", "working for now");
                Date date = new Date(year, month, dayOfMonth);
                valueTimestamp = new Timestamp(date);
                valueTimestampSecond = valueTimestamp.getSeconds();
                valueTimestampNanoSecond = valueTimestamp.getNanoseconds();
                if((dayOfMonth>=1 && dayOfMonth <=9) && (month>=1 && month<=9))
                    oneTimeDateText = "0"+dayOfMonth+"-"+"0"+month+"-"+year;
                else if(dayOfMonth>=1 && dayOfMonth <=9)
                    oneTimeDateText = "0"+dayOfMonth+"-"+month+"-"+year;
                else if(month>=1 && month <=9)
                    oneTimeDateText = dayOfMonth+"-"+"0"+month+"-"+year;
                else
                    oneTimeDateText = dayOfMonth+"-"+month+"-"+year;
                timestampTextView.setText("Date -> "+oneTimeDateText);
                Log.d("testing", "timestamp for future:  "+valueTimestamp);
            }
        };

        timestampTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener, year, month, day
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

        autoCompleteTextViewFoodType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                valueFood = foodSearchValue[position];
            }
        });

        autoCompleteTextViewLaundryType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                valueLaundry = laundrySearchValue[position];
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContext.updateFilterValues(valueAc, valueAb, valueSeater, valueFood, valueLaundry, valueTimestampSecond, valueTimestampNanoSecond, oneTimeDateText);
            }
        });
        return view;

    }
}
