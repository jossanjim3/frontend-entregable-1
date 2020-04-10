package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import us.master.entregable1.adapters.TripAdapter;
import us.master.entregable1.entity.Trip;

public class ListTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Trip> trips;
    LinearLayout filter;
    Switch columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trips);

        // Recovering data from intent extra
        Intent intent = getIntent();
        int position_enlace = (int) intent.getSerializableExtra("position_enlace");
        // Toast.makeText(getApplicationContext(),"position: " + position_enlace, Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recyclerViewTrips);

        // diferenciamos si son viajes favoritos o no
        if (position_enlace == 0) {
            // listado de trips aleatorio
            trips = Trip.generateTrips();
        } else {
            // listado de trips favoritos
            trips = new ArrayList<Trip>();
        }

        TripAdapter adapter = new TripAdapter(trips);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        // boton columnas, 1 o 2 columnas
        columns = findViewById(R.id.columns);
        columns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(columns.isChecked()){
                    recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),1));
                }
            }
        });

        // TODO
        // boton de filtro
        filter = findViewById(R.id.layoutFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"Filtro", Toast.LENGTH_SHORT).show();
                Intent filterIntent = new Intent(getApplicationContext(), FilterTrips.class);
                getApplicationContext().startActivity(filterIntent);
            }
        });
    }
}
