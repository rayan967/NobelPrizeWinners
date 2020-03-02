package com.example.nobelprizewinners.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nobelprizewinners.Adapters.NobelPrizeItem;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NPRepository {
    private LiveData<List<NobelPrizeItem>> mAllRows;
    private NPDao mNPDao;



    public NPRepository(Application application, HashMap<String, Boolean> categories, int min, int max, boolean multiplewins) {
        NPRoomDatabase db = NPRoomDatabase.getDatabase(application, categories, min, max, multiplewins);
        mNPDao = db.npDao();
        mAllRows = mNPDao.getAllRows();
    }

    public void update(Application application, HashMap<String, Boolean> categories, int min, int max, boolean multiplewins) {
        NPRoomDatabase db = NPRoomDatabase.getDatabase(application, categories, min, max, multiplewins);
        mNPDao = db.npDao();
        mAllRows = mNPDao.getAllRows();
    }


    public LiveData<List<NobelPrizeItem>> getAllRows() {
        return mAllRows;
    }

    public void deleteAll()
    {
        mNPDao.deleteAll();
    }


    public void insert (NobelPrizeItem np) {
        new insertAsyncTask(mNPDao).execute(np);
    }

    private static class insertAsyncTask extends AsyncTask<NobelPrizeItem, Void, Void> {

        private NPDao mAsyncTaskDao;

        insertAsyncTask(NPDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NobelPrizeItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
