package com.example.caxidy.agendacontactos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AdaptadorTelefonos extends BaseAdapter {

    private ArrayList<Telefono> lista;
    private final Activity actividad;

    public AdaptadorTelefonos(Activity a, ArrayList<Telefono> v){
        super();
        this.lista = v;
        this.actividad = a;
    }

    @Override
    public int getCount() {
        return lista.size();
    }
    @Override
    public Object getItem(int arg0) {
        return lista.get(arg0);
    }
    @Override
    public long getItemId(int arg0) {
        return lista.get(arg0).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.telefono, null, true);
        EditText textoTel = (EditText) view.findViewById(R.id.unTelefono);
        textoTel.setText(lista.get(position).getTelefono());
        Button botonMod = (Button) view.findViewById(R.id.unTelBMod);
        Button botonEliminar = (Button) view.findViewById(R.id.unTelBBorrar);

        botonMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //!!!
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //!!!!
            }
        });
        return view;
    }
}
