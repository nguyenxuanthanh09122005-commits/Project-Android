package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.movie_booking.Phim;
import com.example.movie_booking.SuatChieu;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "MovieBooking.db";
    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        copyDatabase();
    }

    private void copyDatabase() {
        File dbFile = mContext.getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try {
                File parentDir = dbFile.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }

                InputStream input = mContext.getAssets().open(DB_NAME);
                OutputStream output = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                input.close();
                Log.d("DatabaseHelper", "Database copied successfully.");
            } catch (IOException e) {
                Log.e("DatabaseHelper", "Error copying database", e);
            }
        }
    }

    public List<Phim> getAllMovies() {
        List<Phim> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM Phim", null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String description = cursor.getString(2);
                    int duration = cursor.getInt(3);
                    String releaseDate = cursor.getString(4);
                    String imageName = cursor.getString(5);
                    String trailerUrl = cursor.getString(6);
                    String genre = cursor.getString(7);
                    String ageRating = cursor.getString(8);

                    movieList.add(new Phim(id, title, description, duration, releaseDate, imageName, trailerUrl, genre, ageRating));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error querying movies", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return movieList;
    }

    // Lấy suất chiếu theo Movie ID và nhóm theo tên Rạp
    public Map<String, List<SuatChieu>> getShowtimesGroupedByTheater(int movieId) {
        Map<String, List<SuatChieu>> groupedShowtimes = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT r.ten_rap, s.* FROM SuatChieu s " +
                      "JOIN PhongChieu p ON s.id_phong = p.id_phong " +
                      "JOIN Rap r ON p.id_rap = r.id_rap " +
                      "WHERE s.id_phim = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(movieId)});
        
        if (cursor.moveToFirst()) {
            do {
                String theaterName = cursor.getString(0);
                int id = cursor.getInt(1);
                int mId = cursor.getInt(2);
                int roomId = cursor.getInt(3);
                String startTime = cursor.getString(4);
                String endTime = cursor.getString(5);
                double price = cursor.getDouble(6);

                SuatChieu showtime = new SuatChieu(id, mId, roomId, startTime, endTime, price);
                
                if (!groupedShowtimes.containsKey(theaterName)) {
                    groupedShowtimes.put(theaterName, new ArrayList<>());
                }
                groupedShowtimes.get(theaterName).add(showtime);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return groupedShowtimes;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
