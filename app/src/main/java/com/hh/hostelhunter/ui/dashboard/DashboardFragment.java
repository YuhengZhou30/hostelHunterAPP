package com.hh.hostelhunter.ui.dashboard;

import static com.hh.hostelhunter.Data.Datos_memoria.sendHttpGetRequest;
import static com.hh.hostelhunter.Data.Datos_memoria.sendHttpPostRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.hostelhunter.Data.Datos_memoria;
import com.hh.hostelhunter.Data.Propiedad;
import com.hh.hostelhunter.R;
import com.hh.hostelhunter.UserActions.LoginActivity;
import com.hh.hostelhunter.databinding.FragmentDashboardBinding;
import com.hh.hostelhunter.ui.item;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Recomendados
public class DashboardFragment extends Fragment {
    private boolean respuestaVacia=false;
    private boolean primero=true;
    private RecyclerView recyclerView;
    private int Pagina_actual=2;

    private GridAdapter adapter;
    ArrayList<Propiedad> propiedades = new ArrayList<>();
     List<ListItem> items = new ArrayList<>(); // Define items list globally
    ObjectMapper mapper = new ObjectMapper();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        SearchView searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Change the number of columns as needed

        adapter = new GridAdapter(items);
        if (primero){
            primero=false;
            generateItemList2(1);
            adapter.notifyDataSetChanged();
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = ((GridLayoutManager) recyclerView.getLayoutManager()).getChildCount();
                int totalItemCount = ((GridLayoutManager) recyclerView.getLayoutManager()).getItemCount();
                int pastVisibleItems = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    // call to get more properties

                    if (respuestaVacia==false){
                        generateItemList2(Pagina_actual);
                        Pagina_actual+=1;
                        adapter.notifyDataSetChanged();
                    }





                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Here you can handle the action when the search query is submitted
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        adapter.setOnItemClickListener(new GridAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle click on RecyclerView item here
                // You can get the selected item from the items list using the position

                Intent intent = new Intent(requireActivity(), item.class);
                Propiedad propiedad = propiedades.get(position % propiedades.size());
                intent.putExtra("propiedad", propiedad);
                startActivity(intent);
            }
        });
        //generateItemList();
        //System.out.println(Pagina_actual);




        return view;
    }

    private void generateItemList2(int Page) {
        System.out.println("pedir");
        CompletableFuture<String>  res=sendHttpGetRequest("https://hostelhunter.ieti.site/api/informacion/android?page="+Page+"&size=6");
        res.thenAccept(result -> {
            System.out.println("Response: " + result);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(result);
                JsonNode dataNode = rootNode.path("data");
                if (dataNode.isEmpty()){
                    System.out.println(dataNode.toString());
                    respuestaVacia=true;
                }
                for (JsonNode node : dataNode) {
                    Propiedad propiedad = new Propiedad();
                    propiedad.setPropietario(node.path("nombrePropietario").asText());
                    propiedad.setNombre(node.path("nombre").asText());
                    propiedad.setDescripcion(node.path("descripcion").asText());
                    propiedad.setDireccion(node.path("direccion").asText());
                    propiedad.setID(node.path("alojamientoID").asInt());
                    propiedad.setReglas(node.path("reglas").asText());
                    propiedad.setUrlFoto(jsonNodeToList(node.path("urlFoto")));
                    propiedad.setCapacidad(node.path("capacidad").asInt());
                    propiedad.setLikes(node.path("likes").asInt());
                    propiedad.setPrecioPorNoche(node.path("precioPorNoche").asDouble());
                    //System.out.println(propiedad.toString());
                    items.add(new ListItem(propiedad.getUrlFoto(), propiedad.getNombre(), propiedad.getPrecioPorNoche() + ""));
                    propiedades.add(propiedad);
                }
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
    public static ArrayList<String> jsonNodeToList(JsonNode jsonNode) {
        ArrayList<String> list = new ArrayList<>();

        // Verificar si el JsonNode es una matriz
        if (jsonNode.isArray()) {
            // Iterar sobre los elementos de la matriz
            for (JsonNode element : jsonNode) {
                // Agregar cada elemento a la lista como una cadena
                list.add(element.asText());
            }
        }
        return list;
    }

}
