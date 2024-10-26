package com.example.fitcal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitcal.R;
import com.example.fitcal.bean.Meal;

import java.util.List;

//Me di cuenta que sería lo mismo para los demás y el objeto es el mismo, así que me ahorre adapters
public class BreakfastAdapter extends RecyclerView.Adapter<BreakfastAdapter.ViewHolder>{
    private List<Meal> desayunoList;
    private LayoutInflater mInflater;
    private Context context;

    public BreakfastAdapter(List<Meal> desayunoList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.desayunoList = desayunoList;
    }

    @Override
    public int getItemCount(){return desayunoList.size();}

    @Override
    public BreakfastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.irv_meal, null);
        return new BreakfastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BreakfastAdapter.ViewHolder holder, final int position){
        holder.bindData(desayunoList.get(position));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDesayuno(List<Meal> desayunos){desayunoList = desayunos;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCal, tvCantidad;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreAlimento);
            tvCal = itemView.findViewById(R.id.caloriasAlimento);
            imgIcon = itemView.findViewById(R.id.iconoAlimento);
        }

        public void bindData(final Meal desayuno) {
            tvNombre.setText("nombre: " + desayuno.getName());
            tvCal.setText(" "+desayuno.getCalorias() + "kcal");

            //iconos de la comida por hora
            if (desayuno.getTipo().equals("desayuno")) {
                imgIcon.setImageResource(R.drawable.ic_desayuno);
            } else if (desayuno.getTipo().equals("almuerzo")) {
                imgIcon.setImageResource(R.drawable.ic_almuerzo);
            } else if (desayuno.getTipo().equals("cena")) {
                imgIcon.setImageResource(R.drawable.ic_cena);
            } else {
                imgIcon.setImageResource(R.drawable.ic_orange);
            }
        }
    }
}