package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.models.Usuario;

public class ClanMembersAdapter extends RecyclerView.Adapter<ClanMembersAdapter.ViewHolder> {

    private final List<Usuario> members;

    public ClanMembersAdapter(List<Usuario> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_members_clan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = members.get(position);
        holder.tvUsername.setText(usuario.getUsername());
        holder.ivUserAvatar.setImageResource(R.mipmap.ic_launcher_round);
    }

    @Override
    public int getItemCount() {
        return members != null ? members.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivUserAvatar;
        TextView tvUsername;

        ViewHolder(View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }
}
