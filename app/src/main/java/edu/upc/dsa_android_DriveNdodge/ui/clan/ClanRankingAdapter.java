package edu.upc.dsa_android_DriveNdodge.ui.clan;

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
import edu.upc.dsa_android_DriveNdodge.models.ClanRanking;

public class ClanRankingAdapter extends RecyclerView.Adapter<ClanRankingAdapter.ViewHolder> {

    private List<ClanRanking> clanes;
    private OnClanClickListener listener;

    public interface OnClanClickListener {
        void onClanClick(String clanName);
    }

    public ClanRankingAdapter(List<ClanRanking> clanes, OnClanClickListener listener) {
        this.clanes = clanes;
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
        ClanRanking clan = clanes.get(position);

        holder.position.setText((position + 1) + "º");
        holder.name.setText(clan.getNombre());
        holder.score.setText(clan.getPuntos() + " pts");

        String imgPath = clan.getImagen();
        if (imgPath == null || imgPath.isEmpty()) {
            imgPath = "/img/clan/clan_default.png";
        }

        Picasso.get().load(RetrofitClient.getBaseUrl() + imgPath).placeholder(R.drawable.logo).error(R.drawable.logo).into(holder.avatar);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClanClick(clan.getNombre());
        });

        if (position == 0) holder.position.setTextColor(0xFFFFD700); // oro
        else if (position == 1) holder.position.setTextColor(0xFFC0C0C0); // plata
        else if (position == 2) holder.position.setTextColor(0xFFCD7F32); // bronce
        else holder.position.setTextColor(0xFFFFFFFF); // blanco
    }

    @Override
    public int getItemCount() { return clanes != null ? clanes.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView position, name, score;
        CircleImageView avatar;

        ViewHolder(View view) {
            super(view);
            position = view.findViewById(R.id.textPosition);
            name = view.findViewById(R.id.textUsername);
            score = view.findViewById(R.id.textScore);
            avatar = view.findViewById(R.id.ivRankingAvatar);
        }
    }
}