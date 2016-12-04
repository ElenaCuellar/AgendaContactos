package com.example.caxidy.agendacontactos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
        final MainActivity mainAct = (MainActivity) actividad;
        bd = mainAct.bd;

        //Llenamos los campos de la vista actual con los datos correspondientes
        ImageButton botonLlamar = (ImageButton) view.findViewById(R.id.listLlamar);
        ImageButton botonSms = (ImageButton) view.findViewById(R.id.listSms);

        TextView tNom= (TextView) view.findViewById(R.id.listNombre);
        tNom.setText(lista.get(position).getNombre());
        final TextView tTel= (TextView) view.findViewById(R.id.listTel);
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

        botonLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:654092398"));
                if (ContextCompat.checkSelfPermission(actividad.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //si el permiso no esta concedido, se hace una peticion
                    ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.CALL_PHONE},1);
                }
                MainActivity mainAct = (MainActivity) actividad;
                mainAct.startActivity(i);
            }
        });

        botonSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("smsto:"+Uri.encode(tTel.getText().toString())));
                i.putExtra("sms_body", actividad.getString(R.string.textoSMS));
                MainActivity mainAct = (MainActivity) actividad;
                mainAct.startActivity(i);
            }
        });

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
