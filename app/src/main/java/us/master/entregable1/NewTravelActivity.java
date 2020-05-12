package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        imgViewEndDate = findViewById(R.id.new_travel_imageViewEndDate);

        btnSave = findViewById(R.id.new_travel_button_save);

        btnSave.setOnClickListener(l -> saveTravel());

    }

    private void saveTravel() {
        Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
    }
}
