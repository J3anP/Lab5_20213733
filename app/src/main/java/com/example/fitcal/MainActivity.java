package com.example.fitcal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Spinner activitySpinner, goalSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        activitySpinner = findViewById(R.id.activity_spinner);
        goalSpinner = findViewById(R.id.goal_spinner);

        // Adaptador para nivel de actividad
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.activity_level_options,
                android.R.layout.simple_spinner_item
        );
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        // Adaptador para objetivo de peso
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.goal_options,
                android.R.layout.simple_spinner_item
        );
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapter);
    }
}