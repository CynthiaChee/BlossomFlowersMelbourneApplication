package com.example.mysearchingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysearchingapp.util.Util;

import java.util.ArrayList;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {

    //Instance variables
    private ArrayList<Order> orders;
    private Context context;
    private ItemClickListener itemClickListener, shareClickListener;

    //Constructor for initialization
    public OrdersRecyclerViewAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageBitmap(Util.getBitmapFromBytesArray(orders.get(position).getFlowerImageBytes()));
        holder.receiverName.setText(orders.get(position).getReceiverName());
        holder.date.setText(orders.get(position).getDate());
        holder.time.setText(orders.get(position).getTime());
        holder.destination.setText(orders.get(position).getDestination());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void shareClickListener(ItemClickListener shareClickListener) {
        this.shareClickListener = shareClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView receiverName, date, time, destination;
        ImageView image, shareImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverName = itemView.findViewById(R.id.orderNameText);
            date = itemView.findViewById(R.id.dateTextView);
            time = itemView.findViewById(R.id.orderTimeTextView);
            destination = itemView.findViewById(R.id.locationTextView);
            image = itemView.findViewById(R.id.orderImageView);
            shareImage = itemView.findViewById(R.id.shareButton);

            itemView.setOnClickListener(this);
            shareImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {
                itemClickListener.onClick(view, getAdapterPosition()); //Defined in HomeActivity and MyOrdersActivity
            }
            if (view == shareImage) {
                shareClickListener.onShareClick(view, getAdapterPosition()); //Defined in HomeActivity and MyOrdersActivity
            }
        }
    }
}
