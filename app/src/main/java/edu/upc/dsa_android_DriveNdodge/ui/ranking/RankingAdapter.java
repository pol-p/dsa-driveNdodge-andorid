package edu.upc.dsa_android_DriveNdodge.ui.ranking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.models.UsrRanking;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    private List<UsrRanking> usuarios;

    public RankingAdapter(List<UsrRanking> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsrRanking user = usuarios.get(position);

        // La posición es el índice + 1 (para que empiece en 1º, no en 0º)
        holder.position.setText((position + 1) + "º");
        holder.username.setText(user.getUsername());
        holder.score.setText(user.getMejorPuntuacion() + " pts");
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView position, username, score;

        public ViewHolder(View view) {
            super(view);
            position = view.findViewById(R.id.textPosition);
            username = view.findViewById(R.id.textUsername);
            score = view.findViewById(R.id.textScore);
        }
    }
}