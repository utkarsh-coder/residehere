package com.example.residehere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.residehere.ModelGeocoding.Results;
import com.example.residehere.ModelQueryAutocomplete.MainList;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeScreen extends AppCompatActivity implements PgActivityRecyclerCallback, FilterValuesUpdate {

    private EditText search;

    private FirebaseFirestore db;
    private List<GeoQueryBounds> bounds;
    private String lat;
    private String lng;
    private ArrayList<PgObject> matchingDocsObject;
    private ArrayList<String> idList;
    private RelativeLayout relativeLayout;
    private RelativeLayout progressBarRelativeLayout;
    private RelativeLayout displayResultMessageRelativeLayout;
    private RecyclerView recyclerViewReside;
    private RecyclerView recyclerViewLocations;
    private ApiInterface apiInterface;
    private Button sort;
    private Button filter;
    private AlertDialog.Builder filterBuilder;
    private AlertDialog.Builder sortBuilder;
    private AlertDialog.Builder filterSeaterBuilder;
    private int boundCount = 0;
    private ImageButton addResultImageButton;
    private LinearLayout linearLayout;
    private DocumentSnapshot lastQueriedDocument;
    private List<Task<QuerySnapshot>> tasks;
    private Boolean ac;
    private Boolean nonAc;
    private Boolean ab;
    private Boolean food;
    private Boolean laundry;
    private String seater;
    private Query.Direction sortDirection;
    private List<String> categoryList;
    private String valueAc;
    private String valueAb;
    private String valueSeater;
    private long valueTimestampSecond;
    private int valueTimestampNanoSecond;
    private String oneTimeDateText;
    private String valueFood = null;
    private String valueLaundry = null;
    private String nomenclature = "";
    private BottomLayout bottomLayout;

    private final String[] filterDialogOptions = new String[]{"AC Room", "Non-AC Room", "Attached Bathroom", "Food Facility", "Laundry"};
    private final String[] sortDialogOptions = new String[]{"Price low to high", "Price high to low"};
    private final String[] filterSeaterDialogOptions = new String[]{"1", "2", "3", "4", "5"};
    private int sortCheckedItem;

    private final boolean[] booleanOptions = new boolean[]{false, false, false, false, false};

    //    private static double deg2radian(double lat) {
//        return (lat * Math.PI / 180.0);
//    }
//
//    private static double rad2degree(double distance) {
//        return (distance * 180.0 / Math.PI);
//    }
//
//    private static double getDistance(Double lat1, Double long1, Double lat2, Double long2) {
//
//        double longDiff = long1 - long2;
//        double distance = Math.sin(deg2radian(lat1)) * Math.sin(deg2radian(lat2))
//                + Math.cos(deg2radian(lat1)) * Math.cos(deg2radian(lat2)) * Math.cos(deg2radian(longDiff));
//        distance = Math.acos(distance);
//        distance = rad2degree(distance);
//        distance = distance * 60 * 1.1515;
//        distance = distance * 1.609344;
//        Log.d("testing", distance + "km");
//        return distance;
//    }

//    public static String getState(String latitude, String longitude) {
//        String state = "";
//        try {
//            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
//            state = addresses.get(0).getAdminArea();
//            Log.d("testing", state);
//        } catch (Exception e) {
//            Log.d("testing", e.toString());
//        }
//        return state;
//    }


//    private void runTasksComplete(){
//        for (Task<QuerySnapshot> task : tasks) {
//            QuerySnapshot snap = task.getResult();
//            for (DocumentSnapshot doc : snap.getDocuments()) {
////                                double lat = Double.parseDouble(doc.getString("lat"));
////                                double lng = Double.parseDouble(doc.getString("lng"));
//
//                // We have to filter out a few false positives due to GeoHash
//                // accuracy, but most will match
////                                GeoLocation docLocation = new GeoLocation(lat, lng);
////                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
////                                if (distanceInM <= radiusInM) {}
//
//                HashMap<String, Object> pgData = new HashMap<>();
//                HashMap<String, Object> pgDetails = new HashMap<>();
//                HashMap<String, Object> timestampDetails = new HashMap<>();
//                pgData = (HashMap<String, Object>) doc.getData();
//                pgDetails  = (HashMap<String, Object>)pgData.get("pgDetails");
//                timestampDetails  = (HashMap<String, Object>)pgData.get("availableTimestamp");
//                Timestamp temporary = null;
//                Log.d("testing", "1");
//                if(timestampDetails.get(nomenclature).getClass().getSimpleName().equals("Timestamp")) {
//                    Log.d("testing", "2");
//                    temporary = (Timestamp) timestampDetails.get(nomenclature);
//                    Log.d("testing", "3");
//                }
//                Log.d("testing", "4");
//                Log.d("testing", "timestamp from firebase:  "+temporary);
//                Log.d("testing", "5");
//                if(temporary != null){
//                    Log.d("testing", "6");
//                    if (temporary.compareTo(new Timestamp(valueTimestampSecond, valueTimestampNanoSecond)) < 0){
//                        matchingDocsObject.add(new PgObject(pgDetails.get("accountVerify").toString(),pgDetails.get("address").toString(),pgDetails.get("city").toString(),pgDetails.get("food").toString(),pgDetails.get("laundry").toString(),pgDetails.get("geohash").toString(),pgDetails.get("lat").toString(),pgDetails.get("lng").toString(),pgDetails.get("managerContact").toString(),pgDetails.get("managerEmail").toString(),pgDetails.get("managerName").toString(),pgDetails.get("ownerContact").toString(),pgDetails.get("paymentStatus").toString(),pgDetails.get("pgName").toString(),pgDetails.get("pgStatus").toString(),pgDetails.get("state").toString()));
//                        idList.add(doc.getId());
//                    }
//                    Log.d("testing", "7");
//                }
//            }
//            Log.d("testing", "8");
//        }
//        Log.d("testing", "9");
//        linearLayout.setVisibility(View.VISIBLE);
//        progressBarRelativeLayout.setVisibility(View.GONE);
//        Log.d("testing", "matching size "+matchingDocsObject.size());
//        Log.d("testing", "10");
//
//        if (matchingDocsObject.size() > 0) {
//            recyclerViewReside.setVisibility(View.VISIBLE);
//            relativeLayout.setVisibility(View.GONE);
//            LinearLayoutManager linearLayoutManagerResidingList = new LinearLayoutManager(HomeScreen.this);
//            RecyclerViewAdapterResidingList recyclerViewAdapterResidingList = new RecyclerViewAdapterResidingList(HomeScreen.this, HomeScreen.this, matchingDocsObject);
//            recyclerViewReside.setLayoutManager(linearLayoutManagerResidingList);
//            recyclerViewReside.setAdapter(recyclerViewAdapterResidingList);
//            addResultImageButton.setVisibility(View.VISIBLE);
//            boundCount = 0;
//
//        } else {
//            Log.d("testing", "entered else");
//            relativeLayout.setVisibility(View.VISIBLE);
//            Log.d("testing", "exit else");
//        }
//
//        // matchingDocs contains the results
//        // ...
//        Log.d("testing", "task clear");
//    }

//    Query query = db.collection("PG")
//            .whereEqualTo("roomStatus."+nomenclature, "true")
//            .orderBy("geohash")
//            .startAt(b.startHash)
//            .endAt(b.endHash)
//            .limit(2);
//    Query query = db.collection("PG")
//            .whereEqualTo("globalAvailable", "true")
//            .orderBy("geohash")
//            .startAt(b.startHash)
//            .endAt(b.endHash)
//            .startAfter(lastQueriedDocument)
//            .limit(2);
//    .whereArrayContainsAny("categ", categoryList)
//                            .orderBy("avgPrice",sortDirection)
//                            .orderBy("geohash")
//                            .startAt(0, b.startHash)
//                            .endAt(10000.0, b.endHash)
//                            .startAfter(lastQueriedDocument)
//                            .limit(2);

    public void printResideDetails(Query query) {
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("testing", "size is: " + queryDocumentSnapshots.getDocuments().size());
                List<DocumentSnapshot> listDocumentSnapshot = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot doc : listDocumentSnapshot) {
                    lastQueriedDocument = listDocumentSnapshot.get(listDocumentSnapshot.size() - 1);
                    HashMap<String, Object> pgData = new HashMap<>();
                    HashMap<String, Object> pgDetails = new HashMap<>();
                    pgData = (HashMap<String, Object>) doc.getData();
                    pgDetails = (HashMap<String, Object>) pgData.get("pgDetails");
                    matchingDocsObject.add(new PgObject(pgDetails.get("accountVerify").toString(), pgDetails.get("address").toString(), pgDetails.get("city").toString(), pgDetails.get("food").toString(), pgDetails.get("laundry").toString(), pgDetails.get("geohash").toString(), pgDetails.get("lat").toString(), pgDetails.get("lng").toString(), pgDetails.get("managerContact").toString(), pgDetails.get("managerEmail").toString(), pgDetails.get("managerName").toString(), pgDetails.get("ownerContact").toString(), pgDetails.get("paymentStatus").toString(), pgDetails.get("pgName").toString(), pgDetails.get("pgStatus").toString(), pgDetails.get("state").toString()));
                    idList.add(doc.getId());
                }

                linearLayout.setVisibility(View.VISIBLE);
                progressBarRelativeLayout.setVisibility(View.GONE);
                recyclerViewLocations.setVisibility(View.GONE);
                if (matchingDocsObject.size() > 0) {
                    recyclerViewReside.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManagerResidingList = new LinearLayoutManager(HomeScreen.this);
                    RecyclerViewAdapterResidingList recyclerViewAdapterResidingList = new RecyclerViewAdapterResidingList(HomeScreen.this, HomeScreen.this, matchingDocsObject);
                    recyclerViewReside.setLayoutManager(linearLayoutManagerResidingList);
                    recyclerViewReside.setAdapter(recyclerViewAdapterResidingList);
                    addResultImageButton.setVisibility(View.VISIBLE);
                    boundCount = 0;

                } else {
                    Log.d("testing", "entered else");
                    relativeLayout.setVisibility(View.VISIBLE);
                    Log.d("testing", "exit else");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("testing", e.toString());
            }
        });
    }

    public void printResideDetailsSorted(Query query) {
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("testing", "size is: " + queryDocumentSnapshots.getDocuments().size());
                List<DocumentSnapshot> listDocumentSnapshot = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot doc : listDocumentSnapshot) {
                    lastQueriedDocument = listDocumentSnapshot.get(listDocumentSnapshot.size() - 1);
                    HashMap<String, Object> pgData = new HashMap<>();
                    HashMap<String, Object> pgDetails = new HashMap<>();
                    pgData = (HashMap<String, Object>) doc.getData();
                    pgDetails = (HashMap<String, Object>) pgData.get("pgDetails");
                    HashMap<String, Object> timestampDetails = new HashMap<>();

                    timestampDetails = (HashMap<String, Object>) pgData.get("availableTimestamp");
                    Timestamp temporary = null;
                    Log.d("testing", "1");
                    if (timestampDetails.get(nomenclature).getClass().getSimpleName().equals("Timestamp")) {
                        Log.d("testing", "2");
                        temporary = (Timestamp) timestampDetails.get(nomenclature);
                        Log.d("testing", "3");
                    }
                    Log.d("testing", "4");
                    Log.d("testing", "timestamp from firebase:  " + temporary);
                    Log.d("testing", "5");
                    if (temporary != null) {
                        Log.d("testing", "6");
                        if (temporary.compareTo(new Timestamp(valueTimestampSecond, valueTimestampNanoSecond)) < 0) {
                            matchingDocsObject.add(new PgObject(pgDetails.get("accountVerify").toString(), pgDetails.get("address").toString(), pgDetails.get("city").toString(), pgDetails.get("food").toString(), pgDetails.get("laundry").toString(), pgDetails.get("geohash").toString(), pgDetails.get("lat").toString(), pgDetails.get("lng").toString(), pgDetails.get("managerContact").toString(), pgDetails.get("managerEmail").toString(), pgDetails.get("managerName").toString(), pgDetails.get("ownerContact").toString(), pgDetails.get("paymentStatus").toString(), pgDetails.get("pgName").toString(), pgDetails.get("pgStatus").toString(), pgDetails.get("state").toString()));
                            idList.add(doc.getId());
                        }
                        temporary = null;
                        Log.d("testing", "7");
                    }
                }

                linearLayout.setVisibility(View.VISIBLE);
                progressBarRelativeLayout.setVisibility(View.GONE);
                if (matchingDocsObject.size() > 0) {
                    recyclerViewReside.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManagerResidingList = new LinearLayoutManager(HomeScreen.this);
                    RecyclerViewAdapterResidingList recyclerViewAdapterResidingList = new RecyclerViewAdapterResidingList(HomeScreen.this, HomeScreen.this, matchingDocsObject);
                    recyclerViewReside.setLayoutManager(linearLayoutManagerResidingList);
                    recyclerViewReside.setAdapter(recyclerViewAdapterResidingList);
                    addResultImageButton.setVisibility(View.VISIBLE);
                    boundCount = 0;

                } else {
                    Log.d("testing", "entered else");
                    relativeLayout.setVisibility(View.VISIBLE);
                    Log.d("testing", "exit else");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("testing", e.toString());
            }
        });
    }

    public void runQuery() {
        Log.d("testing", "arrived1");
        // TODO: 05-02-2022 make queries of every possibilities, sorting by price (only check)

        Log.d("testing", "testing2");
        String hash = null, hashSearch = null;
        hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(Double.parseDouble(lat),
                Double.parseDouble(lng)));
        hashSearch = hash.substring(0, 4);
        Log.d("testing", "testing3");
        Timestamp timestampSearch = new Timestamp(valueTimestampSecond, valueTimestampNanoSecond);
        Log.d("testing", "testing4");
        Query query = db.collection("PG")
                .whereArrayContains("category", nomenclature)
                .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
                .whereNotEqualTo(FieldPath.of("availableTimestamp", nomenclature), "infinity")
                .whereLessThan(FieldPath.of("availableTimestamp", nomenclature), timestampSearch)
                .whereEqualTo("roomStatus." + nomenclature, "true");
        Log.d("testing", "testing5");

        if (lastQueriedDocument != null) {
            Log.d("testing", "testing101");
            query = query.startAfter(lastQueriedDocument);
            Log.d("testing", "testing102");
        }
        Log.d("testing", "testing6");
        if (valueFood != null) {
            query = query.whereEqualTo(FieldPath.of("pgDetails", "food"), "true");
        }

        Log.d("testing", "testing7");
        if (valueLaundry != null) {
            query = query.whereEqualTo(FieldPath.of("pgDetails", "laundry"), "true");
        }
        Log.d("testing", "testing8");

        if (sortDirection != null) {
            Log.d("testing", "here it come1");
            Query  q = db.collection("PG")
                    .orderBy("minPrice", sortDirection)
                    .whereArrayContains("category", nomenclature)
                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
                    .whereEqualTo("roomStatus." + nomenclature, "true");
            printResideDetailsSorted(q);
        } else {
            Log.d("testing", "testing9");
            printResideDetails(query);
        }

        Log.d("testing", "here it come");


        Log.d("testing", "arrived2");
//        if (lastQueriedDocument == null && valueFood == null && valueLaundry == null){
//            Log.d("testing", "arrive3");
//            Query q =  query;
//            Log.d("testing", "i am here1");
//            Log.d("testing", hashSearch);
//            printResideDetails(q);
//        }
//        else if (lastQueriedDocument == null && valueFood != null && valueLaundry == null){
//            Log.d("testing", "arrive4");
//            Query q = db.collection("PG")
//                    .whereArrayContains("category", nomenclature)
//                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
//                    .whereNotEqualTo(FieldPath.of("availableTimestamp", nomenclature), "infinity")
//                    .whereLessThan(FieldPath.of("availableTimestamp",nomenclature),timestampSearch)
//                    .whereEqualTo("roomStatus."+nomenclature, "true");
//        }
//        else if (lastQueriedDocument == null && valueFood == null && valueLaundry != null){
//            Log.d("testing", "arrive5");
//            Query q = db.collection("PG")
//                    .whereArrayContains("category", nomenclature)
//                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
//                    .whereNotEqualTo(FieldPath.of("availableTimestamp", "ai2"), "infinity")
//                    .whereLessThan(FieldPath.of("availableTimestamp","ai2"),timestampSearch)
//                    .whereEqualTo("roomStatus."+nomenclature, "true");
//        }
//        else if (lastQueriedDocument == null && valueFood != null && valueLaundry != null){
//            Log.d("testing", "arrive6");
//            Query q = db.collection("PG")
//                    .whereArrayContains("category", nomenclature)
//                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
//                    .whereNotEqualTo(FieldPath.of("availableTimestamp", "ai2"), "infinity")
//                    .whereLessThan(FieldPath.of("availableTimestamp","ai2"),timestampSearch)
//                    .whereEqualTo("roomStatus."+nomenclature, "true");
//        }
//        else if (lastQueriedDocument != null && valueFood == null && valueLaundry == null){
//            Log.d("testing", "arrive7");
//            Query q = db.collection("PG")
//                    .whereArrayContains("category", nomenclature)
//                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
//                    .whereNotEqualTo(FieldPath.of("availableTimestamp", "ai2"), "infinity")
//                    .whereLessThan(FieldPath.of("availableTimestamp","ai2"),timestampSearch)
//                    .whereEqualTo("roomStatus."+nomenclature, "true");
//        }
//        else if (lastQueriedDocument != null && valueFood != null && valueLaundry == null){
//            Log.d("testing", "arrive8");
//            Query q = db.collection("PG")
//                    .whereArrayContains("category", nomenclature)
//                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
//                    .whereNotEqualTo(FieldPath.of("availableTimestamp", "ai2"), "infinity")
//                    .whereLessThan(FieldPath.of("availableTimestamp","ai2"),timestampSearch)
//                    .whereEqualTo("roomStatus."+nomenclature, "true");
//        }
//        else if (lastQueriedDocument != null && valueFood == null && valueLaundry != null){
//            Log.d("testing", "arrive9");
//            Query q = db.collection("PG")
//                    .whereArrayContains("category", nomenclature)
//                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
//                    .whereNotEqualTo(FieldPath.of("availableTimestamp", "ai2"), "infinity")
//                    .whereLessThan(FieldPath.of("availableTimestamp","ai2"),timestampSearch)
//                    .whereEqualTo("roomStatus."+nomenclature, "true");
//        }
//        else if (lastQueriedDocument != null && valueFood != null && valueLaundry != null){
//            Log.d("testing", "arrive10");
//            Query q = db.collection("PG")
//                    .whereArrayContains("category", nomenclature)
//                    .whereEqualTo(FieldPath.of("pgDetails", "geohashSearch"), hashSearch)
//                    .whereNotEqualTo(FieldPath.of("availableTimestamp", "ai2"), "infinity")
//                    .whereLessThan(FieldPath.of("availableTimestamp","ai2"),timestampSearch)
//                    .whereEqualTo("roomStatus."+nomenclature, "true");
//        }
    }

//    public void addQueryToTaskWithoutSorting(Query query){
//        tasks.add(query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if(queryDocumentSnapshots.size() > 0){
//                    DocumentSnapshot lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() -1);
//                    if( lastVisible != null){
//                        if (lastQueriedDocument != null){
//                            String lastVisibleGeohash = (String) lastVisible.get("geohash");
//                            String lastQueriedDocumentGeohash = (String) lastQueriedDocument.get("geohash");
//                            if (lastVisibleGeohash.compareTo(lastQueriedDocumentGeohash) >= 1) {
//                                lastQueriedDocument = lastVisible;
//                            }
//                        }else {
//                            lastQueriedDocument = lastVisible;
//                        }
//                    }
//                }
//
//                boundCount++;
//                if (boundCount < bounds.size()){
//                    runQuery(bounds.get(boundCount));
//                }else {
//                    boundCount = 0;
//                    runTasksComplete();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("testing", e.toString());
//            }
//        }));
//    }
//
//    public void addQueryToTaskWithSorting(Query query){
//        tasks.add(query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if(queryDocumentSnapshots.getDocuments().size() > 0){
//                    DocumentSnapshot lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() -1);
//                    if( lastVisible != null){
//                        if (lastQueriedDocument != null){
//                            Double lastVisibleAvgPrice = lastVisible.getDouble("avgPrice");
//                            Double lastQueriedDocumentAvgPrice = lastQueriedDocument.getDouble("avgPrice");
//                            if (lastVisibleAvgPrice >= lastQueriedDocumentAvgPrice) {
//                                lastQueriedDocument = lastVisible;
//                            }
//                        }else {
//                            lastQueriedDocument = lastVisible;
//                        }
//                    }
//                }
//                boundCount++;
//                if (boundCount < bounds.size()){
//                    runQuery(bounds.get(boundCount));
//                }else {
//                    boundCount = 0;
//                    runTasksComplete();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("testing", e.toString());
//            }
//        }));
//    }

    public void listResidingPlaces() {
        Log.d("testing", "1001");
        runQuery();

//        final GeoLocation center = new GeoLocation(Double.parseDouble(lat), Double.parseDouble(lng));
//        final double radiusInM = 500 * 1000;
//        bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
//
//        Log.d("testing", "1002");
//         if(bounds.size() == 0){
//             progressBarRelativeLayout.setVisibility(View.GONE);
//             relativeLayout.setVisibility(View.VISIBLE);
//             Log.d("testing", "1003");
//         }else {
//             Log.d("testing", "1004");
//             // TODO: 02-03-2022 add this parameter to the runQuery() function
//             runQuery(bounds.get(boundCount));
//             Log.d("testing", "1005");
//         }

//        Double lowerlat = Double.parseDouble(lat)-(Double.parseDouble(lat)*50);
//        Double lowerlng = Double.parseDouble(lng)-(Double.parseDouble(lng)*50);
//        Double higherlat = Double.parseDouble(lat)+(Double.parseDouble(lat)*50);
//        Double higherlng = Double.parseDouble(lng)+(Double.parseDouble(lng)*50);
//
//        GeoPoint lowergeo = new GeoPoint(lowerlat, lowerlng);
//        GeoPoint highergeo = new GeoPoint(higherlat, higherlng);

//        Query q = db.collection("PG").whereGreaterThan("geopoint",lowergeo).whereLessThan("geopoint",highergeo);
//        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                Log.d("testing", queryDocumentSnapshots.toString());
//            }
//        });


//        Tasks.whenAllComplete(tasks)
//                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
//                    @Override
//                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
//
//                    }
//                });

    }

    private void getData(String text) {
        apiInterface.getPlace(text, getString(R.string.api_key)).enqueue(new Callback<MainList>() {
            @Override
            public void onResponse(Call<MainList> call, Response<MainList> response) {
                if (response.isSuccessful()) {

                    Log.d("testing", response.toString());
                    Log.d("testing", response.body().getStatus());

                    progressBarRelativeLayout.setVisibility(View.GONE);
                    recyclerViewLocations.setVisibility(View.VISIBLE);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeScreen.this);

                    RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(HomeScreen.this, HomeScreen.this, response.body().getPredictions());
                    recyclerViewLocations.setLayoutManager(linearLayoutManager);
                    recyclerViewLocations.setAdapter(recyclerviewAdapter);


                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                    recyclerViewReside.setVisibility(View.GONE);
                    recyclerViewLocations.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainList> call, Throwable t) {
                relativeLayout.setVisibility(View.VISIBLE);
                recyclerViewReside.setVisibility(View.GONE);
                Toast.makeText(HomeScreen.this, "error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        valueAc = getIntent().getStringExtra("valueAc");
        valueAb = getIntent().getStringExtra("valueAb");
        valueSeater = getIntent().getStringExtra("valueSeater");
        valueTimestampSecond = getIntent().getLongExtra("valueTimestampSecond", 0);
        valueTimestampNanoSecond = getIntent().getIntExtra("valueTimestampNanoSecond", 0);
        oneTimeDateText = getIntent().getStringExtra("oneTimeDateText");

        addResultImageButton = findViewById(R.id.add_results);
        search = findViewById(R.id.editText_search);
        relativeLayout = findViewById(R.id.nodata_relative_layout);
        recyclerViewReside = findViewById(R.id.recyclerView_reside);
        recyclerViewLocations = findViewById(R.id.recyclerView_locations);
        progressBarRelativeLayout = findViewById(R.id.progress_bar_relative_layout);
        displayResultMessageRelativeLayout = findViewById(R.id.display_result_message);

        displayResultMessageRelativeLayout.setVisibility(View.VISIBLE);

        sort = findViewById(R.id.sort_btn);
        filter = findViewById(R.id.filter_btn);
        linearLayout = findViewById(R.id.linearLayout_sort_filter);
        linearLayout.setVisibility(View.VISIBLE);

        search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        db = FirebaseFirestore.getInstance();
        matchingDocsObject = new ArrayList<>();
        idList = new ArrayList<>();
        categoryList = new ArrayList<>();
        tasks = new ArrayList<>();
        lastQueriedDocument = null;
        lat = null;
        lng = null;

        ac = false;
        nonAc = false;
        ab = false;
        food = false;
        laundry = false;
        seater = null;
        sortDirection = null;

        nomenclature = nomenclature + (Boolean.parseBoolean(valueAc) ? "a" : "n");
        nomenclature = nomenclature + (Boolean.parseBoolean(valueAb) ? "i" : "s");
        nomenclature = nomenclature + valueSeater;


        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .build();

        apiInterface = retrofit.create(ApiInterface.class);

        addResultImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listResidingPlaces();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                linearLayout.setVisibility(View.GONE);
            }


            @Override
            public void afterTextChanged(Editable editable) {
                displayResultMessageRelativeLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                progressBarRelativeLayout.setVisibility(View.VISIBLE);
                lastQueriedDocument = null;
                matchingDocsObject.clear();
                idList.clear();
                tasks.clear();
                boundCount = 0;
                getData(editable.toString());
            }
        });


        sortCheckedItem = -1;

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortDirection == Query.Direction.ASCENDING)
                    sortCheckedItem = 0;
                else if (sortDirection == Query.Direction.DESCENDING)
                    sortCheckedItem = 1;
                sortBuilder = new AlertDialog.Builder(HomeScreen.this);
                sortBuilder.setTitle("");
                sortBuilder.setSingleChoiceItems(sortDialogOptions, sortCheckedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("testing", "check value:" + which);
                        if (which == 0) {
                            sortDirection = Query.Direction.ASCENDING;
                        } else if (which == 1) {
                            sortDirection = Query.Direction.DESCENDING;
                        }
                        dialog.dismiss();
                    }
                });
                sortBuilder.show();
            }
        });


//        filterSeaterBuilder = new AlertDialog.Builder(HomeScreen.this);
//        filterSeaterBuilder.setTitle("");
//        filterSeaterBuilder.setSingleChoiceItems(filterSeaterDialogOptions, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                seater = (i+1)+"";
//            }
//        });
//
//        filterSeaterBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//            }
//        });
//
//        filterSeaterBuilder. setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                seater = null;
//            }
//        });


//        filterBuilder = new AlertDialog.Builder(HomeScreen.this);
//        filterBuilder.setTitle("Filter");
//        filterBuilder.setMultiChoiceItems(filterDialogOptions, booleanOptions, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//                switch (i){
//                    case 0:
//                        ac = b;
//                        break;
//                    case 1:
//                        nonAc = b;
//                        break;
//                    case 2:
//                        ab = b;
//                        break;
//                    case 3:
//                        food = b;
//                        break;
//                    case 4:
//                        laundry = b;
//                        break;
//                }
//            }
//        });

//        filterBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (seater != null){
//                    String roomFilter1;
//                    String roomFilter2;
//                    if ((!ac && !nonAc) || (ac && nonAc)){
//                        roomFilter1 = "{\"ac\":\"" + true + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + seater + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                        roomFilter2 = "{\"ac\":\"" + false + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + seater + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                        categoryList.add(roomFilter1);
//                        categoryList.add(roomFilter2);
//                    }else if (ac){
//                        roomFilter1 = "{\"ac\":\"" + true + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + seater + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                        categoryList.add(roomFilter1);
//                    }else if (nonAc){
//                        roomFilter2 = "{\"ac\":\"" + false + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + seater + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                        categoryList.add(roomFilter2);
//                    }
//                }
//                else {
//                    String roomFilter1;
//                    String roomFilter2;
//                    if ((!ac && !nonAc) || (ac && nonAc)){
//                        for (int k=1; k<=5; k++) {
//                            roomFilter1 = "{\"ac\":\"" + true + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + k + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                            roomFilter2 = "{\"ac\":\"" + false + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + k + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                            categoryList.add(roomFilter1);
//                        }
//                    }else if (ac){
//                        for (int k=1; k<=5; k++) {
//                            roomFilter1 = "{\"ac\":\"" + true + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + k + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                            categoryList.add(roomFilter1);
//                        }
//                    }else if (nonAc){
//                        for (int k=1; k<=5; k++) {
//                            roomFilter2 = "{\"ac\":\"" + false + "\",\"ab\":\"" + ab + "\",\"seater\":\"" + seater + "\",\"food\":\"" + food + "\",\"laundry\":\"" + laundry + "\",\"status\":\"" + true + "\",\"available\":\"" + true + "\"}";
//                            categoryList.add(roomFilter2);
//                        }
//                    }
//                }
//            }
//        });

//        filterBuilder.setNegativeButton("Cancel", null);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomLayout = new BottomLayout(HomeScreen.this, valueAc, valueAb, valueSeater, valueFood, valueLaundry, valueTimestampSecond, valueTimestampNanoSecond, oneTimeDateText);
                bottomLayout.show(getSupportFragmentManager(), "TAG");
            }
        });
    }

    @Override
    public void goToPrintResides(String description) {
        progressBarRelativeLayout.setVisibility(View.VISIBLE);
        recyclerViewLocations.setVisibility(View.GONE);

        apiInterface.getLocation(description, getString(R.string.api_key)).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (response.isSuccessful()) {
                    recyclerViewLocations.setVisibility(View.GONE);
                    Log.d("testing", response.toString());
                    Log.d("testing", response.body().getResults().get(0).getGeometry().getLocation().getLat());
                    lat = response.body().getResults().get(0).getGeometry().getLocation().getLat();
                    lng = response.body().getResults().get(0).getGeometry().getLocation().getLng();
                    search.setText(description);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    listResidingPlaces();
                } else {
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
            }
        });
    }

    @Override
    public void goToSelectCurrentPG(int pos) {

        PgObject currentPG = new PgObject(
                matchingDocsObject.get(pos).getAccountVerify(),
                matchingDocsObject.get(pos).getAddress(),
                matchingDocsObject.get(pos).getCity(),
                matchingDocsObject.get(pos).getFood(),
                matchingDocsObject.get(pos).getLaundry(),
                matchingDocsObject.get(pos).getGeohash(),
                matchingDocsObject.get(pos).getLat(),
                matchingDocsObject.get(pos).getLng(),
                matchingDocsObject.get(pos).getManagerContact(),
                matchingDocsObject.get(pos).getManagerEmail(),
                matchingDocsObject.get(pos).getManagerName(),
                matchingDocsObject.get(pos).getOwnerContact(),
                matchingDocsObject.get(pos).getPaymentStatus(),
                matchingDocsObject.get(pos).getPgName(),
                matchingDocsObject.get(pos).getPgStatus(),
                matchingDocsObject.get(pos).getState());

        Intent intent = new Intent(this, PGScreen.class);
        intent.putExtra("id", idList.get(pos));
        intent.putExtra("currentPG", currentPG);
        intent.putExtra("valueAc", valueAc);
        intent.putExtra("valueAb", valueAb);
        intent.putExtra("valueSeater", valueSeater);
        intent.putExtra("oneTimeDateText", oneTimeDateText);
        intent.putExtra("valueTimestampSecond", valueTimestampSecond);
        intent.putExtra("valueTimestampNanoSecond", valueTimestampNanoSecond);
        startActivity(intent);
    }

    @Override
    public void updateFilterValues(String valueAc, String valueAb, String valueSeater, String valueFood, String valueLaundry, long valueTimestampSecond, int valueTimestampNanoSecond, String oneTimeDateText) {
        this.valueAc = valueAc;
        this.valueAb = valueAb;
        this.valueSeater = valueSeater;
        this.valueFood = valueFood;
        this.valueLaundry = valueLaundry;
        this.valueTimestampSecond = valueTimestampSecond;
        this.valueTimestampNanoSecond = valueTimestampNanoSecond;
        this.oneTimeDateText = oneTimeDateText;
        this.nomenclature = "";
        this.nomenclature = this.nomenclature + (Boolean.parseBoolean(valueAc) ? "a" : "n");
        this.nomenclature = this.nomenclature + (Boolean.parseBoolean(valueAb) ? "i" : "s");
        this.nomenclature = this.nomenclature + valueSeater;
        bottomLayout.dismiss();
        lastQueriedDocument = null;
        matchingDocsObject.clear();
        if (lat != null && lng != null) {
            listResidingPlaces();
        }
    }
}