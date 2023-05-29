package com.example.mysearchingapp;

import android.view.View;

public interface ItemClickListener {

    public void onClick(View view, int position);
    public void onShareClick(View view, int position);
}
