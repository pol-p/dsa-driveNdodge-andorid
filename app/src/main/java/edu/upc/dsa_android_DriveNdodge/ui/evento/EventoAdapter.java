package edu.upc.dsa_android_DriveNdodge.ui.evento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.models.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {

    private List<Evento> eventos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Evento evento);
    }

    public EventoAdapter(List<Evento> eventos, OnItemClickListener listener) {
        this.eventos = eventos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento evento = eventos.get(position);

        Picasso.get()
                .load(evento.getImagen())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fit()
                .centerCrop()
                .into(holder.imgEvento);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(evento));
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgEvento;

        public ViewHolder(View view) {
            super(view);
            imgEvento = view.findViewById(R.id.imgEvento);
        }
    }
}
