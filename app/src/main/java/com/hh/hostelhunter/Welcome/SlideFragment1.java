package com.hh.hostelhunter.Welcome;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hh.hostelhunter.R;

public class SlideFragment1 extends Fragment {

    public SlideFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slide_f1, container, false);

        // Get references to views in fragment layout
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.nombre);

        // Set image resource
        imageView.setImageResource(R.drawable.pensar1); // Reemplaza 'your_image' con el nombre de tu imagen en drawable

        // Set text
        //textView.setText("Tu texto aquí");

        return view;
    }
}
