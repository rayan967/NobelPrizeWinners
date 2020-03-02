package com.example.nobelprizewinners.Repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.example.nobelprizewinners.Adapters.NobelPrizeItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Database(entities = {NobelPrizeItem.class}, version = 1, exportSchema = false)
public abstract class NPRoomDatabase extends RoomDatabase {
    public abstract NPDao npDao();
    private static NPRoomDatabase INSTANCE;



    public static NPRoomDatabase getDatabase(final Context context, final HashMap<String,Boolean> categories, final int min, final int max, final boolean multiplewins) {
        synchronized (NPRoomDatabase.class) {
            RoomDatabase.Callback sRoomDatabaseCallback =
                    new RoomDatabase.Callback(){

                        @Override
                        public void onOpen (@NonNull SupportSQLiteDatabase db){
                            super.onOpen(db);
                            new PopulateFilteredDbAsync(INSTANCE,categories,min,max,multiplewins).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    };

            Log.d("Room Created","true");
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NPRoomDatabase.class, "np_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback( sRoomDatabaseCallback )
                    .build();
            INSTANCE.npDao().deleteAll();
        }

        return INSTANCE;
    }



    private static class PopulateFilteredDbAsync extends AsyncTask<Void, Void, Void> {

        private final NPDao mDao;
        String name,year,category;
        HashMap<String,Boolean> categories;
        int min, max;
        boolean multiplewins;
        List<String> category_list=new ArrayList<>();

        PopulateFilteredDbAsync(NPRoomDatabase db, HashMap<String,Boolean> categories, int min, int max, boolean multiplewins) {
            mDao = db.npDao();
            this.multiplewins=multiplewins;
            if(categories==null)
            {
                category_list.add("CHEMISTRY");
                category_list.add("PEACE");
                category_list.add("ECONOMICS");
                category_list.add("PHYSICS");
                category_list.add("LITERATURE");
                category_list.add("MEDICINE");
            }
            else
            {
                if(categories.get("Chemistry"))
                    category_list.add("CHEMISTRY");
                if(categories.get("Peace"))
                    category_list.add("PEACE");
                if(categories.get("Economics"))
                    category_list.add("ECONOMICS");
                if(categories.get("Physics"))
                    category_list.add("PHYSICS");
                if(categories.get("Literature"))
                    category_list.add("LITERATURE");
                if(categories.get("Medicine"))
                    category_list.add("MEDICINE");
            }
            if(max==0||min==0)
            {
                min=1900;
                max=2020;
            }
            else
            {
                this.min=min;
                this.max=max;
            }

        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {

                String url="http://api.nobelprize.org/v1/prize.json";

                Log.d("Request: ",url);

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .get()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonstring=response.body().string();


                if(jsonstring.charAt(0)=='<')
                    throw new Exception("");


                JSONObject js = new JSONObject(jsonstring);
                JSONObject temp1,temp2;
                JSONArray laureates;
                JSONArray jsonArray=js.getJSONArray("prizes");

                Log.d("Category_list: ", category_list.toString());
                Log.d("Min ", Integer.toString(min));
                Log.d("Max ", Integer.toString(max));
                if(!multiplewins) {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        temp1 = jsonArray.getJSONObject(i);
                        year = temp1.getString("year");
                        if (Integer.parseInt(year) >= min && Integer.parseInt(year) <= max) {
                            category = temp1.getString("category").toUpperCase();
                            if (category_list.contains(category)) {
                                if (temp1.has("laureates")) {
                                    laureates = temp1.getJSONArray("laureates");
                                    for (int j = 0; j < laureates.length(); j++) {

                                        temp2 = laureates.getJSONObject(j);
                                        String firstname = temp2.getString("firstname");
                                        if (temp2.has("surname")) {
                                            String surname = temp2.getString("surname");
                                            name = firstname + " " + surname;
                                        } else
                                            name = firstname;

                                        NobelPrizeItem word = new NobelPrizeItem(name, year, category);
                                        mDao.insert(word);
                                    }
                                } else {
                                    //No Row Added
                                }
                            }
                        }

                    }
                }




                else {
                    List<String> unique_id = new ArrayList<>();
                    List<String> nonunique = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        temp1 = jsonArray.getJSONObject(i);
                        year = temp1.getString("year");
                        category = temp1.getString("category").toUpperCase();
                        if (temp1.has("laureates")) {
                            laureates = temp1.getJSONArray("laureates");
                            for (int j = 0; j < laureates.length(); j++) {

                                temp2 = laureates.getJSONObject(j);
                                if (unique_id.contains(temp2.getString("id"))) {
                                    nonunique.add(temp2.getString("id"));
                                } else
                                    unique_id.add(temp2.getString("id"));
                            }
                        } else {
                            //No Row Added
                        }
                    }



                    for (int i = 0; i < jsonArray.length(); i++) {

                        temp1 = jsonArray.getJSONObject(i);
                        year = temp1.getString("year");
                        if (Integer.parseInt(year) >= min && Integer.parseInt(year) <= max) {
                            category = temp1.getString("category").toUpperCase();
                            if (category_list.contains(category)) {
                                if (temp1.has("laureates")) {
                                    laureates = temp1.getJSONArray("laureates");
                                    for (int j = 0; j < laureates.length(); j++) {

                                            temp2 = laureates.getJSONObject(j);
                                        if (nonunique.contains(temp2.getString("id"))) {

                                            String firstname = temp2.getString("firstname");
                                            if (temp2.has("surname")) {
                                                String surname = temp2.getString("surname");
                                                name = firstname + " " + surname;
                                            } else
                                                continue;

                                            NobelPrizeItem word = new NobelPrizeItem(name, year, category);
                                            mDao.insert(word);
                                        }
                                    }
                                } else {
                                    //No Row Added
                                }
                            }
                        }

                    }



                }
                return null;



            }
            catch(JSONException e){
                NobelPrizeItem word = new NobelPrizeItem("","","");
                mDao.insert(word);
                e.printStackTrace();
                return null;
            }
            catch(Exception e) {
                NobelPrizeItem word = new NobelPrizeItem("","","");
                mDao.insert(word);
                e.printStackTrace();
                return null;
            }
        }
    }

}
