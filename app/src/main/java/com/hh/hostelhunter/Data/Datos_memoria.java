package com.hh.hostelhunter.Data;

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

public class Datos_memoria{
    public static User usuario;
    public static User usuarioLogin;


    public static CompletableFuture<String> sendHttpPostRequest(final String urlStr, final String postData) {
        CompletableFuture<String> futureResult = new CompletableFuture<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Crea una conexión HTTP
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Escribe los datos en el cuerpo de la solicitud
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    os.close();

                    // Obtiene la respuesta
                    int responseCode = connection.getResponseCode();
                    System.out.println("Response Code: " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_BAD_REQUEST || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                        // Procesa la respuesta si es necesario
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        String result = sb.toString();
                        futureResult.complete(result);
                    } else {
                        futureResult.completeExceptionally(new Exception("Error: " + responseCode));
                    }

                    // Cierra la conexión
                    connection.disconnect();
                } catch (IOException e) {
                    futureResult.completeExceptionally(e);
                }
            }
        });

        executor.shutdown(); // Cerramos el executor después de su uso

        return futureResult;
    }

    public static CompletableFuture<String> sendHttpGetRequest(final String urlStr) {
        CompletableFuture<String> futureResult = new CompletableFuture<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Crea una conexión HTTP
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Obtiene la respuesta
                    int responseCode = connection.getResponseCode();
                    System.out.println("Response Code: " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_BAD_REQUEST || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                        // Procesa la respuesta si es necesario
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        String result = sb.toString();
                        futureResult.complete(result);
                    } else {
                        futureResult.completeExceptionally(new Exception("Error: " + responseCode));
                    }

                    // Cierra la conexión
                    connection.disconnect();
                } catch (IOException e) {
                    futureResult.completeExceptionally(e);
                }
            }
        });

        executor.shutdown(); // Cerramos el executor después de su uso

        return futureResult;
    }


}