package com.hh.hostelhunter.ui.notifications;

import static com.hh.hostelhunter.Data.Datos_memoria.sendHttpPostRequest;
import static com.hh.hostelhunter.Data.Datos_memoria.usuarioLogin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.material.textfield.TextInputLayout;
import com.hh.hostelhunter.Data.Datos_memoria;
import com.hh.hostelhunter.Data.User;

import com.hh.hostelhunter.R;
import com.hh.hostelhunter.Welcome.SlideFragment3;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

public class EditarPerfil extends AppCompatActivity {
    private SlideFragment3 slideFragment3;
    private ActivityResultLauncher<String> pickImageLauncher;
    private String base64Img="";
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


        Glide.with(this)
                .load(usuarioLogin.getUrlFoto())
                .centerInside()
                .into(imageButton);
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
                        System.out.println(bitmap.getByteCount());
                        // Convertir el bitmap a array de bytes
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                        imageButton.setImageBitmap(bitmap);
                        base64Img=bitmapToBase64(bitmap);
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

                    String[] partes = base64Img.split("\n");
                    // Unir las partes con un espacio como separador
                    base64Img = String.join("", partes);

                    partes = base64Img.split("\\\\");
                    // Unir las partes con un espacio como separador
                    base64Img = String.join("", partes);
                    JSONObject msgJSON=null;
                    try {
                        msgJSON = new JSONObject();
                        msgJSON.put("nombre", editTextName.getText());
                        msgJSON.put("telefono", editTextTlf.getText());
                        msgJSON.put("email", editTextEmail.getText());
                        msgJSON.put("id", Datos_memoria.usuarioLogin.getId());
                        msgJSON.put("base64",base64Img );
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    CompletableFuture<String> resultFuture = sendHttpPostRequest("https://hostelhunter.ieti.site/api/usuari/update", msgJSON.toString());
                    resultFuture.thenAccept(result -> {
                        System.out.println("Response: " + result);
                        JsonNode rootNode = null;
                        try {
                            // Analizar el JSON
                            rootNode = respuesta.readTree(result);
                            usuarioLogin.setUrlFoto(rootNode.get("data").get("url").asText());
                            usuarioLogin.setEmail(rootNode.get("data").get("gmail").asText());
                            usuarioLogin.setPhoneNumber(rootNode.get("data").get("telefono").asText());
                            usuarioLogin.setUsername(rootNode.get("data").get("nombre").asText());
                            runOnUiThread(() -> Toast.makeText(EditarPerfil.this, "Cambios guardados", Toast.LENGTH_SHORT).show());
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

    public static String[] dividirCadena(String base64, int longitudSegmento) {
        int totalPartes = (base64.length() + longitudSegmento - 1) / longitudSegmento; // Redondeo hacia arriba
        String[] partes = new String[totalPartes];
        for (int i = 0; i < totalPartes; i++) {
            partes[i] = base64.substring(i * longitudSegmento, Math.min((i + 1) * longitudSegmento, base64.length()));
        }
        return partes;
    }
    public static String bitmapToBase64(Bitmap bitmap) {
        // Verificar si el bitmap es nulo
        if (bitmap == null) {
            return null;
        }

        // Convertir el bitmap a un array de bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Codificar el array de bytes a una cadena Base64
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public String saveBitmapToInternalStorage(Bitmap bitmap) {
        Context context = getApplicationContext();
        String fileName = "imagen.jpg";

        try {
            // Obtener la ruta del directorio de archivos internos
            File directory = context.getFilesDir();
            File file = new File(directory, fileName);

            // Convertir el bitmap en un archivo JPEG y guardarlo en el almacenamiento interno
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            // Devolver la ruta del archivo guardado
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar cualquier error que pueda ocurrir durante la escritura del archivo
            return null;
        }
    }


    public Bitmap readBitmapFromInternalStorage(String filePath) {
        try {
            File internalDir = getFilesDir();
            // Abre el archivo en modo de lectura
            FileInputStream fis = new FileInputStream(internalDir+filePath);

            // Lee los bytes del archivo en un arreglo de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] bytes = baos.toByteArray();

            // Decodifica el arreglo de bytes en un objeto Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            // Cierra el flujo de entrada
            fis.close();
            baos.close();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            // Maneja cualquier error que pueda ocurrir durante la lectura del archivo
            return null;
        }
    }




}
