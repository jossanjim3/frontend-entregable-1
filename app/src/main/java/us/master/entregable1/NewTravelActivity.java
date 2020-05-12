package us.master.entregable1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import us.master.entregable1.dialog.DatePickerFragment;
import us.master.entregable1.entity.Trip;

public class NewTravelActivity extends AppCompatActivity {

    private EditText eTxtDestino, eTxtOrigen, eTxtDescription, eTxtPrecio;
    private TextView txtStartDate, txtEndDate;
    private Button btnSave;
    private ImageView imgViewStartDate, imgViewEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);

        eTxtDestino = findViewById(R.id.new_travel_edit_destino);
        eTxtOrigen = findViewById(R.id.new_travel_edit_origen);
        eTxtDescription = findViewById(R.id.new_travel_edit_description);
        eTxtPrecio = findViewById(R.id.new_travel_edit_price);
        txtStartDate = findViewById(R.id.new_travel_textViewStartDate);
        txtEndDate = findViewById(R.id.new_travel_textViewEndDate);

        imgViewStartDate = findViewById(R.id.new_travel_imageViewStartDate);
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
                        txtStartDate.setText(selectedDate);
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        imgViewEndDate = findViewById(R.id.new_travel_imageViewEndDate);
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
                        txtEndDate.setText(selectedDate);
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnSave = findViewById(R.id.new_travel_button_save);

        btnSave.setOnClickListener(l -> saveTravel());

    }

    private void saveTravel() {
        // Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();

        String destino = eTxtDestino.getText().toString();
        String origen = eTxtOrigen.getText().toString();
        String descrip = eTxtDescription.getText().toString();
        Float precio = Float.parseFloat(eTxtPrecio.getText().toString());
        Long sDate = getStartDate();
        Long eDate = getEndDate();

        if(sDate== Long.valueOf(0)|| eDate == Long.valueOf(0) || origen == "" || destino == "" || descrip == "" || precio <= 0 ){
            Toast.makeText(this, getString(R.string.empty_fields_trip), Toast.LENGTH_SHORT).show();
        } else {
            Trip trip = new Trip();
            trip.setDestino(destino);
            trip.setOrigen(origen);
            trip.setDescription(descrip);
            trip.setPrice(precio);
            trip.setStartDate(new Date(getStartDate()));
            trip.setEndDate(new Date(getEndDate()));

            FirestoreService.getServiceInstance().saveTravel(trip, new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Log.d("JD", "Trip insertado");
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("JD", "Error al insertar Trip ");
                    }
                }
            });

        }
    }

    public Long getStartDate(){
        if( (String) txtStartDate.getText() == "dd/mm/yyyy"){
            return Long.valueOf(0);
        } else {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse((String) txtStartDate.getText()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return Long.valueOf(0);
            }
        }
    }

    public Long getEndDate(){
        if((String) txtEndDate.getText() == "dd/mm/yyyy"){
            return Long.valueOf(0);
        } else {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse((String) txtEndDate.getText()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return Long.valueOf(0);
            }
        }
    }
}
