package com.example.residehere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class otpVerificationScreen extends AppCompatActivity {

    private String otp;
    private String enterotp;
    private EditText editText;
    private Button button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView errorTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification_screen);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editText = findViewById(R.id.editText_search);
        button = findViewById(R.id.button);
        errorTextView = findViewById(R.id.error_textView_otp);
        progressBar = findViewById(R.id.progress_bar_otp);

        enterotp = null;
        otp = getIntent().getStringExtra("backendotp");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                editText.setEnabled(false);
                button.setEnabled(false);
                enterotp = editText.getText().toString();
                Log.d("testing", otp);
                Log.d("testing", enterotp);
                if(!enterotp.equals("")) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otp, enterotp);

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
                                    } else {
                                        Log.d("testing", "unable to get data from the server... Please retry later");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText("Wrong OTP entered, please re-enter");
                            editText.setEnabled(true);
                            button.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    button.setEnabled(true);
                    editText.setEnabled(true);
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Enter OTP");
                }
            }
        });
    }
}