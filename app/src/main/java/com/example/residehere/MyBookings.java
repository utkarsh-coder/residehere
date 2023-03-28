package com.example.residehere;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyBookings extends Fragment implements RoomActivityRecyclerCallback {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerViewBooking;
    private ArrayList<Booking> list;
    private String bookingType;
    private Retrofit retrofit;
    private RazorPayMethods razorPayMethodsInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_list, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

//        Checkout.preload(getContext());

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://q8zkr177sj.execute-api.us-east-1.amazonaws.com/")
                .build();
        razorPayMethodsInterface = retrofit.create(RazorPayMethods.class);

        bookingType = this.getArguments().getString("bookingType");
        recyclerViewBooking = view.findViewById(R.id.recyclerview_booking);
        list = new ArrayList<>();

        if (bookingType.equals("allBookings")) {
            Query query = db.collection("Booking")
                    .whereEqualTo("customerNo", mAuth.getCurrentUser().getPhoneNumber());

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                        HashMap<String, Object> tempBooking = new HashMap<>();
                        tempBooking = (HashMap<String, Object>) ds.getData();
                        list.add(new Booking(ds.getId(), tempBooking.get("bookingStatus").toString(), tempBooking.get("roomName").toString(), tempBooking.get("roomId").toString(), tempBooking.get("pgId").toString(), tempBooking.get("customerNo").toString(), tempBooking.get("OwnerNo").toString(), tempBooking.get("pgName").toString(), ds.getTimestamp("bookingTimestamp"), tempBooking.get("endingTimestamp").toString(), ds.getTimestamp("createdAt"), tempBooking.get("currentStatus").toString(), ds.getTimestamp("paidTill"), tempBooking.get("residerName").toString(), tempBooking.get("residerContact").toString()));
                    }
                    LinearLayoutManager linearLayoutManagerBooking = new LinearLayoutManager(getContext());
                    RecyclerViewAdapterBookingList recyclerViewAdapterBooking = new RecyclerViewAdapterBookingList(MyBookings.this, getContext(), list, false);
                    recyclerViewBooking.setLayoutManager(linearLayoutManagerBooking);
                    recyclerViewBooking.setAdapter(recyclerViewAdapterBooking);
                }
            });
        } else {
            Query query = db.collection("Booking")
                    .whereEqualTo("customerNo", mAuth.getCurrentUser().getPhoneNumber())
                    .whereEqualTo("currentStatus", "true");

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                        HashMap<String, Object> tempBooking = new HashMap<>();
                        tempBooking = (HashMap<String, Object>) ds.getData();
                        list.add(new Booking(ds.getId(), tempBooking.get("bookingStatus").toString(), tempBooking.get("roomName").toString(), tempBooking.get("roomId").toString(), tempBooking.get("pgId").toString(), tempBooking.get("customerNo").toString(), tempBooking.get("OwnerNo").toString(), tempBooking.get("pgName").toString(), ds.getTimestamp("bookingTimestamp"), tempBooking.get("endingTimestamp").toString(), ds.getTimestamp("createdAt"), tempBooking.get("currentStatus").toString(), ds.getTimestamp("paidTill"), tempBooking.get("residerName").toString(), tempBooking.get("residerContact").toString()));
                    }
                    LinearLayoutManager linearLayoutManagerBooking = new LinearLayoutManager(getContext());
                    RecyclerViewAdapterBookingList recyclerViewAdapterRoom = new RecyclerViewAdapterBookingList(MyBookings.this, getContext(), list, true);
                    recyclerViewBooking.setLayoutManager(linearLayoutManagerBooking);
                    recyclerViewBooking.setAdapter(recyclerViewAdapterRoom);
                }
            });
        }
        return view;
    }

    @Override
    public void goToBookRoom(int pos) {

        db.collection("Booking").document(list.get(pos).getBookingId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        HashMap<String, Object> temp = new HashMap<>();
                        temp = (HashMap<String, Object>) documentSnapshot.getData();
                        if (temp.get("endingTimestamp").getClass().getSimpleName().equals("Timestamp")) {
                            if (((Timestamp) temp.get("endingTimestamp")).compareTo((Timestamp) temp.get("paidTill")) > 0) {
                                db.collection("Rooms").document(list.get(pos).getRoomId()).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    Log.d("testing", "document exists1");
                                                    int amount = Integer.parseInt(documentSnapshot.getString("price")) * 100;
                                                    razorPayMethodsInterface.generateOrderId(amount).enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(Call<String> call, Response<String> response) {
                                                            Log.d("testing", response.body());
                                                            try {
                                                                Log.d("testing", "printing orderid: " + response.body());
                                                                JSONObject notes = new JSONObject();
                                                                notes.put("firstPayment", "false");
                                                                notes.put("bookingId", list.get(pos).getBookingId());

                                                                Checkout checkout = new Checkout();
                                                                checkout.setKeyID("rzp_test_Gsl9afKiPfznt7");

                                                                JSONObject options = new JSONObject();
                                                                options.put("name", "resideHere");
                                                                options.put("description", "residence booking");
                                                                options.put("currency", "INR");
                                                                options.put("order_id", response.body());
                                                                String price = documentSnapshot.getString("price");
                                                                options.put("amount", +Integer.parseInt(price) * 100);
                                                                options.put("notes", notes);

                                                                checkout.open(getActivity(), options);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<String> call, Throwable t) {
                                                            Log.d("testing", call.toString() + "        " + t.toString());
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        }
                        else {
                            db.collection("Rooms").document(list.get(pos).getRoomId()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                Log.d("testing", "document exists2");
                                                int amount = Integer.parseInt(documentSnapshot.getString("price")) * 100;
                                                razorPayMethodsInterface.generateOrderId(amount).enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        Log.d("testing", response.body());
                                                        try {
                                                            Log.d("testing", "printing orderid: " + response.body());
                                                            JSONObject notes = new JSONObject();
                                                            notes.put("firstPayment", "false");
                                                            notes.put("bookingId", list.get(pos).getBookingId());

                                                            Checkout checkout = new Checkout();
                                                            checkout.setKeyID("rzp_test_Gsl9afKiPfznt7");

                                                            JSONObject options = new JSONObject();
                                                            options.put("name", "resideHere");
                                                            options.put("description", "residence booking");
                                                            options.put("currency", "INR");
                                                            options.put("order_id", response.body());
                                                            String price = documentSnapshot.getString("price");
                                                            options.put("amount", +Integer.parseInt(price) * 100);
                                                            options.put("notes", notes);

                                                            checkout.open(getActivity(), options);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        Log.d("testing", call.toString() + "        " + t.toString());
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}