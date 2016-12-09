/*Ampliacion opcional: añadir dos acciones mas al drawer:
* 1)Borrar o modificar los telefonos del contacto concreto -DONE
* 2)Borrar o modificar las fotos del contacto concreto
 Para las fotos, cada item tendra un imageView con la foto, un Textview para añadir un comentario a la foto,
 un boton para agregar el comentario ("Actualizar comentario") y un boton para borrar la foto de la bd.*/

/*Proyecto de fin de trimestre: agenda de contactos + ejercicio 1: poner botones en los elementos del ListView*/

package com.example.caxidy.agendacontactos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ListActivity implements AppCompatCallback {
    private AppCompatDelegate delegate;
    BDContactos bd;
    Contacto contacto;
    Telefono telefono;
    Foto foto;
    long numReg;
    private final int SUBACTIVIDAD_ALTA=1;
    private final int SUBACTIVIDAD_ACTUALIZAR=2;
    int ultimoContacto, ultimoTelefono, ultimaFoto;
    AdaptadorContactos adaptadorC;
    ArrayList<Contacto> listaContactos;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        delegate = AppCompatDelegate.create(this,this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        delegate.setSupportActionBar(toolbar);

        //Crear la BD de SQLite
        bd = new BDContactos(this);

        //Ultima ID de cada tabla
        ultimoContacto=bd.consultarUltimoContacto();
        ultimoTelefono=bd.consultarUltimoTelefono();
        ultimaFoto=bd.consultarUltimaFoto();

        listaContactos = new ArrayList<>();
        llenarLista();

        adaptadorC = new AdaptadorContactos(this,listaContactos);
        adaptadorC.notifyDataSetChanged();
        setListAdapter(adaptadorC);

        listview = (ListView) findViewById(android.R.id.list);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg)
            {
                Contacto co = (Contacto) listview.getAdapter().getItem(position);
                //Abrir la actividad para modificar un contacto
                abrirModificar(co);
            }
        });
    }

    public void abrirModificar(Contacto co){
        Intent i = new Intent(this,Modificacion.class);
        i.putExtra("codigoModif",co.getID());
        startActivityForResult(i,SUBACTIVIDAD_ACTUALIZAR);
    }

    public void llenarLista(){
        listaContactos.clear();
        //Llenar la lista de contactos
        ArrayList<Contacto> arrC= bd.obtenerContactos();
        if(arrC!=null)
            for(int i=0;i<arrC.size();i++)
                listaContactos.add(arrC.get(i));
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        adaptadorC = null;
        adaptadorC = new AdaptadorContactos(this,listaContactos);
        adaptadorC.notifyDataSetChanged();
        setListAdapter(adaptadorC);
        Toast.makeText(this,getString(R.string.listRecarg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nuevoC) {
            //Se lanza el Activity que nos muestra la interfaz para dar de alta
            Intent i = new Intent(this,Alta.class);
            startActivityForResult(i,SUBACTIVIDAD_ALTA);

            return true;
        }
        else if (id == R.id.config){
            //Preferencias
            Intent i = new Intent(this,Preferencias.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==SUBACTIVIDAD_ALTA){
                //Alta del contacto y de los telefonos y fotos (minimo un telefono y una foto)
                if((boolean)data.getExtras().get("correcta")) {
                    altaContacto(data);
                    altaTel(data);
                    altaFoto(data);
                    onRestart(); //se actualiza el ListView
                }
                else
                    Toast.makeText(this,getString(R.string.noVacios), Toast.LENGTH_LONG).show();
            }
            else if(requestCode==SUBACTIVIDAD_ACTUALIZAR){
                //Modificacion del contacto
                if((boolean)data.getExtras().get("modificar"))
                    modificarContacto(data);

                //Si se ha borrado un contacto, solo recargamos el ListView
                llenarLista();
                onRestart();
            }
        }
    }

    //Metodos de AppCompatCallBack
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {}

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {}

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {return null;}

    //metodos que llaman a la BD:

    public void altaContacto(Intent i) {
        ultimoContacto++; //para el indice
        contacto = new Contacto(ultimoContacto, i.getExtras().get("nombre").toString(), i.getExtras().get("direccion").toString(),
                i.getExtras().get("email").toString(), i.getExtras().get("web").toString());
        numReg = bd.insertarContacto(contacto);
        if (numReg == -1) {
            Toast.makeText(this,getString(R.string.errorReg), Toast.LENGTH_LONG).show();
            ultimoContacto--;
        } else {
            listaContactos.add(contacto);
            Toast.makeText(this,getString(R.string.regIns), Toast.LENGTH_LONG).show();
        }
    }

    public void altaTel(Intent i) {
        int contTel=0;
        ArrayList<Telefono> arrTel = (ArrayList<Telefono>) i.getExtras().getSerializable("telefonos");
        if(arrTel!=null) {
            for(int j=0;j<arrTel.size();j++) {
                ultimoTelefono++;
                telefono = new Telefono(ultimoTelefono, arrTel.get(j).getTelefono(), ultimoContacto);
                numReg = bd.insertarTelefono(telefono);
                if (numReg == -1) {
                    ultimoTelefono--;
                    Toast.makeText(this,getString(R.string.errorRegTel), Toast.LENGTH_LONG).show();
                }
                else
                    contTel++;
            }
            Toast.makeText(this, getString(R.string.telIns)+contTel, Toast.LENGTH_LONG).show();
        }
    }

    public void altaFoto(Intent i) {
        int contF=0;
        ArrayList<Foto> arrF = (ArrayList<Foto>) i.getExtras().getSerializable("fotos");
        if(arrF!=null) {
            for(int j=0;j<arrF.size();j++) {
                ultimaFoto++;
                foto = new Foto(ultimaFoto, arrF.get(j).getNombreFichero(),arrF.get(j).getDescripcionFoto(),ultimoContacto);
                numReg = bd.insertarFotos(foto);
                if (numReg == -1) {
                    ultimaFoto--;
                    Toast.makeText(this, getString(R.string.errorRegF), Toast.LENGTH_LONG).show();
                }
                else
                    contF++;
            }
            Toast.makeText(this, getString(R.string.FotIns)+contF, Toast.LENGTH_LONG).show();
        }
    }

    public void modificarContacto(Intent i){
        contacto = new Contacto(Integer.parseInt(i.getExtras().get("id").toString()), i.getExtras().get("nombre").toString(),
                i.getExtras().get("direccion").toString(), i.getExtras().get("email").toString(),
                i.getExtras().get("web").toString());
        numReg = bd.modificarContacto(contacto);
        if (numReg == -1) {
            Toast.makeText(this,getString(R.string.errorModif), Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this,getString(R.string.regModif), Toast.LENGTH_LONG).show();

        //Añadir nuevos telefonos y fotos:
        int contTel=0;
        ArrayList<Telefono> arrTel = (ArrayList<Telefono>) i.getExtras().getSerializable("telefonos");
        if(arrTel!=null) {
            for(int j=0;j<arrTel.size();j++) {
                ultimoTelefono++;
                telefono = new Telefono(ultimoTelefono, arrTel.get(j).getTelefono(), Integer.parseInt(i.getExtras().get("id").toString()));
                numReg = bd.insertarTelefono(telefono);
                if (numReg == -1) {
                    ultimoTelefono--;
                    Toast.makeText(this,getString(R.string.errorRegTel), Toast.LENGTH_LONG).show();
                }
                else
                    contTel++;
            }
            Toast.makeText(this, getString(R.string.telIns)+contTel, Toast.LENGTH_LONG).show();
        }

        int contF=0;
        ArrayList<Foto> arrF = (ArrayList<Foto>) i.getExtras().getSerializable("fotos");
        if(arrF!=null) {
            for(int j=0;j<arrF.size();j++) {
                ultimaFoto++;
                foto = new Foto(ultimaFoto, arrF.get(j).getNombreFichero(),arrF.get(j).getDescripcionFoto(),Integer.parseInt(i.getExtras().get("id").toString()));
                numReg = bd.insertarFotos(foto);
                if (numReg == -1) {
                    ultimaFoto--;
                    Toast.makeText(this, getString(R.string.errorRegF), Toast.LENGTH_LONG).show();
                }
                else
                    contF++;
            }
            Toast.makeText(this, getString(R.string.FotIns)+contF, Toast.LENGTH_LONG).show();
        }
    }
}
