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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
//Recomendados
public class DashboardFragment extends Fragment {
    private boolean respuestaVacia=false;
    private boolean primero=true;
    private RecyclerView recyclerView;
    private int Pagina_actual=2;

    private GridAdapter adapter;
    ArrayList<Propiedad> propiedades = new ArrayList<>();
     List<ListItem> items = new ArrayList<>(); // Define items list globally
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
            //System.out.println("Response: " + result);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(result);
                JsonNode dataNode = rootNode.path("data");
                for (JsonNode node : dataNode) {
                    //System.out.println(node);
                    Propiedad propiedad = new Propiedad();
                    propiedad.setPropietario(node.path("nombrePropietario").asText());
                    propiedad.setNombre(node.path("nombre").asText());
                    propiedad.setDescripcion(node.path("descripcion").asText());
                    propiedad.setDireccion(node.path("direccion").asText());
                    propiedad.setReglas(node.path("reglas").asText());
                    propiedad.setUrlFoto(node.path("urlFoto").asText());
                    propiedad.setCapacidad(node.path("capacidad").asInt());
                    propiedad.setPrecioPorNoche(node.path("precioPorNoche").asDouble());
                    //System.out.println(propiedad.toString());
                    propiedades.add(propiedad);
                }
                List<Propiedad> propiedades_6 =(propiedades.subList(Math.max(propiedades.size() - 6, 0), propiedades.size()));
                if (dataNode.size()!=0){
                    respuestaVacia=true;
                    for (int i = 0; i < 6; i++) {
                        items.add(new ListItem(propiedades_6.get(i).getUrlFoto(), propiedades_6.get(i).getNombre(), propiedades_6.get(i).getPrecioPorNoche() + ""));
                    }
                }

                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private void generateItemList() {
        System.out.println("una vez");
        Propiedad propiedad1 = new Propiedad();
        propiedad1.setNombre("Villa Vista al Mar");
        propiedad1.setDescripcion("Increíble villa con vista panorámica al océano.");
        propiedad1.setDireccion("Calle Principal, Ciudad Costera, País");
        propiedad1.setCapacidad(8);
        propiedad1.setReglas("No se permiten mascotas. Prohibido fumar en el interior.");
        propiedad1.setPrecioPorNoche(250.0);
        propiedad1.setUrlFoto("https://drive.google.com/uc?export=view&id=10Org_jBNzfjo3wNPOjqNRIytnVGcNAd9");

        Propiedad propiedad2 = new Propiedad();
        propiedad2.setNombre("Cabaña en el Bosque");
        propiedad2.setDescripcion("Acogedora cabaña rodeada de naturaleza.");
        propiedad2.setDireccion("Camino del Bosque, Pueblo Tranquilo, País");
        propiedad2.setCapacidad(4);
        propiedad2.setReglas("Se admiten mascotas con cargo adicional. Zona libre de humo.");
        propiedad2.setPrecioPorNoche(150.0);
        propiedad2.setUrlFoto("https://drive.google.com/uc?export=view&id=10Org_jBNzfjo3wNPOjqNRIytnVGcNAd9");

        Propiedad propiedad3 = new Propiedad();
        propiedad3.setNombre("Apartamento en la Ciudad");
        propiedad3.setDescripcion("Moderno apartamento en el corazón de la ciudad.");
        propiedad3.setDireccion("Avenida Principal, Centro Urbano, País");
        propiedad3.setCapacidad(2);
        propiedad3.setReglas("No se admiten mascotas. Prohibido fumar en todas las áreas.");
        propiedad3.setPrecioPorNoche(100.0);
        propiedad3.setUrlFoto("https://drive.google.com/uc?export=view&id=10Org_jBNzfjo3wNPOjqNRIytnVGcNAd9");

        Propiedad propiedad4 = new Propiedad();
        propiedad4.setNombre("Villa Vista al Mar");
        propiedad4.setDescripcion("Increíble villa con vista panorámica al océano.");
        propiedad4.setDireccion("Calle Principal, Ciudad Costera, País");
        propiedad4.setCapacidad(8);
        propiedad4.setReglas("No se permiten mascotas. Prohibido fumar en el interior.");
        propiedad4.setPrecioPorNoche(250.0);
        propiedad4.setUrlFoto("https://drive.google.com/uc?export=view&id=10Org_jBNzfjo3wNPOjqNRIytnVGcNAd9");

        Propiedad propiedad5 = new Propiedad();
        propiedad5.setNombre("Cabaña en el Bosque");
        propiedad5.setDescripcion("Acogedora cabaña rodeada de naturaleza.");
        propiedad5.setDireccion("Camino del Bosque, Pueblo Tranquilo, País");
        propiedad5.setCapacidad(4);
        propiedad5.setReglas("Se admiten mascotas con cargo adicional. Zona libre de humo.");
        propiedad5.setPrecioPorNoche(150.0);
        propiedad5.setUrlFoto("https://drive.google.com/uc?export=view&id=10Org_jBNzfjo3wNPOjqNRIytnVGcNAd9");

        Propiedad propiedad6 = new Propiedad();
        propiedad6.setNombre("Apartamento en la Ciudad");
        propiedad6.setDescripcion("Moderno apartamento en el corazón de la ciudad.");
        propiedad6.setDireccion("Avenida Principal, Centro Urbano, País");
        propiedad6.setCapacidad(2);
        propiedad6.setReglas("No se admiten mascotas. Prohibido fumar en todas las áreas.");
        propiedad6.setPrecioPorNoche(100.0);
        propiedad6.setUrlFoto("https://drive.google.com/uc?export=view&id=10Org_jBNzfjo3wNPOjqNRIytnVGcNAd9");

        propiedades.add(propiedad1);
        propiedades.add(propiedad2);
        propiedades.add(propiedad3);
        propiedades.add(propiedad4);
        propiedades.add(propiedad5);
        propiedades.add(propiedad6);
        for (int i = 0; i < propiedades.size(); i++) {
            items.add(new ListItem(propiedades.get(i).getUrlFoto(), propiedades.get(i).getNombre(), propiedades.get(i).getPrecioPorNoche() + ""));
        }
    }
}
