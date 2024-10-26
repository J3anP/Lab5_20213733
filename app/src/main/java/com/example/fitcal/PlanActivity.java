package com.example.fitcal;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitcal.adapter.BreakfastAdapter;
import com.example.fitcal.bean.Meal;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity {
    private BreakfastAdapter desayunoAdapter, almuerzoAdapter, cenaAdapter;
    private List<Meal> desayunoList, almuerzoList, cenaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView caloriesText = findViewById(R.id.calories_text);

        int caloriasConsumidas = 125;
        int caloriasObjetivo = 2476;

        //Porcentaje de progreso
        int progreso = (int) ((caloriasConsumidas / (float) caloriasObjetivo) * 100);

        caloriesText.setText(caloriasConsumidas + " / " + caloriasObjetivo + " kcal");
        progressBar.setProgress(progreso);

        /*---------------------------*/

        // Inicializar las listas y los adaptadores
        desayunoList = new ArrayList<>();
        almuerzoList = new ArrayList<>();
        cenaList = new ArrayList<>();

        desayunoAdapter = new BreakfastAdapter(desayunoList,this);
        almuerzoAdapter = new BreakfastAdapter(almuerzoList,this);
        cenaAdapter = new BreakfastAdapter(cenaList,this);


        // Configurar RecyclerViews
        RecyclerView recyclerDesayuno = findViewById(R.id.recyclerDesayuno);
        recyclerDesayuno.setLayoutManager(new LinearLayoutManager(this));
        recyclerDesayuno.setAdapter(desayunoAdapter);

        RecyclerView recyclerAlmuerzo = findViewById(R.id.recyclerAlmuerzo);
        recyclerAlmuerzo.setLayoutManager(new LinearLayoutManager(this));
        recyclerAlmuerzo.setAdapter(almuerzoAdapter);

        RecyclerView recyclerCena = findViewById(R.id.recyclerCena);
        recyclerCena.setLayoutManager(new LinearLayoutManager(this));
        recyclerCena.setAdapter(cenaAdapter);

        // Botones para agregar alimentos
        Button btnAgregarAlimentoDesayuno = findViewById(R.id.btnAgregarAlimentoDesayuno);
        btnAgregarAlimentoDesayuno.setOnClickListener(v -> showBottomSheetDialog("desayuno",desayunoAdapter, desayunoList));

        Button btnAgregarAlimentoAlmuerzo = findViewById(R.id.btnAgregarAlimentoAlmuerzo);
        btnAgregarAlimentoAlmuerzo.setOnClickListener(v -> addFood(almuerzoAdapter, almuerzoList, "almuerzo", "arroz con pollo", 120.0f));

        Button btnAgregarAlimentoCena = findViewById(R.id.btnAgregarAlimentoCena);
        btnAgregarAlimentoCena.setOnClickListener(v -> addFood(cenaAdapter, cenaList, "cena", "te", 80.0f));
    }


    private void addFood(BreakfastAdapter adapter, List<Meal> lista, String tipo, String nombre, float calorias) {
        Meal newMeal = new Meal();
        newMeal.setName(nombre);
        newMeal.setTipo(tipo);
        newMeal.setCalorias(calorias);
        lista.add(newMeal);
        adapter.notifyItemInserted(lista.size() - 1);
    }

    private void showBottomSheetDialog(String tipo, BreakfastAdapter adapter, List<Meal> lista) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_meal);

        TextInputEditText txtName = dialog.findViewById(R.id.name);
        TextInputEditText txtCalorias = dialog.findViewById(R.id.calorias);

        MaterialButton btnAgregar = dialog.findViewById(R.id.bt_agregar);

        btnAgregar.setOnClickListener(v -> {
            String nombreAlimento = txtName.getText().toString();
            String calorias = txtCalorias.getText().toString();
            addFood(adapter, lista, tipo, nombreAlimento, Float.parseFloat(calorias));

            dialog.dismiss();
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}