package com.hh.hostelhunter.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hh.hostelhunter.R;
import com.hh.hostelhunter.ui.dashboard.DashboardFragment;
import com.hh.hostelhunter.ui.notifications.PerfilView;

public class ui extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.nav_view);

        setupViewPager(viewPager);

        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.navigation_dashboard) {
                            viewPager.setCurrentItem(0);
                            return true;
                        } else if (itemId == R.id.navigation_notifications) {
                            viewPager.setCurrentItem(1);
                            return true;
                        }
                        return false;
                    }
                }
        );



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardFragment(), "Casas");  // Se añade el nombre del fragmento como segundo argumento
        adapter.addFragment(new PerfilView(), "Perfil");  // Se añade el nombre del fragmento como segundo argumento
        viewPager.setAdapter(adapter);
    }

}
