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
import com.example.fitcal.bean.Ejercicio;
import com.example.fitcal.bean.Meal;

import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.ViewHolder>{
    private List<Ejercicio> ejercicioList;
    private LayoutInflater mInflater;
    private Context context;

    public EjercicioAdapter(List<Ejercicio> ejercicioList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.ejercicioList = ejercicioList;
    }

    @Override
    public int getItemCount(){return ejercicioList.size();}

    @Override
    public EjercicioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.irv_meal, null);
        return new EjercicioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EjercicioAdapter.ViewHolder holder, final int position){
        holder.bindData(ejercicioList.get(position));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEjercicio(List<Ejercicio> ejercicios){ejercicioList = ejercicios;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCal, tvCantidad;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.nombreAlimento);
            tvCal = itemView.findViewById(R.id.caloriasAlimento);
            imgIcon = itemView.findViewById(R.id.iconoAlimento);
        }

        public void bindData(final Ejercicio ejercicio) {
            tvNombre.setText("nombre: " + ejercicio.getName());
            tvCal.setText(" "+ejercicio.getCaloria() + " kcal");

            imgIcon.setImageResource(R.drawable.ic_exercise);

        }
    }
}
