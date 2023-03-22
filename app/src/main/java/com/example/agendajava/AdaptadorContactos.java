package com.example.agendajava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdaptadorContactos extends RecyclerView.Adapter<AdaptadorContactos.ViewHolder> {
    //Contexto que vamos a necesitar para poder colocar la imagen con glide
    private Context context;
    public List<ContactoEntity> listaContactos;
    private static OnClickListener listener;

    /*Constructor de la clase en el cual recibimos la lista de contactos que se van a mostrar en el
    * recyclerView y el listener para poder actualizar o eliminar un contacto*/
    public AdaptadorContactos(List<ContactoEntity> listaContactos, OnClickListener listener) {
        this.listaContactos = listaContactos;
        this.listener = listener;
    }

    //Inflamos la vista de item_agenda sin personalizar
    @NonNull
    @Override
    public AdaptadorContactos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_contacto, parent, false);
        return new ViewHolder(view);
    }

    //Remplazamos el contenido de la vista item_contacto de acuerdo a al contenido de listaAgenda de acuerdo a su posición
    @Override
    public void onBindViewHolder(@NonNull AdaptadorContactos.ViewHolder holder, int position) {
        ContactoEntity contacto = listaContactos.get(position);
        holder.setListener(contacto);
        holder.nombre.setText(contacto.nombre);
        holder.fecha.setText(contacto.fecha);
        holder.telefono.setText(contacto.telefono);
        holder.nota.setText(contacto.nota);
        Glide.with(context)
                .load(contacto.rutaImagen)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.imagen);
    }

    //Regresa el tamaño de la listaContactos
    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    /*Clase que trabaja junto al adaptador y define como se muestran los datos individualmente
    * Nota.-(No tiene el viewBinding)*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imagen;
        public TextView nombre;
        public TextView fecha;
        public TextView telefono;
        public TextView nota;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.item_imgPhoto);
            nombre = itemView.findViewById(R.id.item_tvNombre);
            fecha = itemView.findViewById(R.id.item_tvFecha);
            telefono = itemView.findViewById(R.id.item_tvTelefono);
            nota = itemView.findViewById(R.id.item_tvNotas);
        }

       public boolean setListener(ContactoEntity contacto) {
           //clickListener para porder visualizar los datos a actualizar en el fragment de Contacto
           itemView.setOnClickListener(v -> listener.onClick(contacto.ID_contacto));
           //clickListener para poder eliminar el registro con un click largo
           itemView.setOnLongClickListener(v -> listener.eliminarContacto(contacto));
           return true;
       }

    }

    /*Funciones con la que vamos a modificar a listaContacto, de acuerdo si se agrego un
     * nuevo contacto, modifico o elimino para que se actualice la lista en el recyclerview */
    public void agregarContacto(ContactoEntity contacto){
        if(!listaContactos.contains(contacto)){
            listaContactos.add(contacto);
            notifyItemInserted(listaContactos.size()-1);
        }
    }

    public void actualizarContacto(ContactoEntity contacto){
        int posicion = listaContactos.indexOf(contacto);
        if(posicion!= -1){
            listaContactos.set(posicion, contacto);
            notifyItemChanged(posicion);
        }
    }

    public void eliminarContacto(ContactoEntity contacto){
        int posicion = listaContactos.indexOf(contacto);
        if(posicion!= -1){
            listaContactos.remove(posicion);
            notifyItemRemoved(posicion);
        }
    }
}
