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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.master.entregable1.entity.Trip;
import us.master.entregable1.resttypes.WeatherResponse;
import us.master.entregable1.resttypes.WeatherRetrofitInterface;

import com.squareup.picasso.Picasso;

public class DetailsTripActivity extends AppCompatActivity {

    private TextView textViewDestino, textViewOrigen, textViewPrice, textViewStartDate, textViewEndDate, textViewDescription, textViewCoord;
    private ImageView imageView, imageViewFav;

    Retrofit retrofit;

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
        textViewCoord = findViewById(R.id.textViewCoordinates);

        // Recovering data from intent extra
        Intent intent = getIntent();
        final Trip trip = (Trip) intent.getSerializableExtra("trip");

        // recupera las coordenadas de destino de la api open weahter map
        // TODO
        retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherRetrofitInterface service = retrofit.create(WeatherRetrofitInterface.class);
        // Call<WeatherResponse> response = service.getCurrentWeatherLatLon(37.3453150f,-5.7420074f,getString(R.string.open_weather_map_api_key));
        Call<WeatherResponse> response = service.getCurrentWeatherCityId(2510911,getString(R.string.open_weather_map_api_key));
        response.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("JD", "Las coordenadas son " + response.body().getCoord());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d("JD", "REST: Error en la llamada. " + t.getMessage());
            }
        });

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
