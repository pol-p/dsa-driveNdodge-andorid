package edu.upc.dsa_android_DriveNdodge.ui.inventario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.models.ItemInventario;
import com.squareup.picasso.Picasso;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {

    private List<ItemInventario> inventario;

    public InventarioAdapter(List<ItemInventario> inventario) {
        this.inventario = inventario;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inventario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemInventario item = inventario.get(position);

        holder.nombre.setText(item.getNombre());
        holder.descripcion.setText(item.getDescripcion());

        holder.cantidad.setText("x " + item.getCantidad());

        Picasso.get().load(item.getImagen()).placeholder(R.drawable.logo).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return inventario.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, cantidad;
        ImageView imagen;

        public ViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.txtNombre);
            descripcion = view.findViewById(R.id.txtDescripcion);
            cantidad = view.findViewById(R.id.txtCantidad);
            imagen = view.findViewById(R.id.imgItem);
        }
    }
}