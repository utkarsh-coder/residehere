package com.example.residehere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerViewAdapterRoom extends RecyclerView.Adapter<RecyclerViewAdapterRoom.ViewHolder> {

    private Context context;
    private RoomActivityRecyclerCallback roomActivityRecyclerCallback;
    private ArrayList<RoomDescriptionObject> roomList;
    private ArrayList<String> roomIdList;

    public RecyclerViewAdapterRoom(Context context, RoomActivityRecyclerCallback roomActivityRecyclerCallback, ArrayList<RoomDescriptionObject> roomList, ArrayList<String> roomIdList) {
        this.context = context;
        this.roomActivityRecyclerCallback = roomActivityRecyclerCallback;
        this.roomList = roomList;
        this.roomIdList = roomIdList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterRoom.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewAdapterRoom.ViewHolder(LayoutInflater.from(context).inflate(R.layout.room_description, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterRoom.ViewHolder holder, int position) {
        holder.nameTextView.setText(roomList.get(holder.getAdapterPosition()).getName());
        holder.acTextView.setText(roomList.get(holder.getAdapterPosition()).getAc());
        holder.abTextView.setText(roomList.get(holder.getAdapterPosition()).getAb());
        holder.seaterTextView.setText(roomList.get(holder.getAdapterPosition()).getSeater());
        holder.priceTextView.setText(roomList.get(holder.getAdapterPosition()).getPrice());

        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomActivityRecyclerCallback.goToBookRoom(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTextView;
        TextView acTextView;
        TextView abTextView;
        TextView seaterTextView;
        TextView priceTextView;
        Button bookBtn;

        public ViewHolder(@io.reactivex.annotations.NonNull View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.room_name);
            acTextView = itemView.findViewById(R.id.room_ac);
            abTextView = itemView.findViewById(R.id.room_ab);
            seaterTextView = itemView.findViewById(R.id.room_seater);
            priceTextView = itemView.findViewById(R.id.room_price);
            bookBtn = itemView.findViewById(R.id.book_button);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
