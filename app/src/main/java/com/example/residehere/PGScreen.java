package com.example.residehere;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PGScreen extends AppCompatActivity  {

    private TextView pgNameTextView;
    private TextView addressTextView;
    private TextView managerNameTextView;
    private TextView stateTextView;
    private ImageButton locateImageButton;
    private int total=0;
    private int size;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutFeatures;
    private TextView featureTextView;
    private ArrayList<RoomDescriptionObject> arrayList;
    private ArrayList<View> viewArrayList;
    private TextView priceTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentRoom;
    private PgObject currentPG;
    private ArrayList<String> categList = null;
    private double amount;
    private String id;
    private String valueAc;
    private String valueAb;
    private String valueSeater;
    private String oneTimeDateText;
    private long valueTimestampSecond;
    private int valueTimestampNanoSecond;
    private ExtendedFloatingActionButton extendedFloatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgscreen);

        pgNameTextView = findViewById(R.id.pg_name);
        addressTextView = findViewById(R.id.address);
        managerNameTextView = findViewById(R.id.manager_name);
        stateTextView = findViewById(R.id.state);
        locateImageButton = findViewById(R.id.locate_image_button);
        linearLayout = findViewById(R.id.rooms_linear_layout);
        extendedFloatingActionButton = findViewById(R.id.select_room_floating_extended_button);

        arrayList = new ArrayList<>();
        viewArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        id = getIntent().getStringExtra("id");
        currentPG = getIntent().getParcelableExtra("currentPG");
        valueAc = getIntent().getStringExtra("valueAc");
        valueAb = getIntent().getStringExtra("valueAb");
        valueSeater = getIntent().getStringExtra("valueSeater");
        oneTimeDateText = getIntent().getStringExtra("oneTimeDateText");
        valueTimestampSecond = getIntent().getLongExtra("valueTimestampSecond",0);
        valueTimestampNanoSecond = getIntent().getIntExtra("valueTimestampNanoSecond",0);

        pgNameTextView.setText(currentPG.getPgName());
        addressTextView.setText(currentPG.getAddress());
        managerNameTextView.setText(currentPG.getManagerName());
        stateTextView.setText(currentPG.getState());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Log.d("testing", "1001");


        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PGScreen.this, RoomScreen.class);
                intent.putExtra("id", id);
                intent.putExtra("valueAc", valueAc);
                intent.putExtra("valueAb", valueAb);
                intent.putExtra("valueSeater", valueSeater);
                intent.putExtra("currentPG", currentPG);
                intent.putExtra("oneTimeDateText", oneTimeDateText);
                intent.putExtra("valueTimestampSecond", valueTimestampSecond);
                intent.putExtra("valueTimestampNanoSecond", valueTimestampNanoSecond);
                startActivity(intent);
            }
        });

        locateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q="+currentPG.getLat()+","+currentPG.getLng()));
                startActivity(intent);
            }
        });
//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 26.761435, 80.885022);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        startActivity(intent);
    }
}