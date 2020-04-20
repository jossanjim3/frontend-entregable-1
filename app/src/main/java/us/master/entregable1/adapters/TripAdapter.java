package us.master.entregable1.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import us.master.entregable1.Constantes;
import us.master.entregable1.DetailsTripActivity;
import us.master.entregable1.R;
import us.master.entregable1.entity.Trip;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class TripAdapter  extends
        RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> mTrips;
    private Context context;
    int position_enlace = -1;

    public TripAdapter(List<Trip> mTrips, int position_enlace) {
        // diferenciamos si el enlace es para viajes favoritos o no
        this.mTrips = mTrips;
        this.position_enlace = position_enlace;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tripView = layoutInflater.inflate(R.layout.trip_item, parent, false);
        return new ViewHolder(tripView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trip trip = mTrips.get(position);
        TextView textViewDestino = holder.textViewDestino;
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageView;
        Button btnComprar = holder.buttonComprar;

        // destino
        textViewDestino.setText("Destino: " + trip.getDestino());

        // description
        StringBuilder sb_desc = new StringBuilder();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        sb_desc.append("Precio: " + df.format(trip.getPrice()) + "€");
        sb_desc.append("\n");

        sb_desc.append("Origen: " + trip.getOrigen());
        sb_desc.append("\n");

        String pattern = "dd/MM/yyyy";
        DateFormat dfDate = new SimpleDateFormat(pattern);
        sb_desc.append(dfDate.format(trip.getStartDate()) + " - " + dfDate.format(trip.getEndDate()));

        textViewDescription.setText(sb_desc.toString());

        // imagen
        Picasso.get()
                .load(trip.getImgUrl())
                .placeholder(R.drawable.ic_place_24dp)
                .error(R.drawable.ic_place_24dp)
                .resize(100, 100)
                .into(imageView);

        holder.tripView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, DetailsTripActivity.class);
                intent.putExtra("trip", trip);
                context.startActivity(intent);
            }
        });

        // diferenciamos si el enlace es para viajes favoritos o no
        if (position_enlace == 0) {
            // todos los viajes
            btnComprar.setVisibility(View.GONE);
        } else {
            // trips favoritos
            btnComprar.setVisibility(View.VISIBLE);
        }
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Viaje a " + trip.getDestino() + " añadido al carrito.", Snackbar.LENGTH_LONG)
                        .setAction("Buy", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDestino, textViewDescription;
        private ImageView imageView;
        private Button buttonComprar;
        private View tripView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripView = itemView;
            textViewDestino = itemView.findViewById(R.id.textViewDestino);
            textViewDescription = itemView.findViewById(R.id.textViewPrecio);
            imageView = itemView.findViewById(R.id.imageViewTrip);
            buttonComprar = itemView.findViewById(R.id.buttonComprar);
        }
    }
}
