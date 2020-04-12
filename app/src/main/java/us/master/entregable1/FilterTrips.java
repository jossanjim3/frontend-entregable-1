package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    static final int FILTERING_REQUEST = 1;  // The request code

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
                        int monthAux = month+1;
                        String monthDate = "";
                        if (monthAux < 10){
                            monthDate = "0"+ monthAux;
                        } else {
                            monthDate = "" +  monthAux;
                        }

                        String dayDate = "";
                        if (day < 10) {
                            dayDate = "0" + day;
                        } else {
                            dayDate = Integer.toString(day);
                        }
                        final String selectedDate = dayDate + "/" + monthDate + "/" + year;
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
                        int monthAux = month+1;
                        String monthDate = "";
                        if (monthAux < 10){
                            monthDate = "0"+ monthAux;
                        } else {
                            monthDate = "" +  monthAux;
                        }

                        String dayDate = "";
                        if (day < 10) {
                            dayDate = "0" + day;
                        } else {
                            dayDate = Integer.toString(day);
                        }

                        final String selectedDate = dayDate + "/" + monthDate + "/" + year;
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
                // Toast.makeText(getApplicationContext(),"button filter", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                // Log.d("JD", "" +  txtViewMinPrice.getText());
                intent.putExtra("START_DATE","" +  txtViewStartDate.getText());
                intent.putExtra("END_DATE","" +  txtViewEndDate.getText());
                intent.putExtra("MIN_PRICE","" +  txtViewMinPrice.getText());
                intent.putExtra("MAX_PRICE","" +  txtViewMaxPrice.getText());
                setResult(FILTERING_REQUEST,intent);
                finish();//finishing activity
            }
        });

    }
}
