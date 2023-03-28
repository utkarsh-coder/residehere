package com.example.residehere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class RecyclerViewAdapterResidingList extends RecyclerView.Adapter<RecyclerViewAdapterResidingList.ViewHolder> {

    private Context context;
    private ArrayList<PgObject> list;
    private PgActivityRecyclerCallback recyclerCallbackContext;

    public RecyclerViewAdapterResidingList(Context context, PgActivityRecyclerCallback recyclerCallbackContext, ArrayList<PgObject> list) {
        this.context = context;
        this.recyclerCallbackContext = recyclerCallbackContext;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false));
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(list.get(position).getPgName());
        holder.addressTextView.setText(list.get(position).getAddress());
        holder.cityTextView.setText(list.get(position).getCity());
        holder.stateTextView.setText(list.get(position).getState());
        // TODO: 05-02-2022 change price after adding price option to firestore
        holder.priceTextView.setText("sample output");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                recyclerCallbackContext.goToSelectCurrentPG(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTextView;
        TextView addressTextView;
        TextView cityTextView;
        TextView stateTextView;
        TextView priceTextView;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_row_layout);
            addressTextView = itemView.findViewById(R.id.address_row_layout);
            cityTextView = itemView.findViewById(R.id.city_row_layout);
            stateTextView = itemView.findViewById(R.id.state_row_layout);
            priceTextView = itemView.findViewById(R.id.price_row_layout);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
