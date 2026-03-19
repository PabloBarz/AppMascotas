package com.example.appmascotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//RecyclerViewAdapter exige 3 metodos
//onCreateViewHolder :  Crea una fila/item
//onBindViewHolder   :  Llenar los datos de la fila /item
//getItemCount       :  Cantidad de elementos
public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {
    private final ArrayList<Mascotas> lista;
    private final Context context;
    private final OnAccionListener listener;

    //Interface
    public interface OnAccionListener{
        void onEditar(int position, Mascotas mascotas);
        void onEliminar(int position, Mascotas mascotas);
    }
    //Constructor
    //Contexto (Activity), Lista (obtenermos por WS GET), listener (eventos de los botones)
    public MascotaAdapter(Context context, ArrayList<Mascotas> lista, OnAccionListener listener){
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNombre, txtTipo, txtPeso;

        ImageButton btnEditarMascota, btnEliminarMascota;

        //Vinculacion (Acceso a cada elemento dentro de item_mascota.xml)

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtPeso = itemView.findViewById(R.id.txtPeso);
            btnEditarMascota = itemView.findViewById(R.id.btnEditarMascota);
            btnEliminarMascota = itemView.findViewById(R.id.btnEliminarMascota);
        }
    }

    //1. Inflar = crea el layout para cada  fila

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mascota,parent,false);
        return new ViewHolder(view);
    }

    //2. Llenar los datos
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mascotas mascota = lista.get(position);

        holder.txtNombre.setText(mascota.getNombre());
        holder.txtTipo.setText(mascota.getTipo());
        holder.txtPeso.setText("Peso" + String.valueOf(mascota.getPesokg()) + "Kg.");

        holder.btnEditarMascota.setOnClickListener(v -> {
            listener.onEditar(holder.getAdapterPosition(),mascota);
        });

        holder.btnEliminarMascota.setOnClickListener(v -> {
            listener.onEliminar(holder.getAdapterPosition(),mascota);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    //4. ELiminar un elemento de la lista<Mascota>

    public void eliminarItem(int position){
        lista.remove(position);
        notifyItemRemoved(position);
    }
}
