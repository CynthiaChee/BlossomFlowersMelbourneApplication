package com.example.mysearchingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysearchingapp.util.Util;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity implements ItemClickListener {

    //Initializing variables
    ArrayList<Order> myOrders = new ArrayList<>();
    String username;
    RecyclerView myRecyclerView;
    OrdersRecyclerViewAdapter myOrdersRecyclerViewAdapter;
    OrderDatabaseHelper myOrdersDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        myOrdersDBHelper = new OrderDatabaseHelper(this);

        //Get current logged in user
        SharedPreferences prefs = getSharedPreferences(Util.SHARED_PREF_DATA, MODE_PRIVATE);
        username = prefs.getString(Util.LOGGEDIN_USER, "");
        myOrders = myOrdersDBHelper.fetchAllOrders(username);

        //Find view by ID
        myRecyclerView = findViewById(R.id.ordersRecyclerView);

        //Set adapter to recycler view
        myOrdersRecyclerViewAdapter = new OrdersRecyclerViewAdapter(myOrders,  this);
        myRecyclerView.setAdapter(myOrdersRecyclerViewAdapter);

        myOrdersRecyclerViewAdapter.setClickListener(this);
        myOrdersRecyclerViewAdapter.shareClickListener(this);

        myRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    //Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.flower_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //If an item on the menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.accountMenu:
                Intent accountIntent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(accountIntent);
                finish();
                return true;
            case R.id.myordersMenu:
                return true;
            case R.id.logoutMenu:
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //If an order is clicked
    @Override
    public void onClick(View view, int position) {
        final Order order = myOrders.get(position);
        createFragment(order);
    }

    public void createFragment(Order order)
    {
        Fragment fragment = new OrderFragment(order);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
    }

    //When share is clicked
    public void onShareClick(View view, int position) {
        Order order = myOrders.get(position);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String msg = "I am sending " + order.getReceiverName() + " flowers on " + order.getDate() + " at " + order.getTime();
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    //If add delivery is clicked
    public void addDeliveryClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), DeliveryActivity.class);
        startActivity(intent);
    }

}
