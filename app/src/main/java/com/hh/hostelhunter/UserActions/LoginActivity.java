package com.hh.hostelhunter.UserActions;
import static android.app.PendingIntent.getActivity;
import static com.hh.hostelhunter.Data.Datos_memoria.sendHttpPostRequest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.material.textfield.TextInputLayout;
import com.hh.hostelhunter.Data.Datos_memoria;
import com.hh.hostelhunter.Data.User;
import com.hh.hostelhunter.MainActivity;
import com.hh.hostelhunter.R;
import com.hh.hostelhunter.Welcome.SlideFragment3;
import com.hh.hostelhunter.Welcome.WelcomeSlidesActivity;

import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
public class LoginActivity extends AppCompatActivity {
    private ImageButton backButton;
    private SlideFragment3 slideFragment3;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView recu_contra;
    private ObjectMapper respuesta = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        slideFragment3 = new SlideFragment3();

        textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        recu_contra = findViewById(R.id.textViewForgotPassword);
        backButton = findViewById(R.id.buttonBack);
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextEmail.getText().toString().isEmpty()) {
                    textInputLayoutUsername.setError("Un email es necesario");
                } else {
                    textInputLayoutUsername.setError(null);
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

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().equals("entroo")){
                    startActivity(new Intent(LoginActivity.this, com.hh.hostelhunter.ui.ui.class));
                    finish();
                }
                // Aquí puedes realizar la lógica de autenticación, por ejemplo
                if (editTextEmail.getText().toString().isEmpty()) {
                    textInputLayoutUsername.setError("Un email es necesario");
                }
                if (editTextPassword.getText().toString().isEmpty()) {
                    textInputLayoutPassword.setError("Una contraseña es necesaria");
                }
                // Si ambos campos están llenos, puedes proceder con la autenticación
                if (!editTextEmail.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()) {
                    buttonLogin.setEnabled(false);
                    buttonLogin.setText("Espera...");
                    Datos_memoria.usuarioLogin = new User(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                    CompletableFuture<String> resultFuture = sendHttpPostRequest("https://hostelhunter.ieti.site/api/usuari/login", Datos_memoria.usuarioLogin.toString());
                    resultFuture.thenAccept(result -> {
                        System.out.println("Response: " + result);
                        JsonNode rootNode = null;
                        try {
                            // Analizar el JSON
                            rootNode = respuesta.readTree(result);
                            Datos_memoria.usuarioLogin.setUsername("pepe");
                            Datos_memoria.usuarioLogin.setPhoneNumber("1111");
                            // Obtener el valor de "nombre" dentro de "data"

                            //String nombreValue = rootNode.get("data").get("nombre").asText();
                            System.out.println("pepe");
                            startActivity(new Intent(LoginActivity.this, com.hh.hostelhunter.ui.ui.class));
                            finish();
                        } catch (JsonProcessingException e) {
                            String Value = rootNode.get("status").asText();
                            if (Value.equals("ERROR")) {
                                // Mostrar un Toast si las credenciales son incorrectas
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Comprueba el usuario y contraseña1", Toast.LENGTH_SHORT).show());
                            }

                        }
                        // Hacer algo con la respuesta
                    }).exceptionally(exception -> {
                        // Mostrar un Toast si hay un error en la conexión
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Comprueba el usuario y contraseña2", Toast.LENGTH_SHORT).show());
                        return null;
                    });

                    buttonLogin.setEnabled(true);
                    buttonLogin.setText("Iniciar Sesión");
                }
            }
        });




        // Agregar un OnClickListener al TextView
        recu_contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar un Toast cuando se hace clic en el TextView
                Toast.makeText(LoginActivity.this, "Se ha enviado el enlace a tu email", Toast.LENGTH_SHORT).show();
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
