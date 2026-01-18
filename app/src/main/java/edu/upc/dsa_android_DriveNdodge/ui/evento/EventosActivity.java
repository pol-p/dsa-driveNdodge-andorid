package edu.upc.dsa_android_DriveNdodge.ui.evento;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.EventService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Evento;
import edu.upc.dsa_android_DriveNdodge.ui.main.PortalPageActivity;
import edu.upc.dsa_android_DriveNdodge.ui.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EventoAdapter adapter;
    private List<Evento> eventos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        // UI
        recyclerView = findViewById(R.id.recyclerViewEventos);
        progressBar = findViewById(R.id.progressBarEventos);
        Button btnBack = findViewById(R.id.btnBackEventos);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new EventoAdapter(eventos, evento -> {
            Intent intent = new Intent(EventosActivity.this, EventDetailActivity.class);
            intent.putExtra("evento", evento);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);


        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        cargarEventos();
    }

    private void cargarEventos() {
        progressBar.setVisibility(View.VISIBLE);

        EventService service =
                RetrofitClient.getClient().create(EventService.class);

        service.getEvents().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call,
                                   Response<List<Evento>> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    eventos.clear();
                    eventos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(EventosActivity.this, "No se pudieron cargar los eventos", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.show(EventosActivity.this, "Error de conexión", Toast.LENGTH_SHORT);
            }
        });
    }
}
