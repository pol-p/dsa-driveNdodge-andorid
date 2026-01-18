package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.ClanService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.ClanRanking;
import edu.upc.dsa_android_DriveNdodge.ui.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClanRankingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ClanService clanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.recyclerViewRanking);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        clanService = RetrofitClient.getClient().create(ClanService.class);
        loadRanking();
    }

    private void loadRanking() {
        progressBar.setVisibility(View.VISIBLE);

        clanService.getClanRanking().enqueue(new Callback<List<ClanRanking>>() {
            @Override
            public void onResponse(Call<List<ClanRanking>> call, Response<List<ClanRanking>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {

                    ClanRankingAdapter adapter = new ClanRankingAdapter(response.body(), clanName -> {
                        Intent intent = new Intent(ClanRankingActivity.this, ClanDetailActivity.class);
                        intent.putExtra("clanNombre", clanName);
                        startActivity(intent);
                    });

                    recyclerView.setAdapter(adapter);
                } else {
                    ToastUtils.show(ClanRankingActivity.this, "Error al cargar ranking", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<ClanRanking>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.show(ClanRankingActivity.this, "Error de red", Toast.LENGTH_SHORT);
            }
        });
    }
}