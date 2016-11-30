package com.example.caxidy.agendacontactos;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class AdaptadorContactos extends BaseAdapter {

    private ArrayList<Contacto> lista;
    private final Activity actividad;

    public AdaptadorContactos(Activity a, ArrayList<Contacto> v){
        super();
        this.lista = v;
        this.actividad = a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.contacto, null, true);
        TextView tNom= (TextView) view.findViewById(R.id.listNombre);
        tNom.setText(lista.get(position).getNombre());
        TextView tTel= (TextView) view.findViewById(R.id.listTel);
        tTel.setText(lista.get(position).getID()); //!!--> con la id del contacto hacemos una consulta que saque el primer telefono q coincida con la idContacto y lo ponemos en el texto
        //!!.....llenamos el resto de campos

        /*!!Para la foto:
        File imgFile = new File(lista.get(position).getFoto());
        if(imgFile.exists()){
            ImageView im = (ImageView) view.findViewById(R.id.ivItem);
            im.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            im.setAdjustViewBounds(true);
        }*/
        return view;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getID();
    }
}
