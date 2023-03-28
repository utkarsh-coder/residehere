package com.example.residehere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.residehere.ModelQueryAutocomplete.ListClass;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    private ArrayList<ListClass> list;
    private Context context;
    private PgActivityRecyclerCallback recyclerCallbackContext;

    public RecyclerviewAdapter(Context context, PgActivityRecyclerCallback recyclerCallbackContext, ArrayList<ListClass> list) {
        this.list = list;
        this.recyclerCallbackContext = recyclerCallbackContext;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.search_layout, parent, false), recyclerCallbackContext);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = list.get(holder.getAdapterPosition()).getDescription();
                recyclerCallbackContext.goToPrintResides(description);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        PgActivityRecyclerCallback pgActivityRecyclerCallback;
        public ViewHolder(@NonNull View itemView, PgActivityRecyclerCallback pgActivityRecyclerCallback){
            super(itemView);
            this.pgActivityRecyclerCallback = pgActivityRecyclerCallback;
            textView = itemView.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
