package com.cinema.movie_booking.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.ViewPagerMovieAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private Button btnLogin;

    private TextView txtHello;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_home,
                container,
                false);

        initViews(view);

        setupUser();

        setupViewPager();

        return view;
    }

    private void initViews(View view){

        tabLayout = view.findViewById(R.id.tabLayout);

        viewPager2 = view.findViewById(R.id.viewPagerMovie);

        btnLogin = view.findViewById(R.id.btnLogin);

        txtHello = view.findViewById(R.id.txtHello);
    }

    private void setupUser(){

        SharedPreferences prefs =
                requireActivity()
                        .getSharedPreferences(
                                "USER_FILE",
                                Context.MODE_PRIVATE);

        String fullName =
                prefs.getString("FULL_NAME", "");

        if(fullName.isEmpty()){

            btnLogin.setVisibility(View.VISIBLE);

            txtHello.setVisibility(View.GONE);
        }
        else{

            btnLogin.setVisibility(View.GONE);

            txtHello.setVisibility(View.VISIBLE);

            txtHello.setText("Chào " + fullName);
        }
//        btnLogin.setOnClickListener(v -> {
//
//            Intent intent =
//                    new Intent(
//                            requireContext(),
//                            LoginActivity.class);
//
//            startActivity(intent);
//        });
    }

    private void setupViewPager(){

        ViewPagerMovieAdapter adapter =
                new ViewPagerMovieAdapter(this);

        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(
                tabLayout,
                viewPager2,
                (tab, position) -> {

                    if(position == 0){
                        tab.setText("Sắp chiếu");
                    }
                    else if(position == 1){
                        tab.setText("Đang chiếu");
                    }
                    else{
                        tab.setText("Suất chiếu sớm");
                    }
                }).attach();

        viewPager2.setCurrentItem(1, false);
    }

}
