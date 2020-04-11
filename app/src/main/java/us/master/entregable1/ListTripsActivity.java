package us.master.entregable1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import us.master.entregable1.adapters.TripAdapter;
import us.master.entregable1.entity.Trip;

public class ListTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Trip> trips;
    LinearLayout filter;
    Switch columns;

    static final int FILTERING_REQUEST = 1;  // The request code

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

        // boton de filtro
        filter = findViewById(R.id.layoutFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"Filtro", Toast.LENGTH_SHORT).show();
                Intent filterIntent = new Intent(getApplicationContext(), FilterTrips.class);
                // getApplicationContext().startActivity(filterIntent);
                startActivityForResult(filterIntent, FILTERING_REQUEST);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed
        if(requestCode == FILTERING_REQUEST) {

            ArrayList<Trip> tripsFiltered = new ArrayList<Trip>();

            String start_date = data.getStringExtra("START_DATE");
            String end_date = data.getStringExtra("END_DATE");
            String min_price = data.getStringExtra("MIN_PRICE");
            String max_price = data.getStringExtra("MAX_PRICE");

//            Log.d("JD", "START_DATE: " + start_date);
//            Log.d("JD", "END_DATE: " + end_date);
//            Log.d("JD", "MIN_PRICE: " + min_price);
//            Log.d("JD", "MAX_PRICE: " + max_price);

            if (start_date.length() == 0 && end_date.length() == 0 && min_price.length() == 0 && max_price.length() == 0) {
                // devuelvo la lista de todos los viajes
                tripsFiltered = trips;

            } else {

                // asigno valores min y max price

                float minPrice;
                if (min_price.length() > 0) {
                    minPrice = Float.parseFloat(min_price);
                } else {
                    minPrice = 0;
                }

                float maxPrice;
                if (max_price.length() > 0) {
                    maxPrice = Float.parseFloat(max_price);
                } else {
                    maxPrice = (float) Math.pow(10,600);
                }

                for(Trip trip : trips) {

                    // min price
                    if((Float.compare(trip.getPrice(),minPrice) > 0 ) || (Float.compare(trip.getPrice(),minPrice) == 0 )) { // f1 > f2 or f1=f2

                        // max price
                        if((Float.compare(trip.getPrice(),maxPrice) < 0 ) || (Float.compare(trip.getPrice(),maxPrice) == 0 )) { // f1 < f2 or f1=f2

                            // date

                            String trip_start_date = Util.dateToString(trip.getStartDate());
                            // Log.d("JD", "trip_start_date: " + trip_start_date);
                            // Log.d("JD", "start_date: " + start_date);
                            if (start_date.length() > 0 && trip_start_date.equals(start_date)) {
                                tripsFiltered.add(trip);
                            } else {
                                // tripsFiltered.add(trip);
                            }

                        }
                    }
                }
            }



            TripAdapter adapter = new TripAdapter(tripsFiltered);
            recyclerView.setAdapter(adapter);

        }
    }

}
