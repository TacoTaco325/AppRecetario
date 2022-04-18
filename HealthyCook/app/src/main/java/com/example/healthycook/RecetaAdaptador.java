package com.example.healthycook;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecetaAdaptador extends RecyclerView.Adapter<RecetaAdaptador.ViewHolder> {

    private Context Recetacontext;
    public ArrayList<Receta> RecetaLista;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public RecetaAdaptador(Context contexto, ArrayList<Receta> recetaLista) {
        Recetacontext = contexto;
        RecetaLista = recetaLista;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Nombre, Calorias, ID;
        ImageView imgReceta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ID = itemView.findViewById(R.id.txtID);
            Nombre = itemView.findViewById(R.id.txtNombre);
            Calorias = itemView.findViewById(R.id.txtCalorias);
            imgReceta = itemView.findViewById(R.id.imgReceta);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener !=null){
                        int position =getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ID.setText(RecetaLista.get(position).getId());
        holder.Nombre.setText(RecetaLista.get(position).getNombre());
        holder.Calorias.setText("Calories: "+RecetaLista.get(position).getCalorias());
        Picasso.with(Recetacontext).load(RecetaLista.get(position).getImagen()).into(holder.imgReceta);
    }

    @Override
    public int getItemCount() {
        return RecetaLista.size();
    }
}
