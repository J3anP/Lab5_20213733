package com.example.fitcal;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import android.os.Handler;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitcal.adapter.BreakfastAdapter;
import com.example.fitcal.adapter.CarouselAdapter;
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
import java.util.Random;

public class PlanActivity extends AppCompatActivity {
    private BreakfastAdapter desayunoAdapter, almuerzoAdapter, cenaAdapter;
    private EjercicioAdapter ejercicioAdapter;
    private List<Ejercicio> ejercicioList;
    private List<Meal> desayunoList, almuerzoList, cenaList;

    private float caloriasConsumidas = 0;
    private float caloriasObjetivo = 0.0f;

    String canal1 = "importanteDefault";
    private Handler handler = new Handler();
    private Runnable notificationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan);

        crearCanalesNotificacion();

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


        ViewPager2 viewPager = findViewById(R.id.viewPager);

        String[] names = {"Comida A", "Desayuno B", "Almuerzo A", "Almuerzo B", "Cena A"};
        String[] calories = {"Calorías: 100 kcal", "Calorías: 200 kcal", "Calorías: 200 kcal", "Calorías: 200 kcal", "Calorías: 200 kcal"};
        String[] descriptions = {
                "Esta es una breve descripción de la imagen.",
                "Esta es una breve descripción de la imagen.",
                "Esta es una breve descripción de la imagen.",
                "Esta es una breve descripción de la imagen.",
                "Esta es una breve descripción de la imagen."
        };
        int[] images = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4, R.drawable.img_5};

        CarouselAdapter carAdapter = new CarouselAdapter(names, calories, descriptions, images);
        viewPager.setAdapter(carAdapter);
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

        if(progreso>100){
            notificarCaloriaSuperada();
        }

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

            notificationRunnable = new Runnable() {
                @Override
                public void run() {
                    notificarMensajeAnimo();
                    handler.postDelayed(this, Integer.parseInt(strTiempo)*60*1000);
                }
            };
            handler.post(notificationRunnable);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    public void crearCanalesNotificacion() {

        NotificationChannel channel = new NotificationChannel(canal1,
                "Canal notificaciones default",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canal para notificaciones con prioridad default");
        channel.enableVibration(true);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermisos();
    }

    public void pedirPermisos() {
        // TIRAMISU = 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(PlanActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }

    public void notificarCaloriaSuperada(){

        //Crear notificación
        //Agregar información a la notificación que luego sea enviada a la actividad que se abre
        Intent intent = new Intent(this, PlanActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Fitcal")
                .setContentText("Ha superado las calorías que puede consumir por hoy")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();

        //Lanzar notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(281, notification);
        }

    }

    public void notificarMensajeAnimo(){
        String[] frases = {
                "¿Qué pasa soldado, ¿Acaso se va a rendir?",
                "La fuerza lo es todo. El poder lo perdona todo, incluso el pasado.",
                "Cuando conoces tu valor, nadie puede hacerte sentir menos."
        };

        Random random = new Random();
        int i = random.nextInt(frases.length);


        //Crear notificación
        //Agregar información a la notificación que luego sea enviada a la actividad que se abre
        Intent intent = new Intent(this, PlanActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Fitcal")
                .setContentText(frases[i])
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();

        //Lanzar notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(282, notification);
        }

    }

    //Notificacion personalizada
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(notificationRunnable);
    }


}