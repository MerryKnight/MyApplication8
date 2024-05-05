package com.example.myapplication8;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "RRR";
    private Handler handler = new Handler();
    TextView resultTextView;
    Button bStart;
    Button bStart1;
    Button bStart2;
    private ImageView imageView;
    String www = "https://random.dog/woof.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.btStart);
        imageView = findViewById(R.id.imageView);
        bStart2 = findViewById(R.id.btStart2);
        bStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadImageTask().execute("https://random.dog/woof.json");
            }
        });
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeWorkRequest firstWorkRequest = new OneTimeWorkRequest.Builder(Work1.class)
                        .build();
                OneTimeWorkRequest secondWorkRequest = new OneTimeWorkRequest.Builder(Work2.class)
                        .build();
                OneTimeWorkRequest thirdWorkRequest = new OneTimeWorkRequest.Builder(Work3.class)
                        .build();
                WorkManager.getInstance()
                        .beginWith(firstWorkRequest)
                        .then(secondWorkRequest)
                        .then(thirdWorkRequest)
                        .enqueue();
            }
        });
        bStart1 = findViewById(R.id.btStart1);
        bStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeWorkRequest firstWorkRequest = new OneTimeWorkRequest.Builder(Work1.class)
                        .build();
                OneTimeWorkRequest secondWorkRequest = new OneTimeWorkRequest.Builder(Work2.class)
                        .build();
                WorkManager.getInstance()
                        .enqueue(firstWorkRequest);
                WorkManager.getInstance()
                        .enqueue(secondWorkRequest);
            }
        });
        ResultLiveData.getInstance().getResultLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(final Integer result) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Результат: " + result);
                    }
                }, 3000);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String jsonResponse = convertInputStreamToString(inputStream);
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String imageUrl = jsonObject.getString("url");
                    bitmap = downloadBitmap(imageUrl);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead));
        }
        return stringBuilder.toString();
    }

    private Bitmap downloadBitmap(String imageUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
        connection.connect();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }
}