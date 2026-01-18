package edu.upc.dsa_android_DriveNdodge.ui.evento;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.models.Evento;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Button btnBack = findViewById(R.id.btnBackEvent);
        TextView tvTitle = findViewById(R.id.tvEventTitle);
        TextView tvDesc = findViewById(R.id.tvEventDesc);
        ImageView ivImage = findViewById(R.id.ivEventImage);

        btnBack.setOnClickListener(v -> finish());

        Evento evento = (Evento) getIntent().getSerializableExtra("evento");


        if (evento == null) {
            finish();
            return;
        }

        tvTitle.setText(evento.getNombre());
        tvDesc.setText(evento.getDescripcion());

        Picasso.get()
                .load(evento.getImagen())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fit()
                .centerCrop()
                .into(ivImage);
    }
}
