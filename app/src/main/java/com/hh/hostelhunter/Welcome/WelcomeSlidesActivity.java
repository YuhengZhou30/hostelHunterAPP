package com.hh.hostelhunter.Welcome;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import com.hh.hostelhunter.R;

public class WelcomeSlidesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_slides);

        ViewPager viewPager = findViewById(R.id.view_pager);

        // Obtener el FragmentManager y el Lifecycle de la actividad
        FragmentManager fragmentManager = getSupportFragmentManager();
        Lifecycle lifecycle = getLifecycle();

        // Crear una instancia del adaptador y pasar el FragmentManager y el Lifecycle
        WelcomeSlidesPagerAdapter adapter = new WelcomeSlidesPagerAdapter(fragmentManager, lifecycle);

        // Establecer el adaptador en el ViewPager
        WelcomeSlidesPagerAdapter pagerAdapter = adapter; // Cambia el tipo de la variable adapter a PagerAdapter
        viewPager.setAdapter(pagerAdapter);
    }
}
