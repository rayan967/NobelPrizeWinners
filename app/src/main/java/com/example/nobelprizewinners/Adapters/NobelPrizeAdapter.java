package com.example.nobelprizewinners.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nobelprizewinners.R;

import java.util.List;

public class NobelPrizeAdapter extends RecyclerView.Adapter<NobelPrizeAdapter.MyViewHolder> {

    private List<NobelPrizeItem> NPList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,year,category;


        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.textView1);
            year =  view.findViewById(R.id.textView2);
            category = view.findViewById(R.id.textView3);
        }
    }


    public NobelPrizeAdapter(List<NobelPrizeItem> NPList) {
        this.NPList = NPList;
        hasStableIds();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nobel_prize_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NobelPrizeItem TT = NPList.get(position);
        holder.name.setText(TT.getName());
        holder.year.setText(TT.getYear());
        holder.category.setText(TT.getCategory());
    }

    @Override
    public int getItemCount() {
        if(NPList!=null)
            return NPList.size();
        else
            return 0;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setRows(List<NobelPrizeItem> nplist){
        NPList = nplist;
        notifyDataSetChanged();
    }
}
