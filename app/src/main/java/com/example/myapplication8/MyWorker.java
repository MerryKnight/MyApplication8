package com.example.myapplication8;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public final String TAG = "MY_TAG";
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters
            workerParams) {
        super(context, workerParams);
        int x = workerParams.getInputData().getInt("key2",0);
        String s = getInputData().getString("key1");


    }
    @NonNull
    @Override
    public Result doWork() {
        Log.v(TAG, "Work is in progress");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Work finished");
        return Worker.Result.success();
    }
}

