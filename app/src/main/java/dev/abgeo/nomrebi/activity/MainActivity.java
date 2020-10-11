package dev.abgeo.nomrebi.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import dev.abgeo.nomrebi.R;
import dev.abgeo.nomrebi.task.CheckUpdateTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check update and get dialog if needed.
        new CheckUpdateTask(this).execute();

        Navigation.findNavController(findViewById(R.id.nav_host_fragment))
                .addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(
                            @NonNull NavController controller,
                            @NonNull NavDestination destination,
                            @Nullable Bundle arguments
                    ) {
                        boolean displayHome = !Objects.equals(destination.getLabel(), "SearchFragment");

                        getSupportActionBar().setDisplayHomeAsUpEnabled(displayHome);
                        getSupportActionBar().setDisplayShowHomeEnabled(displayHome);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Navigation.findNavController(findViewById(R.id.nav_host_fragment))
                    .navigate(R.id.action_to_aboutFragment);

            return true;
        }

        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
