package com.hh.hostelhunter.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hh.hostelhunter.Data.Propiedad;
import com.hh.hostelhunter.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class item extends AppCompatActivity {

    Button Reservar;
    Calendar calendar;
    ArrayList<Date> disabledDates = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        // Obtener referencias a los elementos de la vista
        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewPrice = findViewById(R.id.textViewPrice);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        Reservar = findViewById(R.id.button);


        Intent intent = getIntent();
        if (intent != null) {
            Propiedad propiedad = (Propiedad) intent.getSerializableExtra("propiedad");
            Glide.with(this)
                    .load(propiedad.getUrlFoto())
                    .centerInside()
                    .into(imageView);
            textViewPrice.setText(propiedad.getPrecioPorNoche() + "");
            textViewTitle.setText(propiedad.getNombre());
            textViewDescription.setText(propiedad.getDescripcion());
        }

        // Lista de fechas desactivadas (en rojo)
        String[] fechasDesactivadas = {"20/05/2024", "15/05/2024"};

        try {
            for (String fecha : fechasDesactivadas) {
                disabledDates.add(sdf.parse(fecha));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Configurar el listener para el botón Reservar
        // Configurar el listener para el botón Reservar
        Reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                calendar = Calendar.getInstance();

                // Crear un DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(item.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Obtener la fecha seleccionada
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Validar si la fecha seleccionada está desactivada

                            // La fecha no está desactivada, puedes proceder con la reserva
                            // Aquí puedes implementar la lógica para reservar la propiedad
                            // Por ejemplo, puedes guardar la fecha seleccionada y luego pasarla a otra actividad para completar la reserva
                            String selectedDate = sdf.format(calendar.getTime());
                            Toast.makeText(item.this, "Fecha seleccionada: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                // Mostrar el DatePickerDialog
                datePickerDialog.show();
            }
        });


    }



}
