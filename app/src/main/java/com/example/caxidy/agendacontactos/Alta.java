package com.example.caxidy.agendacontactos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import  java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Alta extends AppCompatActivity {

    EditText tNombre, tTelefono, tDir, tEmail, tWeb, tFoto;
    Button bAlta;
    ImageButton bTel, bFot;
    ImageView imVFoto;
    ArrayList<Telefono> listaTelefonos;
    ArrayList<Foto> listaFotos;
    private static final int FOTO_GALERIA=1, FOTO_CAMARA = 2;
    Uri fotoGaleria;
    private static OutputStream os;
    private static File ruta;
    private static File ficheroSalida;
    Calendar calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_layout);

        tNombre = (EditText) findViewById(R.id.txNombre);
        tTelefono = (EditText) findViewById(R.id.txTelefono);
        tDir = (EditText) findViewById(R.id.txDir);
        tEmail = (EditText) findViewById(R.id.txEmail);
        tWeb = (EditText) findViewById(R.id.txWeb);
        tFoto = (EditText) findViewById(R.id.txFoto);
        bAlta = (Button) findViewById(R.id.bAlta);
        bTel = (ImageButton) findViewById(R.id.bAltaTel);
        bFot = (ImageButton) findViewById(R.id.bAltaFoto);
        imVFoto = (ImageView) findViewById(R.id.fotoUsuario);

        bAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDatosAlta();
            }
        });

        bTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDatosNuevoTel();
            }
        });

        bFot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDatosNuevaImg();
            }
        });

        imVFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerFoto();
            }
        });

        listaTelefonos = new ArrayList<>();
        listaFotos = new ArrayList<>();

    }

    @Override
    //Para guardar la informacion necesaria que se pierde al girar la pantalla
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("giroNombre", tNombre.getText().toString());
        savedInstanceState.putString("giroTel", tTelefono.getText().toString());
        savedInstanceState.putString("giroDir", tDir.getText().toString());
        savedInstanceState.putString("giroEmail", tEmail.getText().toString());
        savedInstanceState.putString("giroWeb", tWeb.getText().toString());
        savedInstanceState.putString("giroFoto", tFoto.getText().toString());
        BitmapDrawable drawable = (BitmapDrawable) imVFoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        savedInstanceState.putParcelable("giroImagen", bitmap);
        savedInstanceState.putSerializable("giroListaTel",listaTelefonos);
        savedInstanceState.putSerializable("giroListaF",listaFotos);
        //!!Testear, sobre todoo las listas

    }
    @Override
    //Para recuperar la informacion guardada al girar la pantalla
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tNombre.setText(savedInstanceState.getString("giroNombre"));
        tTelefono.setText(savedInstanceState.getString("giroTel"));
        tDir.setText(savedInstanceState.getString("giroDir"));
        tEmail.setText(savedInstanceState.getString("giroEmail"));
        tWeb.setText(savedInstanceState.getString("giroWeb"));
        tFoto.setText(savedInstanceState.getString("giroFoto"));
        imVFoto.setImageBitmap((Bitmap)savedInstanceState.getParcelable("giroImagen"));
        listaTelefonos.addAll((ArrayList<Telefono>)savedInstanceState.getSerializable("giroListaTel"));
        listaFotos.addAll((ArrayList<Foto>)savedInstanceState.getSerializable("giroListaF"));
    }

    public void pasarDatosAlta(){
        boolean correcta=true;
        Intent datos = new Intent();

        //En caso de haber metido solo un telefono o una foto y no haber pulsado los botones '+' para añadir otros...
        if(listaTelefonos.isEmpty() && !(tTelefono.getText().toString().equals(""))){
            Telefono tel = new Telefono(tTelefono.getText().toString());
            listaTelefonos.add(tel);
        }
        if(listaFotos.isEmpty() && !(tFoto.getText().toString().equals(""))){
            copiarArchivo();
            Foto fot = new Foto(tFoto.getText().toString(),getString(R.string.sinD));
            listaFotos.add(fot);
        }

        //Ahora, si hemos metido al menos una foto y un telefono se agregara el contacto
        if(listaTelefonos.isEmpty() || listaFotos.isEmpty())
            correcta = false;
        else {
            datos.putExtra("nombre", tNombre.getText().toString());
            datos.putExtra("telefonos", listaTelefonos);
            datos.putExtra("direccion", tDir.getText().toString());
            datos.putExtra("email", tEmail.getText().toString());
            datos.putExtra("web", tWeb.getText().toString());
            datos.putExtra("fotos", listaFotos);
        }
        datos.putExtra("correcta",correcta);
        setResult(RESULT_OK,datos);
        finish();
    }

    public void pasarDatosNuevoTel(){
        Telefono tel;
        if(!tTelefono.getText().toString().equals("")) {
            tel = new Telefono(tTelefono.getText().toString());
            listaTelefonos.add(tel);
            Toast.makeText(getApplicationContext(),getString(R.string.telAg),Toast.LENGTH_SHORT).show();
        }
    }

    public void pasarDatosNuevaImg(){
        Foto fot;
        if(!tFoto.getText().toString().equals("")) {
            //Hacer una copia en el directorio externo del proyecto de la foto
            copiarArchivo();
            //Añadir la foto a la lista
            fot = new Foto(tFoto.getText().toString(),getString(R.string.sinD));
            listaFotos.add(fot);
            Toast.makeText(getApplicationContext(),getString(R.string.fotoAg),Toast.LENGTH_SHORT).show();
        }
    }

    //Al pulsar sobre el ImageView para la foto de contacto se hace lo siguiente:
    public void escogerFoto(){
        //Se comprueba la preferencia Galeria VS Camara
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String s2 = sp.getString("tipoFoto","Galeria");

        if(s2.equals("Galeria")){
            Intent intent = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, FOTO_GALERIA);
        }
        else{
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, FOTO_CAMARA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FOTO_GALERIA && resultCode == RESULT_OK) {
            fotoGaleria = data.getData();
            Bitmap bm;
            try {
                //Poner la foto en el imageView
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),fotoGaleria);
                Bitmap bmResized = Bitmap.createScaledBitmap(bm,250,250,true);
                imVFoto.setImageBitmap(bmResized);
                imVFoto.setAdjustViewBounds(true);
                //Poner la ruta (nombre del fichero) en el edittext
                StringBuffer cadenaArchivo= new StringBuffer(fotoGaleria.getLastPathSegment()); //si el nombre del Edittext es muy largo, no coge bien toda la cadena...
                if(cadenaArchivo.length()>22)
                    cadenaArchivo.setLength(22);
                tFoto.setText(cadenaArchivo+".jpg");
            } catch (IOException e) {}
        }
        else if (requestCode == FOTO_CAMARA && resultCode == RESULT_OK){
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            imVFoto.setImageBitmap(bm);
            //Para ponerle la fecha y hora a las fotos sacadas con la camara (para nombre unico)
            calendario = Calendar.getInstance();
            tFoto.setText("AgCont_"+calendario.get(Calendar.YEAR)+calendario.get(Calendar.MONTH)+calendario.get(Calendar.DAY_OF_MONTH)+
                    calendario.get(Calendar.HOUR_OF_DAY)+calendario.get(Calendar.MINUTE)+
                    calendario.get(Calendar.SECOND)+calendario.get(Calendar.MILLISECOND)+".jpg");
        }
    }

    //Se copia el archivo del imageView en la carpeta del proyecto
    public void copiarArchivo(){
        String nomFichero = tFoto.getText().toString();
        if(nomFichero.equals(""))
            Toast.makeText(getApplicationContext(),getString(R.string.fotNomVac),Toast.LENGTH_SHORT).show();
        else {
            if (imVFoto.getDrawable() == null) {
                Toast.makeText(getApplicationContext(),getString(R.string.fotNoSelImg),
                        Toast.LENGTH_LONG).show();
            } else {
                ruta = getExternalFilesDir(null);
                ficheroSalida = new File(ruta, nomFichero);
                try {
                    os = new FileOutputStream(ficheroSalida);
                } catch (FileNotFoundException e) {
                }
                imVFoto.setDrawingCacheEnabled(true);
                Bitmap bm = imVFoto.getDrawingCache();
                try {
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
