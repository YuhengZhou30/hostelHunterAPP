package com.hh.hostelhunter.ui.notifications;

import static com.hh.hostelhunter.Data.Datos_memoria.sendHttpPostRequest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.material.textfield.TextInputLayout;
import com.hh.hostelhunter.Data.Datos_memoria;
import com.hh.hostelhunter.Data.User;

import com.hh.hostelhunter.R;
import com.hh.hostelhunter.Welcome.SlideFragment3;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
public class EditarPerfil extends AppCompatActivity {
    private SlideFragment3 slideFragment3;
    private ActivityResultLauncher<String> pickImageLauncher;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutTlf;
    private ImageButton imageButton;
    private EditText editTextEmail;
    private EditText editTextTlf;
    private EditText editTextName;
    private Button buttonSave;

    private ObjectMapper respuesta = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editperfil);
        slideFragment3 = new SlideFragment3();
        imageButton= findViewById(R.id.imageButton);
        textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutTlf = findViewById(R.id.textInputLayoutTelefono);
        editTextEmail = findViewById(R.id.editTextCorreo);
        editTextTlf = findViewById(R.id.editTextTelefono);
        editTextName = findViewById(R.id.editTextName);
        System.out.println(Datos_memoria.usuarioLogin.toString());
        editTextName.setText( Datos_memoria.usuarioLogin.getUsername());
        editTextEmail.setText( Datos_memoria.usuarioLogin.getEmail());
        editTextTlf.setText( Datos_memoria.usuarioLogin.getPhoneNumber());
        buttonSave = findViewById(R.id.buttonSave);
        imageButton.setImageResource(R.drawable.pensar1);
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

        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextName.getText().toString().isEmpty()) {
                    textInputLayoutUsername.setError("Un nombre es necesario");
                } else {
                    textInputLayoutUsername.setError(null);
                }
            }
        });

        editTextTlf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editTextTlf.getText().toString().isEmpty()) {
                    textInputLayoutTlf.setError("Un telefono es necesario");
                } else {
                    textInputLayoutTlf.setError(null);
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageLauncher.launch("image/*");
            }
        });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                        imageButton.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes realizar la lógica de autenticación, por ejemplo
                if (editTextEmail.getText().toString().isEmpty()) {
                    textInputLayoutUsername.setError("Un email es necesario");
                }
                if (editTextName.getText().toString().isEmpty()) {
                    textInputLayoutUsername.setError("Un nombre es necesario");
                }
                if (editTextTlf.getText().toString().isEmpty()) {
                    textInputLayoutTlf.setError("Un email es necesario");
                }
                // Si ambos campos están llenos, puedes proceder con la autenticación
                if (!editTextEmail.getText().toString().isEmpty() && !editTextName.getText().toString().isEmpty() && !editTextTlf.getText().toString().isEmpty()) {
                    buttonSave.setEnabled(false);
                    buttonSave.setText("Espera...");
                    System.out.println(Datos_memoria.usuarioLogin.toString());
                    CompletableFuture<String> resultFuture = sendHttpPostRequest("https://hostelhunter.ieti.site/api/usuari/login", Datos_memoria.usuarioLogin.toString());
                    resultFuture.thenAccept(result -> {
                        System.out.println("Response: " + result);
                        JsonNode rootNode = null;
                        try {
                            // Analizar el JSON
                            rootNode = respuesta.readTree(result);

                            // Obtener el valor de "nombre" dentro de "data"

                            //String nombreValue = rootNode.get("data").get("nombre").asText();
                            System.out.println("pepe");
                            startActivity(new Intent(EditarPerfil.this, com.hh.hostelhunter.ui.ui.class));
                            finish();
                        } catch (JsonProcessingException e) {
                            String Value = rootNode.get("status").asText();
                            if (Value.equals("ERROR")) {
                                // Mostrar un Toast si las credenciales son incorrectas
                                runOnUiThread(() -> Toast.makeText(EditarPerfil.this, "Comprueba el usuario y contraseña1", Toast.LENGTH_SHORT).show());
                            }

                        }
                        // Hacer algo con la respuesta
                    }).exceptionally(exception -> {
                        // Mostrar un Toast si hay un error en la conexión
                        runOnUiThread(() -> Toast.makeText(EditarPerfil.this, "Comprueba el usuario y contraseña2", Toast.LENGTH_SHORT).show());
                        return null;
                    });

                    buttonSave.setEnabled(true);
                    buttonSave.setText("Guardar cambios");
                }
            }
        });
    }

}
