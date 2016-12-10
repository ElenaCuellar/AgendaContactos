package com.example.caxidy.agendacontactos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorTelefonos extends BaseAdapter {

    private ArrayList<Telefono> lista;
    private final Activity actividad;
    BDContactos bd;

    public AdaptadorTelefonos(Activity a, ArrayList<Telefono> v){
        super();
        this.lista = v;
        this.actividad = a;
        bd = new BDContactos(this.actividad);
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
        final EditText textoTel = (EditText) view.findViewById(R.id.unTelefono);
        textoTel.setText(lista.get(position).getTelefono());
        Button botonMod = (Button) view.findViewById(R.id.unTelBMod);
        Button botonEliminar = (Button) view.findViewById(R.id.unTelBBorrar);
        final int posicionTel = position;

        botonMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modificar el numero de telefono indicado
                long regMod = bd.modificarTelefono(lista.get(posicionTel),textoTel.getText().toString());
                if(regMod!=-1)
                    Toast.makeText(actividad,actividad.getString(R.string.telefonoModif),Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(actividad,actividad.getString(R.string.telefonoErrorModif),Toast.LENGTH_SHORT).show();
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCount()>1){
                    long regMod = bd.borrarUntel(lista.get(posicionTel).getId());
                    if(regMod!=-1) {
                        lista.remove(posicionTel);
                        //Tenemos que actualizar los datos de los elementos de la lista
                        TelefonosContacto actListaTel = (TelefonosContacto) actividad;
                        actListaTel.actualizarAdaptador();
                        Toast.makeText(actividad, actividad.getString(R.string.unTelBorrado), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(actividad,actividad.getString(R.string.unTelBorradoErr),Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(actividad,actividad.getString(R.string.menosdeuntel),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void actualizarDatos(ArrayList<Telefono> nuevaLista){
        lista.clear();
        lista.addAll(nuevaLista);
        notifyDataSetChanged();
    }

}
