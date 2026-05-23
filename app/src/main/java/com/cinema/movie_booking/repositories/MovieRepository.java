package com.cinema.movie_booking.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cinema.movie_booking.api.RetrofitClient;
import com.cinema.movie_booking.models.CinemaGroup;
import com.cinema.movie_booking.models.Genre;
import com.cinema.movie_booking.models.Movie;
import com.cinema.movie_booking.models.MovieShowtimeResponse;
import com.cinema.movie_booking.models.Showtime;
import com.cinema.movie_booking.utils.Resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private final List<Call<?>> runningCalls =
            new ArrayList<>();
    public LiveData<Resource<List<Movie>>> getMovies(
            String status
    ) {

        MutableLiveData<Resource<List<Movie>>> data =
                new MutableLiveData<>();

        data.setValue(
                Resource.loading(null)
        );

        Call<List<Movie>> call =
                RetrofitClient
                        .getApiService()
                        .getMovies(status);

        runningCalls.add(call);

        call.enqueue(
                new Callback<List<Movie>>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<List<Movie>> call,
                            @NonNull Response<List<Movie>> response
                    ) {

                        if(response.isSuccessful()
                                && response.body()!=null){

                            data.postValue(
                                    Resource.success(
                                            response.body()
                                    )
                            );

                        }else{

                            data.postValue(
                                    Resource.error(
                                            "Failed to load movies",
                                            null
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<List<Movie>> call,
                            @NonNull Throwable t
                    ) {

                        if(!call.isCanceled()){

                            data.postValue(
                                    Resource.error(
                                            t.getMessage(),
                                            null
                                    )
                            );
                        }
                    }
                }
        );

        return data;
    }

    public LiveData<Resource<Movie>> getMovieById(Long movieId) {
        MutableLiveData<Resource<Movie>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getMovieById(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Movie not found", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<List<Genre>>> getAllGenres() {
        MutableLiveData<Resource<List<Genre>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getAllGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(@NonNull Call<List<Genre>> call, @NonNull Response<List<Genre>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Failed to load genres", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Genre>> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<List<MovieShowtimeResponse>>> getMovieShowtimes(Long movieId, String city, String date) {
        MutableLiveData<Resource<List<MovieShowtimeResponse>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getMovieShowtimes(movieId, city, date).enqueue(new Callback<List<MovieShowtimeResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MovieShowtimeResponse>> call, @NonNull Response<List<MovieShowtimeResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(Resource.success(response.body()));
                } else {
                    data.postValue(Resource.error("Failed to load showtimes", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MovieShowtimeResponse>> call, @NonNull Throwable t) {
                data.postValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public List<CinemaGroup> processAndGroupShowtimes(
            List<MovieShowtimeResponse> responseList,
            String selectedDate,
            String selectedCinema,
            String movieName
    ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
        
        String todayStr = dateFormat.format(new Date());
        String nowStr = dateTimeFormat.format(new Date());

        List<Showtime> filteredList = new ArrayList<>();

        if (responseList != null) {
            for (MovieShowtimeResponse res : responseList) {
                if (res.getShowtimes() != null) {
                    for (MovieShowtimeResponse.ShowtimeInfo info : res.getShowtimes()) {
                        
                        if (selectedCinema != null && !selectedCinema.equals("Tất cả rạp")) {
                            if (res.getCinemaName() == null || !res.getCinemaName().equals(selectedCinema)) continue;
                        }

                        String startTime = info.getStartTime();
                        String endTime = info.getEndTime();

                        if (startTime != null && startTime.contains("-")) {
                            if (!startTime.startsWith(selectedDate)) continue;
                        }

                        if (selectedDate.equals(todayStr)) {
                            if (endTime != null && endTime.contains("-") && endTime.compareTo(nowStr) <= 0) {
                                continue;
                            }
                        }

                        Showtime s = new Showtime(
                                info.getId(), info.getStartTime(), info.getEndTime(),
                                res.getCinemaName(), res.getRoomName(), res.getCinemaId()
                        );
                        s.setMovieName(movieName);
                        s.setBaseTicketPrice(info.getBasePrice() != null ? info.getBasePrice() : 0.0);
                        filteredList.add(s);
                    }
                }
            }
        }

        Map<String, Map<String, List<Showtime>>> hierarchicalGroups = new LinkedHashMap<>();
        for (Showtime s : filteredList) {
            String cName = s.getCinemaName() != null ? s.getCinemaName() : "Rạp không tên";
            String rName = s.getRoomName() != null ? s.getRoomName() : "Phòng thường";
            
            hierarchicalGroups.computeIfAbsent(cName, k -> new LinkedHashMap<>())
                    .computeIfAbsent(rName, k -> new ArrayList<>())
                    .add(s);
        }

        List<CinemaGroup> cinemaGroups = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<Showtime>>> cinemaEntry : hierarchicalGroups.entrySet()) {
            List<CinemaGroup.RoomGroup> roomGroups = new ArrayList<>();
            for (Map.Entry<String, List<Showtime>> roomEntry : cinemaEntry.getValue().entrySet()) {
                roomGroups.add(new CinemaGroup.RoomGroup(roomEntry.getKey(), roomEntry.getValue()));
            }
            cinemaGroups.add(new CinemaGroup(cinemaEntry.getKey(), roomGroups));
        }

        return cinemaGroups;
    }

    public String getYoutubeThumbnail(String url) {
        if (url == null) return "";
        String videoId = "";
        try {
            if (url.contains("v=")) {
                videoId = url.split("v=")[1];
                int index = videoId.indexOf("&");
                if (index != -1) videoId = videoId.substring(0, index);
            } else if (url.contains("youtu.be")) {
                videoId = url.substring(url.lastIndexOf("/") + 1);
            }
        } catch (Exception e) {
            return "";
        }
        return videoId.isEmpty() ? "" : "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }

    public MovieMetadata formatMovieMetadata(Movie movie) {
        MovieMetadata metadata = new MovieMetadata();
        if (movie != null) {
            metadata.setFormattedDuration(movie.getDuration() + " phút");
            metadata.setFormattedReleaseDate("Khởi chiếu: " + movie.getReleaseDate());
            metadata.setYoutubeThumbnail(getYoutubeThumbnail(movie.getTrailerUrl()));
        }
        return metadata;
    }

    public static class MovieMetadata {
        private String formattedDuration;
        private String formattedReleaseDate;
        private String youtubeThumbnail;

        public String getFormattedDuration() { return formattedDuration; }
        public void setFormattedDuration(String formattedDuration) { this.formattedDuration = formattedDuration; }
        public String getFormattedReleaseDate() { return formattedReleaseDate; }
        public void setFormattedReleaseDate(String formattedReleaseDate) { this.formattedReleaseDate = formattedReleaseDate; }
        public String getYoutubeThumbnail() { return youtubeThumbnail; }
        public void setYoutubeThumbnail(String youtubeThumbnail) { this.youtubeThumbnail = youtubeThumbnail; }
    }
    public void clear(){

        for(Call<?> call : runningCalls){

            if(!call.isCanceled()){

                call.cancel();
            }
        }

        runningCalls.clear();
    }
}
