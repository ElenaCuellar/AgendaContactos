package com.example.caxidy.agendacontactos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
            //Hacer una copia en el proyecto de la foto
            copiarArchivo();
            //Añadir la foto a la lista
            fot = new Foto(tFoto.getText().toString(),getString(R.string.sinD));
            listaFotos.add(fot);
            Toast.makeText(getApplicationContext(),getString(R.string.fotoAg),Toast.LENGTH_SHORT).show();
        }
    }

    //Al pulsar sobre el ImageView para la foto de contacto se hace lo siguiente:
    public void escogerFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, FOTO_GALERIA);
        //!!Cambiar el metodo despues,cuando tenga lo de las preferencias configuradas, para que de la opcion de camara o galeria
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FOTO_GALERIA && resultCode == RESULT_OK) {
            fotoGaleria = data.getData();
            Bitmap bm;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),fotoGaleria);
                Bitmap bResized = Bitmap.createBitmap(bm,0,0,imVFoto.getWidth(),imVFoto.getHeight()); //Redimensionamos la imagen en el imageView
                //Poner la foto en el imageView
                imVFoto.setImageBitmap(bResized);
                //Poner la ruta (nombre del fichero) en el edittext
                tFoto.setText(fotoGaleria.getLastPathSegment()+".jpg");
            } catch (IOException e) {}
        }
        else if (requestCode == FOTO_CAMARA && resultCode == RESULT_OK){
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            Bitmap bResized = Bitmap.createBitmap(bm,0,0,imVFoto.getWidth(),imVFoto.getHeight());
            imVFoto.setImageBitmap(bResized);
            tFoto.setText(fotoGaleria.getLastPathSegment()+".jpg");
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
