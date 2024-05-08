package com.hh.hostelhunter.Welcome;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hh.hostelhunter.R;
import com.hh.hostelhunter.UserActions.LoginActivity;
import com.hh.hostelhunter.UserActions.RegistroActivity;

public class SlideFragment3 extends Fragment {
    private Button iniciarSesionButton;
    private Button registrarButton;
    public SlideFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slide_f3, container, false);

        // Get references to views in fragment layout
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.nombre);
        textView.setText("Dejanos ayudar con esto");
        // Set image resource
        imageView.setImageResource(R.drawable.pensar3); // Reemplaza 'your_image' con el nombre de tu imagen en drawable

        // Configura el listener para el botón de iniciar sesión
        iniciarSesionButton = view.findViewById(R.id.button2);
        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el contexto de la actividad que contiene el fragmento
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    //getActivity().finish();
                }
            }
        });


        registrarButton = view.findViewById(R.id.registrar);
        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el contexto de la actividad que contiene el fragmento
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), RegistroActivity.class));
                    //getActivity().finish();
                }
            }
        });

        return view;
    }
}


