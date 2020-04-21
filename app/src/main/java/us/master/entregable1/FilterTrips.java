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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        txtViewMaxPrice = findViewById(R.id.textViewMaxPrice);

        // recupero los valores que paso por intenet desde list trip activity
        Intent intent = getIntent();
        txtViewMinPrice.setText(intent.getStringExtra("MIN_PRICE"));
        txtViewMaxPrice.setText(intent.getStringExtra("MAX_PRICE"));
        setStartDate(intent.getStringExtra("START_DATE"));
        setEndDate(intent.getStringExtra("END_DATE"));


        // start date picker
        imgViewStartDate = findViewById(R.id.imageViewStartDate);
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

        // end date picker
        imgViewEndDate = findViewById(R.id.imageViewEndDate);
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


        // boton de filtrar
        btnFilter = findViewById(R.id.buttonFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"button filter", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                // Log.d("JD", "" +  txtViewMinPrice.getText());
                intent.putExtra("START_DATE",getStartDate());
                intent.putExtra("END_DATE",getEndDate());
                intent.putExtra("MIN_PRICE", getMinPrice());
                intent.putExtra("MAX_PRICE",getMaxPrice());
                setResult(RESULT_OK,intent);
                finish();//finishing activity
            }
        });

    }

    public void setStartDate(String value) {
        System.out.println("**************" + value);
        if(!value.equals("0")) {
            Date d = new Date();
            d.setTime(Long.valueOf(value));
            String pattern = "dd/MM/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            txtViewStartDate.setText(df.format(d));
        } else {
            txtViewStartDate.setText("dd/mm/yyyy");
        }
    }

    public void setEndDate(String value) {
        if(!value.equals("0")) {
            Date d = new Date();
            d.setTime(Long.valueOf(value));
            String pattern = "dd/MM/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            txtViewEndDate.setText(df.format(d));
        } else {
            txtViewEndDate.setText("dd/mm/yyyy");
        }
    }

    public Long getMinPrice() {
        if (txtViewMinPrice.getText().length() <= 0){
            return Long.valueOf(0);
        } else {
            return Long.valueOf(String.valueOf(txtViewMinPrice.getText()));
        }
    }

    public Long getMaxPrice() {
        if (txtViewMaxPrice.getText().length() <= 0){
            return Long.valueOf(0);
        } else {
            return Long.valueOf(String.valueOf(txtViewMaxPrice.getText()));
        }
    }

    public Long getStartDate(){
        if( (String) txtViewStartDate.getText() == "dd/mm/yyyy"){
            return Long.valueOf(0);
        } else {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse((String) txtViewStartDate.getText()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return Long.valueOf(0);
            }
        }
    }

    public Long getEndDate(){
        if((String) txtViewEndDate.getText() == "dd/mm/yyyy"){
            return Long.valueOf(0);
        } else {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse((String) txtViewEndDate.getText()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return Long.valueOf(0);
            }
        }
    }

}
