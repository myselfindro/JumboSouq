package com.jumbosouq.com.fargment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.jumbosouq.com.R;

public class DrawerFragment extends Fragment {

    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.drawer_layout_item, container, false);//add_activity_frg


        return v;

    }
    }
