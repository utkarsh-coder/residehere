package com.example.residehere;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewAdapterBookingList extends RecyclerView.Adapter<RecyclerViewAdapterBookingList.ViewHolder>{

    private Context context;
    private ArrayList<Booking> list;
    private Boolean enablePayment;
    private RoomActivityRecyclerCallback roomActivityRecyclerCallback;

    public RecyclerViewAdapterBookingList(RoomActivityRecyclerCallback roomActivityRecyclerCallback, Context context, ArrayList<Booking> list, Boolean enablePayment) {
        this.roomActivityRecyclerCallback = roomActivityRecyclerCallback;
        this.context = context;
        this.list = list;
        this.enablePayment = enablePayment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.booking_row_layout, parent, false));
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewBookingStatus.setText(list.get(holder.getAdapterPosition()).getBookingStatus());
        holder.textViewPgName.setText(list.get(holder.getAdapterPosition()).getPgName());
        holder.textViewRoomName.setText(list.get(holder.getAdapterPosition()).getRoomName());
        holder.textViewStartingTimestamp.setText(list.get(holder.getAdapterPosition()).getBookingTimestamp().toDate()+"");
        holder.textViewBookingTimestamp.setText(list.get(holder.getAdapterPosition()).getCreatedAt().toDate().toString());
        holder.textViewResiderName.setText(list.get(holder.getAdapterPosition()).getResiderName());
        holder.textViewResiderContact.setText(list.get(holder.getAdapterPosition()).getResiderContact());
        if(list.get(holder.getAdapterPosition()).getEndingTimestamp().equals("infinity")){
            holder.textViewEndingTimestamp.setText("Till the time you want");
        }else {
            String endingTime = list.get(holder.getAdapterPosition()).getEndingTimestamp();
            Timestamp tempTimestamp = new Timestamp(Long.parseLong(endingTime.substring(endingTime.indexOf("=") + 1, endingTime.indexOf(","))), Integer.parseInt(endingTime.substring(endingTime.lastIndexOf("=") + 1, endingTime.lastIndexOf(")"))));
            holder.textViewEndingTimestamp.setText(tempTimestamp.toDate().toString());
        }
        if(list.get(holder.getAdapterPosition()).getBookingStatus().equals("booked"))
            holder.textViewBookingStatus.setTextColor(context.getResources().getColor(R.color.green));
        else if(list.get(holder.getAdapterPosition()).getBookingStatus().equals("cancelled"))
            holder.textViewBookingStatus.setTextColor(context.getResources().getColor(R.color.red));
        else
            holder.textViewBookingStatus.setTextColor(context.getResources().getColor(R.color.yellow));

        if (enablePayment){
            holder.paymentButton.setVisibility(View.VISIBLE);
        }
        holder.paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomActivityRecyclerCallback.goToBookRoom(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewBookingStatus;
        TextView textViewPgName;
        TextView textViewRoomName;
        TextView textViewStartingTimestamp;
        TextView textViewBookingTimestamp;
        TextView textViewEndingTimestamp;
        TextView textViewResiderName;
        TextView textViewResiderContact;
        Button paymentButton;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            textViewBookingStatus = itemView.findViewById(R.id.booking_status);
            textViewPgName = itemView.findViewById(R.id.pg_name);
            textViewRoomName = itemView.findViewById(R.id.room_name);
            paymentButton = itemView.findViewById(R.id.payment_btn);
            textViewStartingTimestamp = itemView.findViewById(R.id.starting_timestamp_content);
            textViewBookingTimestamp = itemView.findViewById(R.id.booking_timestamp_content);
            textViewEndingTimestamp = itemView.findViewById(R.id.ending_timestamp_content);
            textViewResiderName = itemView.findViewById(R.id.resider_name_content);
            textViewResiderContact = itemView.findViewById(R.id.resider_contact_content);
            paymentButton.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

        }
    }
}
