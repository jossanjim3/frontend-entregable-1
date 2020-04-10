package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FilterTrips extends AppCompatActivity {

    TextView txtViewStartDate, txtViewEndDate, txtViewMinPrice, txtViewMaxPrice;
    ImageView imgViewStartDate, imgViewEndDate, imgViewMinPrice, imgViewMaxPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_trips);

        txtViewStartDate = findViewById(R.id.textViewStartDate);
        txtViewEndDate = findViewById(R.id.textViewEndDate);
        txtViewMinPrice = findViewById(R.id.textViewMinPrice);
        txtViewMaxPrice= findViewById(R.id.textViewMaxPrice);

        imgViewStartDate = findViewById(R.id.imageViewStartDate);
        imgViewEndDate = findViewById(R.id.imageViewEndDate);
        imgViewMinPrice = findViewById(R.id.imageViewMinPrice);
        imgViewMaxPrice = findViewById(R.id.imageViewMaxPrice);

        imgViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"start date", Toast.LENGTH_SHORT).show();
            }
        });

        imgViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"end date", Toast.LENGTH_SHORT).show();
            }
        });

        imgViewMinPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"min price", Toast.LENGTH_SHORT).show();
            }
        });

        imgViewMaxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"max price", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
