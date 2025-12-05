package edu.upc.dsa_android_DriveNdodge.ui.ranking;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.api.ShopService;
import edu.upc.dsa_android_DriveNdodge.models.UsrRanking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRankingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.recyclerViewRanking);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRanking();
    }

    private void loadRanking() {
        progressBar.setVisibility(View.VISIBLE);

        ShopService service = RetrofitClient.getClient().create(ShopService.class);
        Call<List<UsrRanking>> call = service.getRanking();

        call.enqueue(new Callback<List<UsrRanking>>() {
            @Override
            public void onResponse(Call<List<UsrRanking>> call, Response<List<UsrRanking>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<UsrRanking> ranking = response.body();

                    // Conectamos el Adapter
                    RankingAdapter adapter = new RankingAdapter(ranking);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ViewRankingActivity.this, "Error al cargar ranking", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UsrRanking>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewRankingActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}