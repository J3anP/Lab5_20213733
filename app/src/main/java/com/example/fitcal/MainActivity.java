package com.example.fitcal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitcal.bean.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private Spinner activitySpinner, goalSpinner;
    TextInputEditText txtPeso, txtAltura, txtEdad;
    RadioButton rdMale,rdFemale;
    private RadioGroup genderGroup;
    String genero = " ";
    String naf; //nivel de actividade física
    String op; //objetivo de peso
    float tbm = 0.0f;
    float newTbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa los campos de entrada
        txtPeso = findViewById(R.id.input_weight);
        txtAltura = findViewById(R.id.input_height);
        txtEdad = findViewById(R.id.input_age);

        activitySpinner = findViewById(R.id.activity_spinner);
        goalSpinner = findViewById(R.id.goal_spinner);
        genderGroup = findViewById(R.id.gender_group);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.activity_level_options,
                android.R.layout.simple_spinner_item
        );
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String activity = parent.getItemAtPosition(position).toString();

                if (activity.equals("Poco o ningun ejercicio")) {
                    naf = "1";
                } else if (activity.equals("Ejercicio ligero")) {
                    naf = "2";
                } else if (activity.equals("Ejercicio moderado")) {
                    naf = "3";
                } else if (activity.equals("Ejercicio fuerte")) {
                    naf = "4";
                } else if (activity.equals("Ejercicio muy fuerte")) {
                    naf = "5";
                } else {
                    naf = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                naf = "";
            }
        });

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.goal_options,
                android.R.layout.simple_spinner_item
        );
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapter);

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String goal = parent.getItemAtPosition(position).toString();

                if (goal.equals("Bajar de peso")) {
                    op = "bp";
                } else if (goal.equals("Mantener peso")) {
                    op = "mp";
                } else if (goal.equals("Subir de peso")) {
                    op = "sp";
                } else {
                    op = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                op = "";
            }
        });

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_male) {
                    genero = "M";
                } else if (checkedId == R.id.radio_female) {
                    genero = "F";
                }
            }
        });

        MaterialButton btNext = findViewById(R.id.bt_next);
        btNext.setOnClickListener(v -> {
            String pesoText = txtPeso.getText() != null ? txtPeso.getText().toString().trim() : "";
            String alturaText = txtAltura.getText() != null ? txtAltura.getText().toString().trim() : "";
            String edadText = txtEdad.getText() != null ? txtEdad.getText().toString().trim() : "";

            float peso = 0.0f;
            int altura = 0;
            int edad = 0;

            boolean validInput = true;

            try {
                if (!pesoText.isEmpty()) {
                    peso = Float.parseFloat(pesoText);
                } else {
                    txtPeso.setError("Por favor ingrese su peso");
                    validInput = false;
                }

                if (!alturaText.isEmpty()) {
                    altura = Integer.parseInt(alturaText);
                } else {
                    txtAltura.setError("Por favor ingrese su altura");
                    validInput = false;
                }

                if (!edadText.isEmpty()) {
                    edad = Integer.parseInt(edadText);
                } else {
                    txtEdad.setError("Por favor ingrese su edad");
                    validInput = false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (!validInput) return;

            // Calculo de TMB
            float ctbm;
            if (genero.equals("M")) {
                ctbm = (10 * peso + 6.25f * altura - 5.0f * edad + 5.0f);
            } else {
                ctbm = (10 * peso + 6.25f * altura - 5.0f * edad - 161.0f);
            }

            if (naf.equals("1")) {
                tbm = ctbm * 1.2f;
            } else if (naf.equals("2")) {
                tbm = ctbm * 1.375f;
            } else if (naf.equals("3")) {
                tbm = ctbm * 1.55f;
            } else if (naf.equals("4")) {
                tbm = ctbm * 1.725f;
            } else if (naf.equals("5")) {
                tbm = ctbm * 1.9f;
            } else {
                tbm = ctbm * 1.2f;
            }
            Log.d("Valor de tbm"," "+tbm);

            Intent intent = new Intent(MainActivity.this, PlanActivity.class);
            // Ajuste final basado en el objetivo de peso (op)
            User user = new User();

            if (op.equals("bp")) {
                user.setEdad(edad);
                user.setAltura(altura);
                user.setPeso(peso);
                user.setGenero(genero);
                user.setTmb(tbm);
                user.setActividadFisica(naf);
                user.setGastoEnergia(tbm-300.0f);
                user.setObjetivoPeso(op);

                intent.putExtra("user", user);
            } else if (op.equals("mp")) {
                user.setEdad(edad);
                user.setAltura(altura);
                user.setPeso(peso);
                user.setGenero(genero);
                user.setTmb(tbm);
                user.setActividadFisica(naf);
                user.setGastoEnergia(tbm);
                user.setObjetivoPeso(op);
                intent.putExtra("user", user);

            } else if (op.equals("sp")) {
                user.setEdad(edad);
                user.setAltura(altura);
                user.setPeso(peso);
                user.setGenero(genero);
                user.setTmb(tbm);
                user.setActividadFisica(naf);
                user.setGastoEnergia(tbm+500.0f);
                user.setObjetivoPeso(op);

                intent.putExtra("user", user);
            } else {
                user.setEdad(edad);
                user.setAltura(altura);
                user.setPeso(peso);
                user.setGenero(genero);
                user.setTmb(tbm);
                user.setActividadFisica(naf);
                user.setGastoEnergia(tbm + 500.0f);
                user.setObjetivoPeso(op);

                intent.putExtra("user", user);
            }

            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}