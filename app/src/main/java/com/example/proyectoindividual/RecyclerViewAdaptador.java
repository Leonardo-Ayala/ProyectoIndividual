package com.example.proyectoindividual;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoindividual.Producto;
import com.example.proyectoindividual.R;

import java.util.List;

public class RecyclerViewAdaptador extends RecyclerView.Adapter<RecyclerViewAdaptador.ViewHolder> implements View.OnClickListener {

    public List<Producto> ProductoList;
    Activity activity;
    private View.OnClickListener listener;

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView marca , tipo , stock;
        private ImageView imagenProducto;

        public ViewHolder( View itemView) {
            super(itemView);
            marca =(TextView)itemView.findViewById(R.id.textViewMarca);
            tipo =(TextView)itemView.findViewById(R.id.textViewTipo);
            stock =(TextView)itemView.findViewById(R.id.textViewStock);
            imagenProducto =(ImageView)itemView.findViewById(R.id.imageViewProducto);

        }
    }


    public RecyclerViewAdaptador(List<Producto> ProductoList , Activity activity) {
        this.ProductoList = ProductoList;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.marca.setText(ProductoList.get(position).getMarca());
        holder.tipo.setText(ProductoList.get(position).getTipo());
        holder.stock.setText(ProductoList.get(position).getStock());
        Glide.with(activity).load(ProductoList.get(position).getImagenProducto()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.imagenProducto);
    }

    @Override
    public int getItemCount() {
        return ProductoList.size();
    }
}
