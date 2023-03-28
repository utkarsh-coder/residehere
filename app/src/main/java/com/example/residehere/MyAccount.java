package com.example.residehere;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyAccount extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText name;
    private EditText contact;
    private EditText email;
    private EditText address;
    private HashMap<String, Object> customerData;
    private Button updateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        name = view.findViewById(R.id.name_editText);
        email = view.findViewById(R.id.email_editText);
        address = view.findViewById(R.id.address_editText);
        contact = view.findViewById(R.id.phone_editText);
        updateButton = view.findViewById(R.id.top_bar_btn);
        contact.setEnabled(false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        customerData = (HashMap<String, Object>) MainScreen.data.get("details");

        if(MainScreen.data == null){
            db.collection("CustomerList").document(mAuth.getCurrentUser().getPhoneNumber())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        MainScreen.data = (HashMap<String, Object>)task.getResult().getData();
                    }
                }
            });
        }

        name.setText(customerData.get("name").toString());
        contact.setText(mAuth.getCurrentUser().getPhoneNumber());
        email.setText(customerData.get("email").toString());
        address.setText(customerData.get("address").toString());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText()!=null && email.getText()!=null && address.getText()!=null){
                    Map<String, String> details = new HashMap<>();
                    Map<String, Object> detailsMap = new HashMap<>();
                    details.put("name", name.getText().toString());
                    details.put("email", email.getText().toString());
                    details.put("address", address.getText().toString());
                    detailsMap.put("details", details);

                    db.collection("CustomerList").document(mAuth.getCurrentUser().getPhoneNumber())
                            .update(detailsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            MainScreen.data.put("details", details);
                        }
                    });
                }
            }
        });

        return view;
    }
}
