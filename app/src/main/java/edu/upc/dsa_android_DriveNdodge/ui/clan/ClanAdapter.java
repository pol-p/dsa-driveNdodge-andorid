package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Clan;

public class ClanAdapter extends RecyclerView.Adapter<ClanAdapter.ClanViewHolder> {

    private List<Clan> clans;
    private Context context;
    private OnItemClickListener listener;
    private final String BASE_URL_IMG = RetrofitClient.getBaseUrl() + "img/clan/";


    public interface OnItemClickListener {
        void onItemClick(Clan clan);
    }

    public ClanAdapter(List<Clan> clans, Context context, OnItemClickListener listener) {
        this.clans = clans;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_clan, parent, false);
        return new ClanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClanViewHolder holder, int position) {
        Clan clan = clans.get(position);

        holder.tvName.setText(clan.getNombre());

        String imgPath = clan.getImagen();
        if (imgPath == null || imgPath.isEmpty()) imgPath = "clan_default.png";

        Picasso.get().load(BASE_URL_IMG + imgPath).placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round).fit().centerCrop().into(holder.ivLogo);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(clan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clans != null ? clans.size() : 0;
    }

    public static class ClanViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        TextView tvName;

        public ClanViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLogo = itemView.findViewById(R.id.ivClanLogoRow);
            tvName = itemView.findViewById(R.id.tvClanNameRow);
        }
    }
}