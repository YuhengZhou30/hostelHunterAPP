package com.hh.hostelhunter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.hostelhunter.Welcome.WelcomeSlidesActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String jsonFileName = "datos.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) {
            controller.setAppearanceLightStatusBars(isLightColor(Color.WHITE));
        }
        // Leer el JSON desde el archivo y convertirlo en un objeto Java
        DataObject data = readDataFromJson();
        if (data != null) {
            System.out.println("First Time: " + data.isFirstTime());
        }

        // Abrir la actividad de bienvenida al iniciar la aplicación
        startActivity(new Intent(MainActivity.this, WelcomeSlidesActivity.class));

        // Finalizar esta actividad para que no se pueda volver atrás
        finish();

        // Guardar datos en un archivo JSON
        saveDataToJson();
    }

    // Comprueba si un color es claro o oscuro
    private boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    private DataObject readDataFromJson() {
        try {
            FileInputStream fileInputStream = openFileInput(jsonFileName);
            ObjectMapper objectMapper = new ObjectMapper();
            DataObject data = objectMapper.readValue(fileInputStream, DataObject.class);
            fileInputStream.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveDataToJson() {
        // Crear un objeto JSON
        DataObject data = new DataObject();
        data.setFirstTime(false);

        // Convertir el objeto Java en una cadena JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(data);

            // Guardar la cadena JSON en un archivo
            FileOutputStream fileOutputStream = openFileOutput(jsonFileName, MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();

            System.out.println("Datos guardados en " + jsonFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DataObject {
    private boolean firstTime;

    // Constructor por defecto
    public DataObject() {
    }

    // Constructor con parámetros
    public DataObject(boolean firstTime) {
        this.firstTime = firstTime;
    }

    // Getter para firstTime
    public boolean isFirstTime() {
        return firstTime;
    }

    // Setter para firstTime
    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }
}
