package com.example.movie_booking.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movie_booking.R;

public class TrailerDialogFragment extends DialogFragment {

    private String trailerUrl;

    public static TrailerDialogFragment newInstance(String url) {
        TrailerDialogFragment fragment = new TrailerDialogFragment();
        Bundle args = new Bundle();
        args.putString("trailer_url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trailerUrl = getArguments().getString("trailer_url");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_trailer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        WebView wvTrailer = view.findViewById(R.id.wvTrailer);
        ProgressBar pbLoading = view.findViewById(R.id.pbLoading);
        view.findViewById(R.id.btnCloseTrailer).setOnClickListener(v -> dismiss());

        WebSettings webSettings = wvTrailer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        
        wvTrailer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (pbLoading != null) pbLoading.setVisibility(View.GONE);
            }
        });

        wvTrailer.setWebChromeClient(new WebChromeClient());

        String finalUrl = trailerUrl;
        if (trailerUrl != null) {
            if (trailerUrl.contains("watch?v=")) {
                finalUrl = trailerUrl.replace("watch?v=", "embed/");
            } else if (trailerUrl.contains("youtu.be/")) {
                finalUrl = trailerUrl.replace("youtu.be/", "youtube.com/embed/");
            }
        }

        String html = "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"" + finalUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        wvTrailer.loadData(html, "text/html", "utf-8");
    }

    @Override
    public void onDestroyView() {
        View view = getView();
        if (view != null) {
            WebView wvTrailer = view.findViewById(R.id.wvTrailer);
            if (wvTrailer != null) {
                wvTrailer.destroy();
            }
        }
        super.onDestroyView();
    }
}
