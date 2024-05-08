package com.hh.hostelhunter.Welcome;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;


public class WelcomeSlidesPagerAdapter extends FragmentPagerAdapter {

    public WelcomeSlidesPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Devolver el fragmento correspondiente a la posición
        switch (position) {
            case 0:
                return new SlideFragment1();
            case 1:
                return new SlideFragment2();
            case 2:
                return new SlideFragment3();
            // Agrega más casos según sea necesario para más diapositivas
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Devolver el número total de diapositivas
        return 3; // Cambia este valor según la cantidad de diapositivas que tengas
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Devolver el título de la página en la posición especificada
        switch (position) {
            case 0:
                return "Slide 1";
            case 1:
                return "Slide 2";
            case 2:
                return "Slide 3";

            // Agrega más casos según sea necesario para más diapositivas
            default:
                return null;
        }
    }
}
