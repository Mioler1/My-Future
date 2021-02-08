package com.example.my_future.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_future.Models.Pressure;
import com.example.my_future.R;

import java.util.List;

public class PressureAdapter extends RecyclerView.Adapter<PressureAdapter.PressureAdapterVH> {
    LayoutInflater layoutInflater;
    List<Pressure> pressureList;
    Context context;

    public PressureAdapter(Context context, List<Pressure> pressureList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.pressureList = pressureList;
    }

    @NonNull
    @Override
    public PressureAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PressureAdapterVH(layoutInflater.inflate(R.layout.items_pressure, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PressureAdapterVH holder, int position) {
        Pressure pressure = pressureList.get(position);
        holder.namePressure.setText(pressure.getNamePressure());
        holder.sis.setText(pressure.getSis());
        holder.dis.setText(pressure.getDis());
    }

    @Override
    public int getItemCount() {
        return pressureList.size();
    }

    public class PressureAdapterVH extends RecyclerView.ViewHolder {
        TextView namePressure, sis, dis;

        public PressureAdapterVH(@NonNull View itemView) {
            super(itemView);
            namePressure = itemView.findViewById(R.id.text_pressure);
            sis = itemView.findViewById(R.id.text_sis);
            dis = itemView.findViewById(R.id.text_dis);

            View view = LayoutInflater.from(context).inflate(R.layout.activity_filling_data_health, null);
            TextView textPressure = (TextView) view.findViewById(R.id.visible_text_pressure);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textPressure.setText("fwfwf");
                    Toast.makeText(context, namePressure.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
