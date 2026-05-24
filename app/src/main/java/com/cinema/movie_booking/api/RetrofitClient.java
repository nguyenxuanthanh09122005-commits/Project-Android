package com.cinema.movie_booking.api;

import android.content.Context;
import android.net.TrafficStats;

import com.cinema.movie_booking.BuildConfig;
import com.cinema.movie_booking.MovieApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static volatile ApiService apiService;
    private static volatile OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (RetrofitClient.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true);

                    // Add Auth Interceptor first
                    builder.addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();

                        Context context = MovieApplication.getAppContext();
                        if (context != null) {
                            String token = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                                    .getString("auth_token", null);

                            if (token != null && !token.isEmpty()) {
                                requestBuilder.header("Authorization", "Bearer " + token);
                            }
                        }

                        TrafficStats.setThreadStatsTag(0xF00D);
                        try {
                            return chain.proceed(requestBuilder.build());
                        } finally {
                            TrafficStats.clearThreadStatsTag();
                        }
                    });

                    // Add Logging Interceptor last to see the headers added by Auth Interceptor
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(logging);

                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (RetrofitClient.class) {
                if (apiService == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BuildConfig.BASE_URL)
                            .client(getOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    apiService = retrofit.create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
