package com.example.residehere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoomScreen extends AppCompatActivity implements RoomActivityRecyclerCallback, PaymentResultListener {

    private RecyclerView recyclerViewRoom;
    private String id;
    private ArrayList<RoomDescriptionObject> roomList;
    private ArrayList<String> roomIdList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private PgObject currentPG;
    private String valueAc;
    private String valueAb;
    private String valueSeater;
    private String oneTimeDateText;
    private long valueTimestampSecond;
    private int valueTimestampNanoSecond;
    private Retrofit retrofit;
    private RazorPayMethods razorPayMethodsInterface;
    private String residerName;
    private String residerContact;
    private static final String[] dialogDurationOptions = new String[]{"No fixed duration","30 days","60 days","90 days","120 days", "150 days", "180 days"};

    private void startPayment(int amount, int pos, Timestamp endingTimestamp){
        Log.d("testing", "before response");

        Log.d("testing", "1");

        Calendar calendarAMonth = Calendar.getInstance();
        SimpleDateFormat dfAMonth = new SimpleDateFormat("dd-MM-yyyy");
        try {
            calendarAMonth.setTime(dfAMonth.parse(oneTimeDateText));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        calendarAMonth.add(Calendar.DAY_OF_YEAR, 30);
        SimpleDateFormat sdf1AMonth = new SimpleDateFormat("dd-MM-yyyy");
        String outputAMonth = sdf1AMonth.format(calendarAMonth.getTime());
        Log.d("testing", "getTime:  "+outputAMonth);
        String[] dateFragmentsAMonth = outputAMonth.split("-");
        Date date = new Date(Integer.parseInt(dateFragmentsAMonth[2])-1900, Integer.parseInt(dateFragmentsAMonth[1])-1, Integer.parseInt(dateFragmentsAMonth[0]));
        final Timestamp timestampAMonth = new Timestamp(date);

        razorPayMethodsInterface.generateOrderId(amount).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject notes = new JSONObject();
                    notes.put("firstPayment", "true");
                    notes.put("roomName", roomList.get(pos).getName());
                    notes.put("roomId", roomIdList.get(pos));
                    notes.put("pgId", id);
                    notes.put("customerNo",mAuth.getCurrentUser().getPhoneNumber());
                    notes.put("OwnerNo", currentPG.getOwnerContact());
                    notes.put("pgName", currentPG.getPgName());
                    notes.put("residerName", residerName);
                    notes.put("residerContact", residerContact);
                    notes.put("bookingTimestampSecond", valueTimestampSecond);
                    notes.put("bookingTimestampNanoSecond", valueTimestampNanoSecond);
                    Log.d("testing", "nanoseconds"+timestampAMonth.getNanoseconds()+"");
                    Log.d("testing", "nanoseconds"+timestampAMonth.getSeconds()+"");
                    notes.put("aMonthTimestampSecond", timestampAMonth.getSeconds());
                    notes.put("aMonthTimestampNanoSecond", timestampAMonth.getNanoseconds());

                    if(endingTimestamp == null) {
                        Log.d("testing", "it is infinity");
                        notes.put("endingTimestampSecond", "infinity");
                    }
                    else {
                        Log.d("testing", "it is some timestamp");
                        notes.put("endingTimestampSecond", endingTimestamp.getSeconds());
                        notes.put("endingTimestampNanoSecond", endingTimestamp.getNanoseconds());
                    }

                    Log.d("testing", new Timestamp(valueTimestampSecond, valueTimestampNanoSecond)+"");
                    Log.d("testing", new Timestamp(valueTimestampSecond, valueTimestampNanoSecond).toDate()+"");

                    Log.d("testing", "2");
                    Log.d("testing", "orderId:  "+response.body());
                    Checkout co = new Checkout();
                    co.setKeyID("rzp_test_Gsl9afKiPfznt7");

                    JSONObject options = new JSONObject();
                    options.put("name", "resideHere");
                    options.put("description", "residence booking");
                    options.put("order_id", response.body());
                    options.put("currency", "INR");
                    options.put("amount", amount+"");
                    options.put("notes", notes);

                    co.open(RoomScreen.this, options);
                }catch (Exception e){
                    Log.d("testing", "catch block run");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("testing", call.toString()+ "        "+ t.toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_screen);

        Checkout.preload(getApplicationContext());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        id = getIntent().getStringExtra("id");
        valueAc = getIntent().getStringExtra("valueAc");
        valueAb = getIntent().getStringExtra("valueAb");
        valueSeater = getIntent().getStringExtra("valueSeater");
        currentPG = getIntent().getParcelableExtra("currentPG");
        oneTimeDateText = getIntent().getStringExtra("oneTimeDateText");
        valueTimestampSecond = getIntent().getLongExtra("valueTimestampSecond",0);
        valueTimestampNanoSecond = getIntent().getIntExtra("valueTimestampNanoSecond",0);
        roomList = new ArrayList<>();
        roomIdList = new ArrayList<>();

        recyclerViewRoom = findViewById(R.id.recyclerView_rooms);

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://q8zkr177sj.execute-api.us-east-1.amazonaws.com/")
                .build();
        razorPayMethodsInterface = retrofit.create(RazorPayMethods.class);

        Log.d("testing", id);
        Log.d("testing", "length"+id.length());
        db.collection("Rooms")
                .whereEqualTo("pgId", id)
                .whereEqualTo("ac",valueAc)
                .whereEqualTo("ab", valueAb)
                .whereEqualTo("seater", valueSeater)
                .whereEqualTo("status", "true")
                .whereLessThan("availableTimestamp", new Timestamp(valueTimestampSecond, valueTimestampNanoSecond)).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("testing", "complete");
                        if (task.isSuccessful()){
                            for (DocumentSnapshot ds: task.getResult().getDocuments()) {
                                Log.d("testing", "entered");
                                HashMap<String, Object> roomFetchMap = new HashMap<>();
                                roomFetchMap = (HashMap<String, Object>) ds.getData();
                                roomIdList.add(ds.getId());
                                roomList.add(new RoomDescriptionObject(roomFetchMap.get("name").toString(), roomFetchMap.get("seater").toString(), roomFetchMap.get("ac").toString(), roomFetchMap.get("ab").toString(), roomFetchMap.get("price").toString()));
                            }

                            if(roomList.size() > 0){
                                Log.d("testing", "room fetched");
                                LinearLayoutManager linearLayoutManagerRoom = new LinearLayoutManager(RoomScreen.this);
                                RecyclerViewAdapterRoom recyclerViewAdapterRoom = new RecyclerViewAdapterRoom(RoomScreen.this, RoomScreen.this, roomList, roomIdList);
                                recyclerViewRoom.setLayoutManager(linearLayoutManagerRoom);
                                recyclerViewRoom.setAdapter(recyclerViewAdapterRoom);
                            }
                            else {
                                Log.d("testing", "room not fetched");
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("testing", e.toString());
            }
        });

    }

    @Override
    public void goToBookRoom(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose duration of stay");
        builder.setSingleChoiceItems(dialogDurationOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int period = which;
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            calendar.setTime(df.parse(oneTimeDateText));
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Timestamp endingTimestamp = null;
                        if(which != 0){
                            calendar.add(Calendar.DAY_OF_YEAR, 30*which);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                            String output = sdf1.format(calendar.getTime());
                            Log.d("testing", "getTime:  "+output);
                            String[] dateFragments = output.split("-");
                            Date date = new Date(Integer.parseInt(dateFragments[2])-1900, Integer.parseInt(dateFragments[1])-1, Integer.parseInt(dateFragments[0]));
                            endingTimestamp = new Timestamp(date);
                        }
                        dialog.dismiss();
                        startPayment(Integer.parseInt(roomList.get(pos).getPrice())*100, pos, endingTimestamp);
                    }
                });


        AlertDialog.Builder builderInfo = new AlertDialog.Builder(this);
        builderInfo.setTitle("Enter these details");
        View view = this.getLayoutInflater().inflate(R.layout.basic_info, null);
        builderInfo.setView(view);
        CountryCodePicker ccp = view.findViewById(R.id.basic_info_ccp);
        EditText editTextName = view.findViewById(R.id.basic_info_name);
        EditText editTextContact = view.findViewById(R.id.basic_info_contact);


        builderInfo.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String contact = editTextContact.getText().toString();
                residerName = name;
                residerContact = ccp.getSelectedCountryCode()+contact;
                if(name.equals("") || contact.equals("") || contact.length() != 10){
                    Toast.makeText(RoomScreen.this, "Enter details correctly", Toast.LENGTH_SHORT).show();
                }
                else{
                    builder.show();
                }
            }
        });
        builderInfo.show();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d("testing", "payment success");
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d("testing", "payment failed");
        Log.d("testing", s);
    }
}