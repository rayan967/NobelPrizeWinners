package com.example.nobelprizewinners.ViewModels;

import android.app.Application;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.os.Handler;
import android.widget.DatePicker;

import com.example.nobelprizewinners.Adapters.NobelPrizeItem;
import com.example.nobelprizewinners.Repository.NPRepository;

public class NPViewModel extends AndroidViewModel {
    private NPRepository mRepository;
    private LiveData<List<NobelPrizeItem>> mAllRows;



    public NPViewModel(Application application) {
        super(application);
    }

    public void update(Application application, HashMap<String, Boolean> categories, int min, int max, boolean multiplewins) {
        mAllRows = null;
        mRepository = new NPRepository(application, categories, min, max, multiplewins);
        Log.d("Im called","aaaaaaaaaaaaaaaaaa");
        mAllRows = mRepository.getAllRows();
    }

    public LiveData<List<NobelPrizeItem>> getAllRows() {
        return mAllRows;
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public static List<NobelPrizeItem> modifyIfEmpty(List<NobelPrizeItem> words)
    {
        if(words.size()<1){
            words.add(new NobelPrizeItem("No Data Found","",""));
        return words;
        }
        else
            return words;
    }

    public void insert(NobelPrizeItem tt) {
        mRepository.insert(tt);
    }
}