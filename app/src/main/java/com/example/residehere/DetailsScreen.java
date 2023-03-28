package com.example.residehere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailsScreen extends AppCompatActivity {

    private Button contibueBtn;
    EditText name;
    EditText email;
    EditText address;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        contibueBtn = findViewById(R.id.continue_btn);
        name = findViewById(R.id.name_row_layout);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);

        contibueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText() != null && email.getText() != null && address.getText() != null){
                    HashMap<String, Object> details = new HashMap();
                    Map<String, Object> detailsMap = new HashMap<>();

                    details.put("name", name.getText().toString());
                    details.put("email", email.getText().toString());
                    details.put("address", address.getText().toString());

                    detailsMap.put("details", details);

                    db.collection("CustomerList").document(mAuth.getCurrentUser().getPhoneNumber())
                            .set(detailsMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else {
                    Toast.makeText(DetailsScreen.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}