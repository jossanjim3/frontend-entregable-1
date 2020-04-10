package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import us.master.entregable1.dialog.DatePickerFragment;

public class FilterTrips extends AppCompatActivity {

    TextView txtViewStartDate, txtViewEndDate, txtViewMinPrice, txtViewMaxPrice;
    ImageView imgViewStartDate, imgViewEndDate;
    Button btnFilter;

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

        imgViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"start date", Toast.LENGTH_SHORT).show();
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + " / " + (month+1) + " / " + year;
                        txtViewStartDate.setText(selectedDate);
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        imgViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"end date", Toast.LENGTH_SHORT).show();

                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + " / " + (month+1) + " / " + year;
                        txtViewEndDate.setText(selectedDate);
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        btnFilter = findViewById(R.id.buttonFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"button filter", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
