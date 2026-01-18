package edu.upc.dsa_android_DriveNdodge.ui.ranking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.UsrRanking;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    private List<UsrRanking> usuarios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String username);
    }
    public RankingAdapter(List<UsrRanking> usuarios, OnItemClickListener listener) {
        this.usuarios = usuarios;
        this.listener = listener;
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

        holder.position.setText((position + 1) + "º");
        holder.username.setText(user.getUsername());
        holder.score.setText(String.valueOf(user.getMejorPuntuacion()));

        String imgPath = user.getImagenPerfil();

        if (imgPath == null || imgPath.isEmpty()) {
            imgPath = "avatar_default.webp";
        }

        Picasso.get().load(RetrofitClient.getBaseUrl() + "img/avatar/" + imgPath).placeholder(R.drawable.logo).error(R.mipmap.ic_launcher_round).into(holder.avatar);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user.getUsername());
            }
        });

        if (position == 0) holder.position.setTextColor(0xFFFFD700); // Oro
        else if (position == 1) holder.position.setTextColor(0xFFC0C0C0); // Plata
        else if (position == 2) holder.position.setTextColor(0xFFCD7F32); // Bronce
        else holder.position.setTextColor(0xFFFFFFFF); // Blanco
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView position, username, score;
        CircleImageView avatar;

        public ViewHolder(View view) {
            super(view);
            position = view.findViewById(R.id.textPosition);
            username = view.findViewById(R.id.textUsername);
            score = view.findViewById(R.id.textScore);
            avatar = view.findViewById(R.id.ivRankingAvatar);
        }
    }
}