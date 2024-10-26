package com.example.fitcal;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitcal.adapter.BreakfastAdapter;
import com.example.fitcal.adapter.EjercicioAdapter;
import com.example.fitcal.bean.Ejercicio;
import com.example.fitcal.bean.Meal;
import com.example.fitcal.bean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity {
    private BreakfastAdapter desayunoAdapter, almuerzoAdapter, cenaAdapter;
    private EjercicioAdapter ejercicioAdapter;
    private List<Ejercicio> ejercicioList;
    private List<Meal> desayunoList, almuerzoList, cenaList;

    private float caloriasConsumidas = 0;
    private float caloriasObjetivo = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan);


        User user = (User) getIntent().getSerializableExtra("user");
        caloriasObjetivo = user.getGastoEnergia();
        Log.d("PlanActivity"," "+caloriasObjetivo);
        Log.d("Se llega a obtener el valor de tbm usuario ", " " + user.getGastoEnergia());
        /*
        String strCaloriasObjetivo = intent.getStringExtra("user");
        if (strCaloriasObjetivo != null && !strCaloriasObjetivo.isEmpty()) {
            caloriasObjetivo = Float.parseFloat(strCaloriasObjetivo);
            Log.d("Recibio calorias",strCaloriasObjetivo);
        } else {
            Log.d("PlanActivity", "El valor de calorías recibido es nulo :c");
        }

         */
        //Manejo del botton_navbar

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.principal);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.principal){
                    Intent intentRestaurant = new Intent(PlanActivity.this, PlanActivity.class);
                    startActivity(intentRestaurant);
                    return true;
                }else if(item.getItemId()==R.id.catalogo){
                    Intent intentPrincipal = new Intent(PlanActivity.this, CatalogoActivity.class);
                    startActivity(intentPrincipal);
                    return true;
                }else{
                    return false;
                }
            }
        });


        /*---------------------------*/

        // Inicializar las listas y los adaptadores
        desayunoList = new ArrayList<>();
        almuerzoList = new ArrayList<>();
        cenaList = new ArrayList<>();
        ejercicioList = new ArrayList<>();

        desayunoAdapter = new BreakfastAdapter(desayunoList,this);
        almuerzoAdapter = new BreakfastAdapter(almuerzoList,this);
        cenaAdapter = new BreakfastAdapter(cenaList,this);
        ejercicioAdapter = new EjercicioAdapter(ejercicioList,this);


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

        RecyclerView recyclerEjercicio = findViewById(R.id.recyclerEjercicio);
        recyclerEjercicio.setLayoutManager(new LinearLayoutManager(this));
        recyclerEjercicio.setAdapter(ejercicioAdapter);

        // Manejo de los botones
        Button btnAgregarAlimentoDesayuno = findViewById(R.id.btnAgregarAlimentoDesayuno);
        btnAgregarAlimentoDesayuno.setOnClickListener(v -> showBottomSheetDialog("desayuno",desayunoAdapter, desayunoList, caloriasObjetivo));

        Button btnAgregarAlimentoAlmuerzo = findViewById(R.id.btnAgregarAlimentoAlmuerzo);
        btnAgregarAlimentoAlmuerzo.setOnClickListener(v -> showBottomSheetDialog("almuerzo",almuerzoAdapter, almuerzoList, caloriasObjetivo));

        Button btnAgregarAlimentoCena = findViewById(R.id.btnAgregarAlimentoCena);
        btnAgregarAlimentoCena.setOnClickListener(v -> showBottomSheetDialog("cena",cenaAdapter, cenaList, caloriasObjetivo));

        Button btnAgregarEjercicio = findViewById(R.id.btnAgregarEjercicio);
        btnAgregarEjercicio.setOnClickListener(v -> showBottomSheetDialogExercise(ejercicioAdapter, ejercicioList, caloriasObjetivo));

        MaterialButton btnNotify = findViewById(R.id.bt_notification);
        btnNotify.setOnClickListener(v -> showBottomSheetDialogNotify());
    }



    private void addFood(BreakfastAdapter adapter, List<Meal> lista, String tipo, String nombre, float calorias, float caloriasObjetivo) {
        Meal newMeal = new Meal();
        newMeal.setName(nombre);
        newMeal.setTipo(tipo);
        newMeal.setCalorias(calorias);
        lista.add(newMeal);
        adapter.notifyItemInserted(lista.size() - 1);


        caloriasConsumidas += calorias;
        updateCaloriasProgreso(caloriasObjetivo);
    }

    private void addExercise(EjercicioAdapter adapter, List<Ejercicio> lista, String nombre, float calorias, float caloriasObjetivo) {
        Ejercicio newEjercicio = new Ejercicio();
        newEjercicio.setName(nombre);
        newEjercicio.setCaloria(calorias);
        lista.add(newEjercicio);
        adapter.notifyItemInserted(lista.size() - 1);


        caloriasConsumidas -= calorias;
        updateCaloriasProgreso(caloriasObjetivo);
    }

    private void showBottomSheetDialog(String tipo, BreakfastAdapter adapter, List<Meal> lista, float caloriasObjetivo) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_meal);

        TextInputEditText txtName = dialog.findViewById(R.id.name);
        TextInputEditText txtCalorias = dialog.findViewById(R.id.calorias);

        MaterialButton btnAgregar = dialog.findViewById(R.id.bt_agregar);

        btnAgregar.setOnClickListener(v -> {
            String nombreAlimento = txtName.getText().toString();
            String calorias = txtCalorias.getText().toString();
            addFood(adapter, lista, tipo, nombreAlimento, Float.parseFloat(calorias), caloriasObjetivo);

            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void updateCaloriasProgreso(float caloriasObjetivo) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView caloriesText = findViewById(R.id.calories_text);

        int progreso = (int) ((caloriasConsumidas / caloriasObjetivo) * 100);

        caloriesText.setText(caloriasConsumidas + " / " + caloriasObjetivo + " kcal");
        progressBar.setProgress(progreso);
    }

    private void showBottomSheetDialogExercise(EjercicioAdapter adapter, List<Ejercicio> lista, float caloriasObjetivo) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_exercise);

        TextInputEditText txtName = dialog.findViewById(R.id.name);
        TextInputEditText txtCalorias = dialog.findViewById(R.id.calorias);

        MaterialButton btnAgregar = dialog.findViewById(R.id.bt_add_exercise);

        btnAgregar.setOnClickListener(v -> {
            String nombreEjercicio = txtName.getText().toString();
            String calorias = txtCalorias.getText().toString();
            addExercise(adapter, lista, nombreEjercicio, Float.parseFloat(calorias), caloriasObjetivo);

            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showBottomSheetDialogNotify() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_notify);

        TextInputEditText txtTiempo = dialog.findViewById(R.id.calorias);

        MaterialButton btnAgregar = dialog.findViewById(R.id.bt_agregar);

        btnAgregar.setOnClickListener(v -> {
            String strTiempo = txtTiempo.getText().toString();

            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


}