package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import us.master.entregable1.entity.Trip;
import com.squareup.picasso.Picasso;

public class DetailsTripActivity extends AppCompatActivity {

    private TextView textViewDestino, textViewOrigen, textViewPrice, textViewStartDate, textViewEndDate, textViewDescription;
    private ImageView imageView, imageViewFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trip);

        imageView = findViewById(R.id.imageViewTripDetail);
        imageViewFav = findViewById(R.id.imageViewFav);
        textViewDestino = findViewById(R.id.textViewDestino);
        textViewOrigen = findViewById(R.id.textViewOrigen);
        textViewPrice = findViewById(R.id.textViewPrecio);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        textViewDescription = findViewById(R.id.textViewDescripcion);

        // Recovering data from intent extra
        Intent intent = getIntent();
        final Trip trip = (Trip) intent.getSerializableExtra("trip");

        // destino
        textViewDestino.setText(trip.getDestino());

        // origen
        textViewOrigen.setText(trip.getOrigen());

        // precio
        DecimalFormat dfp = new DecimalFormat("#.##");
        dfp.setRoundingMode(RoundingMode.CEILING);
        textViewPrice.setText(dfp.format(trip.getPrice()) + "€");

        // Format to Date String
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        textViewStartDate.setText(df.format(trip.getStartDate()));
        textViewEndDate.setText(df.format(trip.getEndDate()));

        textViewDescription.setText(trip.getDescription());

        Picasso.get()
                .load(trip.getImgUrl())
                .placeholder(R.drawable.ic_place_24dp)
                .error(R.drawable.ic_place_24dp)
                .resize(400, 400)
                .into(imageView);

        // Log.d("JD", "favorito: " + trip.isFavorite());
        if (trip.isFavorite()) {
            imageViewFav.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            imageViewFav.setImageResource(android.R.drawable.btn_star_big_off);
        }

        imageViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (trip.isFavorite()) {
                    imageViewFav.setImageResource(android.R.drawable.btn_star_big_off);
                    Util.doTripFavorite(trip,false);
                    trip.setFavorite(false);
                } else {
                    imageViewFav.setImageResource(android.R.drawable.btn_star_big_on);
                    Util.doTripFavorite(trip,true);
                    trip.setFavorite(true);
                }

            }
        });
    }
}
