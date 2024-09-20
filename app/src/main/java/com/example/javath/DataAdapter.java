package com.example.javath;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<DataModel> dataList;

    public DataAdapter(List<DataModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel data = dataList.get(position);
        holder.index.setText(String.valueOf(position + 1));
        holder.outletCode.setText(data.getOutletCode());
        holder.totalSales.setText(data.getTotalSales());
        holder.date.setText(data.getDate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView index, outletCode, totalSales, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index);
            outletCode = itemView.findViewById(R.id.outletCode);
            totalSales = itemView.findViewById(R.id.totalSales);
            date = itemView.findViewById(R.id.date);
        }
    }
}
