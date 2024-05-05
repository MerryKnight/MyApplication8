package com.example.myapplication8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class Work3 extends Worker {
    public Work3(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int result = 10 * 3;
        Context applicationContext = getApplicationContext();
        ResultLiveData.getInstance().setResultWithDelay(result,6000);
        return Result.success();
    }
}
