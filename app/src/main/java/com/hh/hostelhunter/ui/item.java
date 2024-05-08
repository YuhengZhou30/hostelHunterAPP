package com.hh.hostelhunter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hh.hostelhunter.Data.Propiedad;
import com.hh.hostelhunter.R;

public class item extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        // Recuperar el objeto de MiClase del Intent


        // Obtener referencias a los elementos de la vista
        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewPrice = findViewById(R.id.textViewPrice);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        Intent intent = getIntent();
        if (intent != null) {
            Propiedad propiedad = (Propiedad) intent.getSerializableExtra("propiedad");
            Glide.with(this)
                    .load(propiedad.getUrlFoto())
                    .centerInside()
                    .into(imageView);
            textViewPrice.setText(propiedad.getPrecioPorNoche()+"");
            textViewTitle.setText(propiedad.getNombre());
            textViewDescription.setText(propiedad.getDescripcion());
        }

        // Configurar datos de ejemplo (reemplaza con tus datos reales)
        /*
        imageView.setImageResource(R.drawable.pensar3);
        textViewPrice.setText("$99.99");
        textViewTitle.setText("Título del Producto");
        textViewDescription.setText("Descripción detallada del producto...");*/
    }
}
