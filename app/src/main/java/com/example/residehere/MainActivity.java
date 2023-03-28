package com.example.residehere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String phone;
    private Button submitBtn;
    private TextView errorTextView;
    private EditText phoneEdittext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CountryCodePicker ccp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_empty_progress_bar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null){
            db.collection("CustomerList").document(mAuth.getCurrentUser().getPhoneNumber())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), DetailsScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        else {
            setContentView(R.layout.activity_main);
            submitBtn = findViewById(R.id.submitBtn);
            errorTextView = findViewById(R.id.error_textView);
            phoneEdittext = findViewById(R.id.phoneNumber);
            ccp = findViewById(R.id.ccp);
            progressBar = findViewById(R.id.progress_bar_main_activity);

            phoneEdittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() == 10){
                        submitBtn.setEnabled(true);
                        errorTextView.setVisibility(View.INVISIBLE);
                    }else{
                        submitBtn.setEnabled(false);
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                }
            });

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ccp.setEnabled(false);
                    phoneEdittext.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    submitBtn.setEnabled(false);
                    phone = phoneEdittext.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+"+ccp.getSelectedCountryCode()+phone,
                            60,
                            TimeUnit.SECONDS,
                            MainActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    Log.d("testing", "completed");

                                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        db.collection("CustomerList").document(mAuth.getCurrentUser().getPhoneNumber())
                                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                if(documentSnapshot.exists()){
                                                                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                                                                    startActivity(intent);
                                                                }
                                                                else {
                                                                    Intent intent = new Intent(getApplicationContext(), DetailsScreen.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });
                                                    } else {
                                                        Log.d("testing", "unable to get data from the server... Please retry later");
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Log.d("testing", "failed");
                                    ccp.setEnabled(true);
                                    phoneEdittext.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if(phoneEdittext.getText().toString().length() == 10){
                                        submitBtn.setEnabled(true);
                                    }else {
                                        submitBtn.setEnabled(false);
                                    }
                                    Toast.makeText(getApplicationContext(), "some error occured", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);

                                    Intent intent = new Intent(getApplicationContext(), otpVerificationScreen.class);
                                    intent.putExtra("backendotp", s);
                                    startActivity(intent);
                                }
                            }
                    );
                }
            });
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        phoneEdittext.setEnabled(true);
        ccp.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
        if (phoneEdittext.getText().toString().length() == 10){
            submitBtn.setEnabled(true);
        }else {
            submitBtn.setEnabled(false);
        }
    }
}