package com.example.nobelprizewinners.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nobelprizewinners.Adapters.NobelPrizeAdapter;
import com.example.nobelprizewinners.Adapters.NobelPrizeItem;
import com.example.nobelprizewinners.R;
import com.example.nobelprizewinners.ViewModels.NPViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity  {


    private List<NobelPrizeItem> NPList=new ArrayList<>();
    NobelPrizeAdapter mAdapter;
    private NPViewModel mNPViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isOnline())
        {
            AlertDialog builder = new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("Error")
                    .setMessage("No Internet connection")
                    .setPositiveButton("OK",null)
                    .show();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new NobelPrizeAdapter(NPList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        final int year=Calendar.getInstance().get(Calendar.YEAR);



        mNPViewModel = new ViewModelProvider(MainActivity.this).get(NPViewModel.class);

        mNPViewModel.update(getApplication(),null,1900,year,false);
        mNPViewModel.getAllRows().observe(MainActivity.this, new Observer<List<NobelPrizeItem>>() {
            @Override
            public void onChanged(@Nullable List<NobelPrizeItem> words) {
                mAdapter.setRows(words);
            }
        });







        ImageView imageView=findViewById(R.id.filter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_box, null);
                final RangeSeekBar<Integer> rangeSeekBar = layout.findViewById(R.id.rangeSeekBar);
                final CheckBox chemistry=layout.findViewById(R.id.checkBox);
                final CheckBox peace=layout.findViewById(R.id.checkBox2);
                final CheckBox economics=layout.findViewById(R.id.checkBox3);
                final CheckBox physics=layout.findViewById(R.id.checkBox4);
                final CheckBox literature=layout.findViewById(R.id.checkBox5);
                final CheckBox medicine=layout.findViewById(R.id.checkBox6);
                final CheckBox multiple=layout.findViewById(R.id.checkBox7);

                AlertDialog builder = new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Filter By")
                        .setView(layout)
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String,Boolean> hashMap=new HashMap<>();
                                if(chemistry.isChecked())
                                    hashMap.put("Chemistry",true);
                                else
                                    hashMap.put("Chemistry",false);

                                if(peace.isChecked())
                                    hashMap.put("Peace",true);
                                else
                                    hashMap.put("Peace",false);

                                if(economics.isChecked())
                                    hashMap.put("Economics",true);
                                else
                                    hashMap.put("Economics",false);

                                if(physics.isChecked())
                                    hashMap.put("Physics",true);
                                else
                                    hashMap.put("Physics",false);

                                if(literature.isChecked())
                                    hashMap.put("Literature",true);
                                else
                                    hashMap.put("Literature",false);

                                if(medicine.isChecked())
                                    hashMap.put("Medicine",true);
                                else
                                    hashMap.put("Medicine",false);
                                boolean multiplewins;
                                if(multiple.isChecked())
                                    multiplewins=true;
                                else
                                    multiplewins=false;
                                rangeSeekBar.setRangeValues(1900,year);
                                TextView maxyear=layout.findViewById(R.id.maxyear);
                                maxyear.setText(Integer.toString(year));
                                int min=rangeSeekBar.getSelectedMinValue();
                                int max=rangeSeekBar.getSelectedMaxValue();


                                mNPViewModel = new ViewModelProvider(MainActivity.this).get(NPViewModel.class);

                                mNPViewModel.update(getApplication(),hashMap,min,max,multiplewins);
                                mNPViewModel.getAllRows().observe(MainActivity.this, new Observer<List<NobelPrizeItem>>() {
                                    @Override
                                    public void onChanged(@Nullable List<NobelPrizeItem> words) {

                                        words=NPViewModel.modifyIfEmpty(words);
                                        mAdapter.setRows(words);
                                    }
                                });



                            }
                        })
                        .show();
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }



}
