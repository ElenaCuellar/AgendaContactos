package com.example.caxidy.agendacontactos;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AdaptadorFotos extends BaseAdapter {
    private ArrayList<Foto> lista;
    private final Activity actividad;
    BDContactos bd;

    public AdaptadorFotos(Activity a, ArrayList<Foto> v){
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
        View view = ly.inflate(R.layout.foto, null, true);
        final EditText textoFoto = (EditText) view.findViewById(R.id.unaDescr);
        textoFoto.setText(lista.get(position).getDescripcionFoto());
        Button botonMod = (Button) view.findViewById(R.id.unaFBMod);
        Button botonEliminar = (Button) view.findViewById(R.id.unaFBBorrar);
        final int posicionFot = position;

        Foto ft = lista.get(position);
        if(ft!=null) {
            File archivoImg = new File(actividad.getExternalFilesDir(null)+"/"+ft.getNombreFichero());
            if(archivoImg.exists()){
                ImageView im = (ImageView) view.findViewById(R.id.fotoContac);
                im.setImageBitmap(BitmapFactory.decodeFile(archivoImg.getAbsolutePath()));
                im.setAdjustViewBounds(true);
            }
        }

        botonMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modificar la descripcion de la foto
                long regMod = bd.modificarFoto(lista.get(posicionFot),textoFoto.getText().toString());
                if(regMod!=-1)
                    Toast.makeText(actividad,actividad.getString(R.string.fotoModif),Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(actividad,actividad.getString(R.string.fotoErrorModif),Toast.LENGTH_SHORT).show();
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCount()>1){
                    long regMod = bd.borrarUnaFot(lista.get(posicionFot).getId());
                    if(regMod!=-1) {
                        lista.remove(posicionFot);
                        FotosContacto actListaFotos = (FotosContacto) actividad;
                        actListaFotos.actualizarAdaptador();
                        Toast.makeText(actividad, actividad.getString(R.string.unaFotBorrada), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(actividad,actividad.getString(R.string.unaFotBorradaErr),Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(actividad,actividad.getString(R.string.menosdeunafoto),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void actualizarDatos(ArrayList<Foto> nuevaLista){
        lista.clear();
        lista.addAll(nuevaLista);
        notifyDataSetChanged();
    }
}
