package com.example.caxidy.agendacontactos;

import android.app.Activity;
import android.graphics.Bitmap;
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
    BDContactos bd;

    public AdaptadorContactos(Activity a, ArrayList<Contacto> v){
        super();
        this.lista = v;
        this.actividad = a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.contacto, null, true);

        //Obtenemos la clase que gestiona la BD para generar consultas, etc
        MainActivity mainAct = (MainActivity) actividad;
        bd = mainAct.bd;

        //Llenamos los campos de la vista actual con los datos correspondientes
        TextView tNom= (TextView) view.findViewById(R.id.listNombre);
        tNom.setText(lista.get(position).getNombre());
        TextView tTel= (TextView) view.findViewById(R.id.listTel);
        Telefono tl = bd.consultarTelefono(lista.get(position).getID());
        if(tl!=null)
            tTel.setText(tl.getTelefono());
        else
            tTel.setText("");

        TextView tDir= (TextView) view.findViewById(R.id.listDir);
        tDir.setText(lista.get(position).getDireccion());
        TextView tEmail= (TextView) view.findViewById(R.id.listEmail);
        tEmail.setText(lista.get(position).getEmail());
        TextView tWeb= (TextView) view.findViewById(R.id.listWeb);
        tWeb.setText(lista.get(position).getWeb());

        Foto ft = bd.consultarFoto(lista.get(position).getID());
        if(ft!=null) {
            File archivoImg = new File(actividad.getExternalFilesDir(null)+"/"+ft.getNombreFichero());
            if(archivoImg.exists()){
                ImageView im = (ImageView) view.findViewById(R.id.listFoto);
                im.setImageBitmap(BitmapFactory.decodeFile(archivoImg.getAbsolutePath()));
                im.setAdjustViewBounds(true);
            }
        }

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
