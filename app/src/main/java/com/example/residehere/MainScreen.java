package com.example.residehere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainScreen extends AppCompatActivity implements PaymentResultListener{

    private BottomNavigationView bottomNavigationView;
    static Map<String, Object> data;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Retrofit retrofit;
    private RazorPayMethods razorPayMethodsInterface;

    private void startPayment(int amount){
        Log.d("testing", "before response");
        Log.d("testing", "1");
        razorPayMethodsInterface.generateOrderId(amount).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    Log.d("testing", "2");
                    Log.d("testing", "orderId:  "+response.body());
                    Checkout co = new Checkout();
                    co.setKeyID("rzp_test_Gsl9afKiPfznt7");

                    JSONObject notes = new JSONObject();
                    notes.put("roomName", "optional");

                    JSONObject options = new JSONObject();
                    options.put("name", "resideHere");
                    options.put("description", "residence booking");
                    options.put("order_id", response.body());
                    options.put("currency", "INR");
                    options.put("amount", amount+"");
                    options.put("notes", notes);
                    co.open(MainScreen.this, options);
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
        setContentView(R.layout.activity_main_screen);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.navigation);

        data = null;

        Fragment selectedFragment = null;
        selectedFragment = new ResideSearch();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_navigation, selectedFragment).commit();

        db.collection("CustomerList").document(mAuth.getCurrentUser().getPhoneNumber())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    data = (HashMap<String, Object>)task.getResult().getData();
                }
            }
        });

        final GeoLocation center = new GeoLocation(Double.parseDouble("26"), Double.parseDouble("80"));
        final double radiusInM = 500 * 1000;
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        for(GeoQueryBounds b: bounds){
            db.collection("PG")
                    .whereArrayContains("category", "ai2")
                    .whereEqualTo("roomStatus."+"ai2", "true")
                    .orderBy("minPrice")
                    .orderBy(FieldPath.of("availableTimestamp", "ai2"))
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Log.d("testing", queryDocumentSnapshots.getDocuments().toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("testing", e.toString());
                }
            });
        }

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://q8zkr177sj.execute-api.us-east-1.amazonaws.com/")
                .build();
        razorPayMethodsInterface = retrofit.create(RazorPayMethods.class);

//        startPayment(100);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId())
                {
                    case R.id.logo:
                        selectedFragment = new ResideSearch();
                        Toast.makeText(MainScreen.this, "logo", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.my_account:
                        selectedFragment = new MyAccount();
                        Toast.makeText(MainScreen.this, "my account", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.payment_history:
                        Bundle bundle =new Bundle();
                        bundle.putString("bookingType", "allBookings");
                        selectedFragment = new MyBookings();
                        selectedFragment.setArguments(bundle);
                        Toast.makeText(MainScreen.this, "payment history", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.living_history:
                        bundle =new Bundle();
                        bundle.putString("bookingType", "enrolledBookings");
                        selectedFragment = new MyBookings();
                        selectedFragment.setArguments(bundle);
                        Toast.makeText(MainScreen.this, "living history", Toast.LENGTH_SHORT).show();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_navigation, selectedFragment).commit();
                return true;
            }
        });
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