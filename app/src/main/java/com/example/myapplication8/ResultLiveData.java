package com.example.myapplication8;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ResultLiveData {
    private static final ResultLiveData instance = new ResultLiveData();
    private static Handler handler = new Handler();
    private MutableLiveData<Integer> resultLiveData = new MutableLiveData<>();

    private ResultLiveData() {
    }

    public static ResultLiveData getInstance() {
        return instance;
    }

    public LiveData<Integer> getResultLiveData() {
        return resultLiveData;
    }

    public void setResult(int result) {
        resultLiveData.postValue(result);
    }

    public void setResultWithDelay(final int result, long delayMillis) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resultLiveData.postValue(result);
            }
        }, delayMillis);
    }
}
