package us.master.entregable1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import us.master.entregable1.DetailsTripActivity;
import us.master.entregable1.R;
import us.master.entregable1.entity.Trip;
import com.squareup.picasso.Picasso;

public class TripAdapter  extends
        RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> mTrips;
    private Context context;
    public TripAdapter(List<Trip> mTrips) {
        this.mTrips = mTrips;
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
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageView;

        textViewTitle.setText("Destino: " + trip.getTitle());

        // description
        StringBuilder sb_desc = new StringBuilder();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        sb_desc.append("Precio: " + df.format(trip.getPrice()) + "€");
        sb_desc.append("\n");

        sb_desc.append("Origen: " + trip.getDescription());

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
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, textViewDescription;
        private ImageView imageView;
        private View tripView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripView = itemView;
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageViewTrip);
        }
    }
}
