package com.cinema.movie_booking.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.cinema.movie_booking.R;
import com.cinema.movie_booking.adapters.ViewPagerMovieAdapter;
import com.cinema.movie_booking.viewmodels.UserViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private Button btnLogin;
    private TextView txtHello;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupViewModel();
        setupUser();
        setupViewPager();
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPagerMovie);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtHello = view.findViewById(R.id.txtHello);
    }

    private void setupViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void setupUser() {
        userViewModel.getUserFullName().observe(getViewLifecycleOwner(), fullName -> {
            if (!isAdded() || getView() == null) return;

            boolean isLoggedIn = fullName != null && !fullName.trim().isEmpty();

            btnLogin.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
            txtHello.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);

            if (isLoggedIn) {
                txtHello.setText("Chào " + fullName);
            }
        });
    }

    private void setupViewPager() {

        ViewPagerMovieAdapter adapter =
                new ViewPagerMovieAdapter(this);

        viewPager2.setAdapter(adapter);

        // Defer setting offscreen page limit to avoid blocking the main thread during initial startup rendering
        viewPager2.post(() -> {
            if (isAdded() && viewPager2 != null) {
                viewPager2.setOffscreenPageLimit(1);
            }
        });

        new TabLayoutMediator(
                tabLayout,
                viewPager2,
                (tab, position) -> {

                    switch(position){

                        case 0:
                            tab.setText("Sắp chiếu");
                            break;

                        case 1:
                            tab.setText("Đang chiếu");
                            break;

                        case 2:
                            tab.setText("Suất chiếu sớm");
                            break;
                    }

                }
        ).attach();

        viewPager2.setCurrentItem(
                1,
                false
        );
    }
}
