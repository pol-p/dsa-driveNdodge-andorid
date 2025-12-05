package edu.upc.dsa_android_DriveNdodge.ui.shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.models.Item;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Item> items;
    private OnItemClickListener listener;

    // Interfaz para saber qué ítem se ha pulsado
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ShopAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el XML de la fila (row_shop_item)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nombre.setText(item.getNombre());
        holder.descripcion.setText(item.getDescripcion());
        holder.precio.setText(item.getPrecio() + " 💰");

        // Al hacer click en la tarjeta, avisamos a la Activity
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, precio;

        public ViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.textNombre);
            descripcion = view.findViewById(R.id.textDescripcion);
            precio = view.findViewById(R.id.textPrecio);
        }
    }
}