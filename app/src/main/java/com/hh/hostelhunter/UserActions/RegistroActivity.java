package com.hh.hostelhunter.UserActions;
import static com.hh.hostelhunter.Data.Datos_memoria.sendHttpPostRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;
import com.hh.hostelhunter.Data.Datos_memoria;
import com.hh.hostelhunter.Data.User;
import com.hh.hostelhunter.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistroActivity extends AppCompatActivity {
    private ImageButton backButton;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
    private ObjectMapper respuesta = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPhone=findViewById(R.id.textInputLayoutPhone);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone= findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextPassword2);
        buttonRegister = findViewById(R.id.buttonRegister);
        backButton = findViewById(R.id.buttonBack);
        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextUsername.getText().toString().isEmpty()) {
                    textInputLayoutUsername.setError("Un nombre es necesario");
                } else {
                    textInputLayoutUsername.setError(null);
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextEmail.getText().toString().isEmpty()) {
                    textInputLayoutEmail.setError("Un email es necesario");
                } else {
                    textInputLayoutEmail.setError(null);
                }
            }
        });

        editTextPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextEmail.getText().toString().isEmpty()) {
                    textInputLayoutPhone.setError("Un telefono es necesario");
                } else {
                    textInputLayoutPhone.setError(null);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextPassword.getText().toString().isEmpty()) {
                    textInputLayoutPassword.setError("Una contraseña es necesaria");
                } else {
                    textInputLayoutPassword.setError(null);
                }
            }
        });

        editTextConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextConfirmPassword.getText().toString().isEmpty()) {
                    textInputLayoutConfirmPassword.setError("Se tiene que repetir la contraseña");
                } else {
                    textInputLayoutConfirmPassword.setError(null);
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes realizar la lógica de registro, por ejemplo
                if (editTextUsername.getText().toString().isEmpty()) {
                    textInputLayoutUsername.setError("Un nombre es necesario");
                }
                if (editTextEmail.getText().toString().isEmpty()) {
                    textInputLayoutEmail.setError("Un email es necesario");
                }
                if (editTextPhone.getText().toString().isEmpty()  ) {
                    textInputLayoutPhone.setError("Un telefono es necesario");
                }
                if (editTextPassword.getText().toString().isEmpty()) {
                    textInputLayoutPassword.setError("Una contraseña es necesaria");
                }
                if (!(editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString()))) {
                    textInputLayoutConfirmPassword.setError("Contraseña no es igual");
                }
                // Si todos los campos están llenos, puedes proceder con el registro
                if (!editTextUsername.getText().toString().isEmpty() &&
                        !editTextPhone.getText().toString().isEmpty() &&
                        !editTextEmail.getText().toString().isEmpty() &&
                        !editTextPassword.getText().toString().isEmpty() &&
                        (editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString()))) {

                    Datos_memoria.usuario=new User(editTextUsername.getText().toString(),editTextPhone.getText().toString()
                    ,editTextEmail.getText().toString(),editTextPassword.getText().toString());
                    System.out.println(Datos_memoria.usuario.toString());
                    CompletableFuture<String> resultFuture = sendHttpPostRequest("https://hostelhunter.ieti.site/api/usuari/registrar",Datos_memoria.usuario.toString());
                    resultFuture.thenAccept(result -> {
                        System.out.println("Response: " + result);
                        JsonNode rootNode = null;
                        try {
                            // Analizar el JSON
                            rootNode = respuesta.readTree(result);

                            // Obtener el valor de "nombre" dentro de "data"
                            String nombreValue = rootNode.get("data").get("nombre").asText();
                            runOnUiThread(() -> Toast.makeText(RegistroActivity.this, "Registrado Correctamente", Toast.LENGTH_SHORT).show());

                            finish();
                        } catch (JsonProcessingException e) {
                            String Value = rootNode.get("status").asText();
                            if (Value.equals("ERROR")) {
                                // Mostrar un Toast si las credenciales son incorrectas
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(RegistroActivity.this, " el usuario ya existe", Toast.LENGTH_SHORT).show());
                            }

                        }
                        // Hacer algo con la respuesta
                    }).exceptionally(exception -> {
                        // Mostrar un Toast si hay un error en la conexión
                        runOnUiThread(() -> Toast.makeText(RegistroActivity.this, "Error conexion", Toast.LENGTH_SHORT).show());
                        return null;
                    });



                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

}
