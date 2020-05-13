package us.master.entregable1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import us.master.entregable1.adapters.TripAdapter;
import us.master.entregable1.entity.Trip;

public class ListTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Trip> trips;
    LinearLayout filter;
    Switch columns;

    TextView txtViewNoTrips;
    Button btnNoTrips;

    private DataChangedListener mDataChangedListener;
    private ItemErrorListener mErrorListener;
    private Context context;

    static final int FILTERING_REQUEST = 1;  // The request code
    int position_enlace = -1;

    // para los filtros
    Long filterMinPrice = Long.valueOf(0);
    Long filterMaxPrice = Long.valueOf(0);
    Long filterStartDate = Long.valueOf(0);
    Long filterEndDate = Long.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trips);

        txtViewNoTrips = findViewById(R.id.textViewNoTrips);
        btnNoTrips = findViewById(R.id.buttonNoTrips);

        if (Constantes.trips.size() > 0) {
            txtViewNoTrips.setVisibility(View.GONE);
            btnNoTrips.setVisibility(View.GONE);
        } else {
            txtViewNoTrips.setVisibility(View.VISIBLE);
            btnNoTrips.setVisibility(View.VISIBLE);
        }

        // Recovering data from intent extra
        Intent intent = getIntent();
        position_enlace = (int) intent.getSerializableExtra("position_enlace");
        // Toast.makeText(getApplicationContext(),"position: " + position_enlace, Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recyclerViewTrips);

        // diferenciamos si el enlace es para viajes favoritos o no
        if (position_enlace == 0) {
            // todos los viajes
            trips = Constantes.trips;

        } else {
            // trips favoritos
            trips = new ArrayList<Trip>();
            for (Trip t : Constantes.trips) {
                if (t.isFavorite()) {
                    trips.add(t);
                }
            }
        }

        TripAdapter adapter = new TripAdapter(trips, position_enlace);
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
//                Log.d("JD", "filter start activity");
//                Log.d("JD", "filterMinPrice: " + filterMinPrice);
//                Log.d("JD", "filterMaxPrice: " + filterMaxPrice);
//                Log.d("JD", "filterStartDate: " + filterStartDate);
//                Log.d("JD", "filterEndDate: " + filterEndDate);

                Intent intent = new Intent(ListTripsActivity.this, FilterTrips.class);
                intent.putExtra("MIN_PRICE",String.valueOf(filterMinPrice));
                intent.putExtra("MAX_PRICE",String.valueOf(filterMaxPrice));
                intent.putExtra("START_DATE",String.valueOf(filterStartDate));
                intent.putExtra("END_DATE",String.valueOf(filterEndDate));
                startActivityForResult(intent, FILTERING_REQUEST);
            }
        });

        btnNoTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListTripsActivity.this, NewTravelActivity.class));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("JD", "filter on activity result");
//        Log.d("JD", "requestCode: " + requestCode);
//        Log.d("JD", "resultCode: " + resultCode);

        if (requestCode == FILTERING_REQUEST) {
//            Log.d("JD", "requestCode == FILTERING_REQUEST ->" + requestCode);

            // filtro pulsando aplicar
            if (resultCode == RESULT_OK) {

//                Log.d("JD", "resultCode == RESULT_OK ->" + resultCode);
                filterMinPrice = data.getLongExtra("MIN_PRICE",0);
                filterMaxPrice = data.getLongExtra("MAX_PRICE",0);
                filterStartDate = data.getLongExtra("START_DATE",0);
                filterEndDate = data.getLongExtra("END_DATE",0);

//                Log.d("JD", "filterMinPrice: " + filterMinPrice);
//                Log.d("JD", "filterMaxPrice: " + filterMaxPrice);
//                Log.d("JD", "filterStartDate: " + filterStartDate);
//                Log.d("JD", "filterEndDate: " + filterEndDate);

                this.refreshRecycle(filterStartDate,filterEndDate,filterMinPrice,filterMaxPrice);
            }
        } else {
//            Log.d("JD", "requestCode else ->" + requestCode);
            if(requestCode == 2) {
                this.refreshRecycle(filterStartDate,filterEndDate,filterMinPrice,filterMaxPrice);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void refreshRecycle(final Long filterStartDate, final Long filterEndDate, final Long filterMinPrice, final Long filterMaxPrice) {
        List<Trip> filterTripsList = new ArrayList<Trip>();

//        Log.d("JD", "count elements trips filter: " + filterTripsList.size());
        for (Trip trip : trips) {
            boolean added = false;

            boolean checkMinPrice  = compruebaMinPrice(filterMinPrice, trip);
            boolean checkMaxPrice  = compruebaMaxPrice(filterMaxPrice, trip);
            boolean checkStartDate = compruebaStartDate(filterStartDate, trip);
            boolean checkEndDate   = compruebaEndDate(filterEndDate, trip);

//            Log.d("JD", "Trip : " + trip.getDestino() + " - check: checkMinPrice" + checkMinPrice + ", checkMaxPrice: " + checkMaxPrice + ", checkStartDate: " + checkStartDate + ", checkEndDate: " + checkEndDate);

            // si no pasa algun filtro no se añade
            if (checkMinPrice && checkMaxPrice && checkStartDate && checkEndDate) {
                filterTripsList.add(trip);
            }
        }

//        Log.d("JD", "count elements trips filter: " + filterTripsList.size());
        TripAdapter adapter = new TripAdapter(filterTripsList, position_enlace);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),1));
    }

    private boolean compruebaStartDate(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter <= trip.getStartDate().getTime()){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean compruebaEndDate(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter >= trip.getEndDate().getTime()){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean compruebaMinPrice(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter <= trip.getPrice()){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean compruebaMaxPrice(Long filter, Trip trip){
        if(filter == Long.valueOf(0)){
            return true;
        } else {
            if(filter >= trip.getPrice()){
                return true;
            } else {
                return false;
            }
        }
    }

    public void setErrorListener(ItemErrorListener itemErrorListener) {
        mErrorListener = itemErrorListener;
    }

    public interface ItemErrorListener {
        void onItemError(FirebaseFirestoreException error);
    }

    public void setDataChangedListener(DataChangedListener dataChangedListener) {
        mDataChangedListener = dataChangedListener;
    }

    public interface  DataChangedListener {
        void onDataChanged();
    }

}
