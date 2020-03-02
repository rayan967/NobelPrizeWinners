package com.example.nobelprizewinners.Adapters;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nobelprize_table")
public class NobelPrizeItem {



        @PrimaryKey(autoGenerate = true)
        public int key;
        @ColumnInfo(name = "Name")
        public String name;
        @ColumnInfo(name = "Year")
        public String year;
        @ColumnInfo(name = "Category")
        public String category;


        public NobelPrizeItem(String name, String year, String category) {
            this.name=name;
            this.year=year;
            this.category=category;
        }
        public NobelPrizeItem(){}


        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }


        public String getYear() {
            return year;
        }
        public void setYear(String year) {
            this.year = year;
        }


        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }
        

}
