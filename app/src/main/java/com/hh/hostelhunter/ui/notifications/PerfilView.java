package com.hh.hostelhunter.ui.notifications;

import static android.app.Activity.RESULT_OK;
import static com.hh.hostelhunter.Data.Datos_memoria.usuarioLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hh.hostelhunter.R;
import com.hh.hostelhunter.UserActions.LoginActivity;

public class PerfilView extends Fragment {
    static int EDITAR_PERFIL_REQUEST_CODE=1;
    TextView perfil,logout,nombre;
    ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.perfil, container, false);

        // Obtener referencias a los TextViews clickeables
        perfil= view.findViewById(R.id.Perfil);
        logout = view.findViewById(R.id.logout);
        nombre= view.findViewById(R.id.nombre);
        imageView=view.findViewById(R.id.imageView);
        nombre.setText(usuarioLogin.getUsername());
        String[] partes = usuarioLogin.getUrlFoto().split("\"");
        // Unir las partes con un espacio como separador
        usuarioLogin.setUrlFoto( String.join("", partes));
        Glide.with(this)
                .load(usuarioLogin.getUrlFoto())
                .centerInside()
                .into(imageView);
        // Configurar listeners de clics para los TextViews clickeables
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), EditarPerfil.class), EDITAR_PERFIL_REQUEST_CODE);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se hace clic en el TextView clickeable 2
                Toast.makeText(getActivity(), "Cerrado", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITAR_PERFIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Aquí actualizas la foto del perfil utilizando la URL obtenida de EditarPerfil
                String nuevaUrlFoto = data.getStringExtra("urlFoto");
                // Actualiza la foto del perfil con la nueva URL
                Glide.with(this)
                        .load(nuevaUrlFoto)
                        .centerInside()
                        .into(imageView);
            }
        }
    }

}