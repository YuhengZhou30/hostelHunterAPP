package com.hh.hostelhunter.ui;

import static com.hh.hostelhunter.Data.Datos_memoria.sendHttpPostRequest;
import static com.hh.hostelhunter.Data.Datos_memoria.usuarioLogin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.hostelhunter.Data.Datos_memoria;
import com.hh.hostelhunter.Data.Propiedad;
import com.hh.hostelhunter.R;
import com.hh.hostelhunter.ui.notifications.EditarPerfil;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class item extends AppCompatActivity {
    ArrayList<String> fechas=new ArrayList<>();
    double PrecioFinal=0;
    Propiedad propiedad;
    ArrayList<Date[]> fechasOcupadas=new ArrayList<>();
    LocalDate startDate;
    LocalDate endDate;
    boolean likeAcv;

    TextView precioFinal;
    Button Reservar,fechaInicio,fechaFin,like;
    ArrayList<Date> disabledDates = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private ObjectMapper respuesta = new ObjectMapper();

    JsonNode diasOcupados=null;

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    ObjectMapper mapper = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Obtener referencias a los elementos de la vista
        TextView textViewPrice = findViewById(R.id.textViewPrice);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        Reservar = findViewById(R.id.button);
        fechas.add("");
        fechas.add("");
        fechaInicio=findViewById(R.id.fechaInicio);
        fechaFin=findViewById(R.id.fechaFin);
        precioFinal=findViewById(R.id.precio);
        like=findViewById(R.id.like);
        Reservar.setEnabled(false);
        Reservar.setVisibility(View.INVISIBLE);
        fechaFin.setEnabled(false);
        fechaFin.setVisibility(View.INVISIBLE);
        Reservar.setBackgroundColor(Color.parseColor("#FF000000"));
        fechaInicio.setBackgroundColor(Color.parseColor("#FF000000"));
        fechaFin.setBackgroundColor(Color.parseColor("#FF000000"));

        Intent intent = getIntent();
        if (intent != null) {
            propiedad = (Propiedad) intent.getSerializableExtra("propiedad");

            CompletableFuture<String> resultFuture = sendHttpPostRequest("https://hostelhunter.ieti.site/api/alojamiento/android","{\"alojamientoID\":\""+propiedad.getID()+"\",\"usuarioID\":\""+usuarioLogin.getId()+"\"}");
            resultFuture.thenAccept(result -> {

                try {
                    JsonNode rootNode = mapper.readTree(result);
                    System.out.println(rootNode.get("data").get("like"));
                    if (rootNode.get("data").get("like").asText().equals("Si")){
                        likeAcv=true;
                        like.setBackgroundColor(Color.parseColor("#FF69B4"));
                    }else{
                        likeAcv=false;
                        like.setBackgroundColor(Color.parseColor("#FF000000"));
                    }

                } catch (JsonProcessingException e) {
                    String Value = diasOcupados.get("status").asText();
                    if (Value.equals("ERROR")) {
                        // Mostrar un Toast si las credenciales son incorrectas
                        Toast.makeText(item.this, "malall", Toast.LENGTH_SHORT).show();
                    }
                }
                // Hacer algo con la respuesta
            }).exceptionally(exception -> {
                // Mostrar un Toast si hay un error en la conexión
                Toast.makeText(item.this, "Servidor malo", Toast.LENGTH_SHORT).show();
                return null;
            });

            if (propiedad.getLikes().equals("Si")){
                like.setBackgroundColor(Color.parseColor("#FF69B4"));
            }else {
                like.setBackgroundColor(Color.parseColor("#000000"));
            }
            ArrayList<String> propertyImages = new ArrayList<>();
            for (String i : propiedad.getUrlFoto()){
                propertyImages.add(i);
            }
            textViewPrice.setText(propiedad.getPrecioPorNoche() + "€");
            textViewTitle.setText(propiedad.getNombre());
            textViewDescription.setText(propiedad.getDescripcion());
            adapter = new PropertyAdapter(propertyImages);
            like.setText("<3");
            recyclerView.setAdapter(adapter);
        }

        CompletableFuture<String> resultFuture = sendHttpPostRequest("https://hostelhunter.ieti.site/api/reservas/info","{\"alojamientoID\":\""+propiedad.getID()+"\"}");
        resultFuture.thenAccept(result -> {
            //System.out.println("dias ocupados: " + result);
            try {
                // Analizar el JSON
                diasOcupados = respuesta.readTree(result).get("occupiedIntervals");
                for (JsonNode jsonNode : diasOcupados) {
                    Date date1 = sdf.parse("01/01/2022");
                    // Obtener los valores "start" y "end" de cada objeto
                    String start = jsonNode.get("start").asText();
                    String end = jsonNode.get("end").asText();

                    Date[] fecha=new Date[2];
                    fecha[0]=sdf.parse(start);
                    fecha[1]=sdf.parse(end);
                    fechasOcupadas.add(fecha);
                }


            } catch (JsonProcessingException e) {
                String Value = diasOcupados.get("status").asText();
                if (Value.equals("ERROR")) {
                    // Mostrar un Toast si las credenciales son incorrectas
                    Toast.makeText(item.this, "malall", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            // Hacer algo con la respuesta
        }).exceptionally(exception -> {
            // Mostrar un Toast si hay un error en la conexión
            Toast.makeText(item.this, "Servidor malo", Toast.LENGTH_SHORT).show();
            return null;
        });

        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(item.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Obtener la fecha seleccionada
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(Calendar.YEAR, year);
                        selectedCalendar.set(Calendar.MONTH, monthOfYear);
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Verificar si la fecha seleccionada es anterior a la fecha actual
                        if (selectedCalendar.before(Calendar.getInstance())) {
                            Toast.makeText(item.this, "No se puede seleccionar una fecha anterior a hoy", Toast.LENGTH_SHORT).show();
                            return; // Salir del método sin hacer nada si la fecha es anterior a hoy
                        }


                        // Convertir la fecha seleccionada a un formato legible
                        String selectedDate = sdf.format(selectedCalendar.getTime());
                        boolean bien=false;
                        try {

                            bien=!fechaEstaOcupada(fechasOcupadas,sdf.parse(selectedDate));
                            System.out.println(bien);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (bien){

                            fechas.set(0,selectedDate);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            startDate = LocalDate.parse(fechas.get(0), formatter);
                            if (fechas.get(1).length()>3){
                                int comparacion=-1;
                                try {
                                     comparacion = compararFechas(sdf.parse(fechas.get(0)), sdf.parse(fechas.get(1)));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if(comparacion>0){
                                    Toast.makeText(item.this, "La fecha inicio no puede ser más posterior que fecha fin", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(item.this, "Fecha inicio: " + selectedDate, Toast.LENGTH_SHORT).show();
                                    PrecioFinal = (ChronoUnit.DAYS.between(startDate, endDate) + 1)*propiedad.getPrecioPorNoche();
                                    precioFinal.setText(PrecioFinal+"€");
                                    fechaFin.setEnabled(true);
                                    fechaFin.setVisibility(View.VISIBLE);
                                    fechaInicio.setText(selectedDate);
                                }

                            }else {
                                Toast.makeText(item.this, "Fecha inicio: " + selectedDate, Toast.LENGTH_SHORT).show();
                                fechaFin.setEnabled(true);
                                fechaFin.setVisibility(View.VISIBLE);
                                fechaInicio.setText(selectedDate);
                            }

                        }else {
                            Toast.makeText(item.this, "La fecha esta ocupada", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                // Establecer el límite mínimo de fecha como la fecha actual
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                // Mostrar el DatePickerDialog
                datePickerDialog.show();
            }
        });

        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                Calendar calendar = Calendar.getInstance();
                Calendar fechaMinimo = Calendar.getInstance();

                try {
                    // Parsear el string a un objeto Date
                    Date date = sdf.parse(fechas.get(0));

                    // Establecer la fecha en el objeto Calendar
                    fechaMinimo.setTime(date);
                    System.out.println(date);
                    // Ahora, calendar contiene la fecha "13/05/2024" como un objeto Calendar
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(item.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Obtener la fecha seleccionada
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(Calendar.YEAR, year);
                        selectedCalendar.set(Calendar.MONTH, monthOfYear);
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        // Convertir la fecha seleccionada a un formato legible
                        String selectedDate = sdf.format(selectedCalendar.getTime());
                        boolean bien=false;
                        try {

                            bien=!fechaEstaOcupada(fechasOcupadas,sdf.parse(selectedDate));
                            System.out.println(bien);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (bien){
                            Toast.makeText(item.this, "Fecha Fin: " + selectedDate, Toast.LENGTH_SHORT).show();


                            fechas.set(1,selectedDate);
                            fechaFin.setText(selectedDate);
                            Reservar.setEnabled(true);
                            Reservar.setVisibility(View.VISIBLE);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            endDate= LocalDate.parse(fechas.get(1), formatter);



                            // Calculate the difference inclusively
                            PrecioFinal = (ChronoUnit.DAYS.between(startDate, endDate) + 1)*propiedad.getPrecioPorNoche();
                            precioFinal.setText(PrecioFinal+"€");
                        }else {
                            Toast.makeText(item.this, "La fecha esta ocupada", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                // Establecer el límite mínimo de fecha como la fecha actual
                datePickerDialog.getDatePicker().setMinDate(fechaMinimo.getTimeInMillis());

                // Mostrar el DatePickerDialog
                datePickerDialog.show();
            }
        });

        Reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    JSONObject msgJSON=null;
                    msgJSON = new JSONObject();
                    msgJSON.put("alojamientoID", propiedad.getID()+"");
                    msgJSON.put("fechainicio", fechas.get(0));
                    msgJSON.put("fechafinal", fechas.get(1));
                    msgJSON.put("total", PrecioFinal+"");
                    msgJSON.put("usuarioID",Datos_memoria.usuarioLogin.getId()+"" );

                    String[] partes = msgJSON.toString().split("\\\\");
                    // Unir las partes con un espacio como separador
                    String enviar = String.join("", partes);
                    System.out.println(enviar);


                    CompletableFuture<String> resultFuture = sendHttpPostRequest("https://hostelhunter.ieti.site/api/reservas/anadir",enviar);
                    Toast.makeText(item.this, "Reservado Correctamente!!", Toast.LENGTH_SHORT).show();
                    resultFuture.thenAccept(result -> {
                        System.out.println("Response: " + result);
                        JsonNode rootNode = null;
                        try {
                            // Analizar el JSON
                            rootNode = respuesta.readTree(result);
                        } catch (JsonProcessingException e) {
                            String Value = rootNode.get("status").asText();
                            if (Value.equals("ERROR")) {
                                // Mostrar un Toast si las credenciales son incorrectas
                                Toast.makeText(item.this, "malall", Toast.LENGTH_SHORT).show();
                            }

                        }
                        // Hacer algo con la respuesta
                    }).exceptionally(exception -> {
                        // Mostrar un Toast si hay un error en la conexión
                        Toast.makeText(item.this, "Servidor malo", Toast.LENGTH_SHORT).show();
                        return null;
                    });
                }catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!likeAcv){

                    sendHttpPostRequest("https://hostelhunter.ieti.site/api/anadir/like","{\"alojamientoID\":\""+propiedad.getID()+"\",\"usuarioID\":\""+usuarioLogin.getId()+"\"}");
                    like.setBackgroundColor(Color.parseColor("#FF69B4"));
                    likeAcv=true;
                }else if (likeAcv){

                    sendHttpPostRequest("https://hostelhunter.ieti.site/api/quitar/like","{\"alojamientoID\":\""+propiedad.getID()+"\",\"usuarioID\":\""+usuarioLogin.getId()+"\"}");
                    like.setBackgroundColor(Color.parseColor("#000000"));
                    likeAcv=false;
                }


            }
        });


    }

    public static boolean fechaEstaOcupada(ArrayList<Date[]> fechasOcupadas, Date fecha) {
        for (Date[] intervalo : fechasOcupadas) {
            Date inicio = intervalo[0];
            Date fin = intervalo[1];
            // Verificar si la fecha está dentro del intervalo
            if (!fecha.before(inicio) && !fecha.after(fin)) {
                // La fecha está dentro del intervalo, por lo que está ocupada
                return true;
            }
        }
        // La fecha no está dentro de ningún intervalo, por lo que no está ocupada
        return false;
    }

    public static int compararFechas(Date date1, Date date2) {
        // Utiliza el método compareTo() de Date para comparar las fechas
        return date1.compareTo(date2);
    }

}


class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private ArrayList<String> propertyImages;

    public PropertyAdapter(ArrayList<String> propertyImages) {
        this.propertyImages = propertyImages;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        String imageUrl = propertyImages.get(position);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return propertyImages.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

