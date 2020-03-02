package com.example.nobelprizewinners.Repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.nobelprizewinners.Adapters.NobelPrizeItem;

import java.util.List;

@Dao
public interface NPDao {

    @Insert
    void insert(NobelPrizeItem np);

    @Query("DELETE FROM nobelprize_table")
    void deleteAll();

    @Query("SELECT * from nobelprize_table")
    LiveData<List<NobelPrizeItem>> getAllRows();

}
