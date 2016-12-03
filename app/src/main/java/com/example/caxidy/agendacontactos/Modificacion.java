package com.example.caxidy.agendacontactos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Modificacion extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    EditText tNombre, tTelefono, tDir, tEmail, tWeb, tFoto;
    ImageButton bTel, bFot;
    ImageView imVFoto;
    ArrayList<Telefono> listaTelefonos;
    ArrayList<Foto> listaFotos;
    BDContactos bd;
    Contacto contacto;
    private static final int FOTO_GALERIA=1, FOTO_CAMARA = 2;
    Uri fotoGaleria;
    private static OutputStream os;
    private static File ruta;
    private static File ficheroSalida;
    int idC;
    String telefonoPpal, fotoPpal;
    boolean modificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navOpen, R.string.navClos);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tNombre = (EditText) findViewById(R.id.txNombre);
        tTelefono = (EditText) findViewById(R.id.txTelefono);
        tDir = (EditText) findViewById(R.id.txDir);
        tEmail = (EditText) findViewById(R.id.txEmail);
        tWeb = (EditText) findViewById(R.id.txWeb);
        tFoto = (EditText) findViewById(R.id.txFoto);
        bTel = (ImageButton) findViewById(R.id.bAltaTel);
        bFot = (ImageButton) findViewById(R.id.bAltaFoto);
        imVFoto = (ImageView) findViewById(R.id.fotoUsuario);

        modificar=false;
        bd = new BDContactos(this);

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

        //Poner los datos del contacto en la actividad:
        idC = getIntent().getExtras().getInt("codigoModif");
        contacto = bd.consultarContacto(idC);

        tNombre.setText(contacto.getNombre());
        tTelefono.setText(bd.consultarTelefono(idC).getTelefono());
        tDir.setText(contacto.getDireccion());
        tEmail.setText(contacto.getEmail());
        tWeb.setText(contacto.getWeb());
        Foto ft = bd.consultarFoto(idC);
        tFoto.setText(ft.getNombreFichero());
        if(ft!=null) {
            File archivoImg = new File(getExternalFilesDir(null)+"/"+ft.getNombreFichero());
            if(archivoImg.exists()){
                imVFoto = (ImageView) findViewById(R.id.fotoUsuario);
                imVFoto.setImageBitmap(BitmapFactory.decodeFile(archivoImg.getAbsolutePath()));
                imVFoto.setAdjustViewBounds(true);
            }
        }
        telefonoPpal = tTelefono.getText().toString();
        fotoPpal = tFoto.getText().toString();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modificar, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Son las opciones del drawer
        int id = item.getItemId();

        if (id == R.id.itemLlamar) {
            //!!
        } else if (id == R.id.itemEmail) {
            //!!
        } else if (id == R.id.itemWeb){
            //!!
        } else {
            //!!
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.modificar) {
            pasarDatosMod();
            return true;
        }
        else if (id == R.id.borrar){
            borrarContacto();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pasarDatosMod(){
        Intent datos = new Intent();
        modificar=true;

        //En caso de haber metido solo un telefono o una foto y no haber pulsado los botones '+' para añadir otros...
        if(listaTelefonos.isEmpty() && !(tTelefono.getText().toString().equals(""))){
            if(!tTelefono.getText().toString().equals(telefonoPpal)) { //Si el telefono no es el mismo que el principal (es uno nuevo)...
                Telefono tel = new Telefono(tTelefono.getText().toString());
                listaTelefonos.add(tel);
            }
        }
        if(listaFotos.isEmpty() && !(tFoto.getText().toString().equals(""))){
            if(!tFoto.getText().toString().equals(fotoPpal)) {
                copiarArchivo();
                Foto fot = new Foto(tFoto.getText().toString(), getString(R.string.sinD));
                listaFotos.add(fot);
            }
        }

        datos.putExtra("id",idC);
        datos.putExtra("nombre", tNombre.getText().toString());
        datos.putExtra("telefonos", listaTelefonos);
        datos.putExtra("direccion", tDir.getText().toString());
        datos.putExtra("email", tEmail.getText().toString());
        datos.putExtra("web", tWeb.getText().toString());
        datos.putExtra("fotos", listaFotos);
        datos.putExtra("modificar",modificar);
        setResult(RESULT_OK,datos);
        finish();
    }

    public void borrarContacto(){
        modificar=false;
        //!! hacer la transaccion de borrar el contacto d la bd aqui --> trigger...

        Intent datos = new Intent();
        datos.putExtra("modificar",modificar);
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
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),fotoGaleria);
                Bitmap bResized = Bitmap.createBitmap(bm,0,0,imVFoto.getWidth(),imVFoto.getHeight());
                //Poner la foto en el imageView
                imVFoto.setImageBitmap(bResized);
                //Poner la ruta (nombre del fichero) en el edittext
                tFoto.setText(fotoGaleria.getLastPathSegment()+".jpg");
            } catch (IOException e) {}
        }
        else if (requestCode == FOTO_CAMARA && resultCode == RESULT_OK){
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            imVFoto.setImageBitmap(bm);
            tFoto.setText("IMG_AgCont_00"+bd.consultarTotalFotos()+".jpg");
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