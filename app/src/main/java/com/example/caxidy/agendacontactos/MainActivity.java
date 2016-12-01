/*!! - ojo: el campo de la foto es para el nombre del fichero, por lo que el campo de la tabla Fotos de la descripcion no va
* ahi...--> a lo mejor la descripcion es para el atributo contentDescription del ImageView del xml contacto
* SQLite tiene foreign key pero no funciona, por lo que para hacer la funcino de foreign key hay que hacerlo con triggers:
* es decir, al borrar un usuario que borre todos sus telefonos y fotos
* Error al dar de alta*/
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
    int totalContactos;
    AdaptadorContactos adaptadorC;
    ArrayList<Contacto> listaContactos;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        delegate = AppCompatDelegate.create(this,this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);
        //!!si tengo que quitar lo del delegate: setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        delegate.setSupportActionBar(toolbar);

        //Crear la BD de SQLite
        bd = new BDContactos(this);

        totalContactos=bd.consultarTotalContactos();

        listaContactos = new ArrayList<>();
        //Llenar la lista de contactos
        ArrayList<Contacto> arrC= bd.obtenerContactos();
        if(arrC!=null)
            for(int i=0;i<arrC.size();i++)
                listaContactos.add(arrC.get(i));

        adaptadorC = new AdaptadorContactos(this,listaContactos);
        adaptadorC.notifyDataSetChanged();
        setListAdapter(adaptadorC);

        listview = (ListView) findViewById(android.R.id.list);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg)
            {
                Contacto co = (Contacto) listview.getAdapter().getItem(position);
                //!!hacemos lo necesario: abrir activity de modificar...
            }
        });
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        Toast.makeText(this, "Lista de contactos recargada", Toast.LENGTH_SHORT).show();
        adaptadorC = null;
        adaptadorC = new AdaptadorContactos(this,listaContactos);
        adaptadorC.notifyDataSetChanged();
        setListAdapter(adaptadorC);
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
            //!!opcion de preferencias
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //!!borrar este super si da problemas

        if(resultCode==RESULT_OK){
            if(requestCode==SUBACTIVIDAD_ALTA){
                //Alta del contacto y de los telefonos y fotos (minimo un telefono y una foto)
                if((boolean)data.getExtras().get("correcta")) {
                    altaContacto(data);
                    altaTel(data);
                    altaFoto(data);
                    onRestart(); //se actualiza el ListView //!!funciona?
                }
                else
                    Toast.makeText(this, "Los campos Telefono y Foto no pueden estar vacios", Toast.LENGTH_LONG).show();
            }
            else if(requestCode==SUBACTIVIDAD_ACTUALIZAR){
                //!!tratar los datos de la actividad de borrado, modificacion...
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

    //!!alta,baja,consulta y modificar Contacto, Telefono, Foto -- faltan las funciones de los botones d agregar otro tel o foto
    public void altaContacto(Intent i) {
        totalContactos++; //para el indice
        contacto = new Contacto(totalContactos, i.getExtras().get("nombre").toString(), i.getExtras().get("direccion").toString(),
                i.getExtras().get("email").toString(), i.getExtras().get("web").toString());
        numReg = bd.insertarContacto(contacto);
        if (numReg <= 0) {
            Toast.makeText(this, "ERROR : No se ha insertado ningun registro.", Toast.LENGTH_LONG).show();
            totalContactos--;
        } else {
            listaContactos.add(contacto);
            Toast.makeText(this, "Registro insertado (total: " + numReg + ")", Toast.LENGTH_LONG).show();
        }
    }
    //!!si se añade un telefono o foto al modificar no podemos usar totalContactos como idContacto, si no que tenemos que sacar la id del objeto contacto
    public void altaTel(Intent i) {
        int contTel=0;
        int posTel = bd.consultarTotalTel(totalContactos);
        ArrayList<Telefono> arrTel = (ArrayList<Telefono>) i.getExtras().getSerializable("telefonos");
        if(!arrTel.isEmpty()) {
            for(int j=0;j<arrTel.size();j++) {
                posTel++;
                telefono = new Telefono(posTel, arrTel.get(j).getTelefono(), totalContactos);
                numReg = bd.insertarTelefono(telefono);
                if (numReg <= 0) {
                    Toast.makeText(this, "ERROR : registro no insertado", Toast.LENGTH_LONG).show();
                }
                else
                    contTel++;
            }
            Toast.makeText(this, "Registros insertados: "+contTel, Toast.LENGTH_LONG).show();

        }
    }

    public void altaFoto(Intent i) {
        int contF=0;
        int posF = bd.consultarTotalFotos(totalContactos);
        ArrayList<Foto> arrF = (ArrayList<Foto>) i.getExtras().getSerializable("fotos");
        if(!arrF.isEmpty()) {
            for(int j=0;j<arrF.size();j++) {
                posF++;
                foto = new Foto(posF, arrF.get(j).getNombreFichero(),arrF.get(j).getDescripcionFoto(),totalContactos);
                numReg = bd.insertarFotos(foto);
                if (numReg <= 0) {
                    Toast.makeText(this, "ERROR : registro no insertado", Toast.LENGTH_LONG).show();
                }
                else
                    contF++;
            }
            Toast.makeText(this, "Registros insertados: "+contF, Toast.LENGTH_LONG).show();
        }
    }

    /*public void bajaContacto() {
        int id = Integer.parseInt(tId.getText().toString());
        if ( id > 0) {
            //sacamos el contacto de la BD
            c = bd.consultar(id);
            nreg_afectados = bd.borrar(c);
            if (nreg_afectados <= 0) {
                Toast.makeText(this,"No se ha borrado ningun registro.",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Registro borrado.",Toast.LENGTH_LONG).show();
                tId.setText("");
                tNom.setText("");
                tTel.setText("");
                tDir.setText("");
                tEmail.setText("");
                tWeb.setText("");
                tFoto.setText("");
                tX.setText("");
                tY.setText("");
                listadoContactos();
            }
        }
    }

    public void consultaContacto() {
        int id = Integer.parseInt(tId.getText().toString());
        if ( id > 0) {
            c = bd.consultar(id);
            if(c.getID() == -1) {
                Toast.makeText(this,"Registro no Localizado",Toast.LENGTH_LONG).show();
            }
            else {
                tNom.setText(c.getNombre());
                tTel.setText(c.getTelefono());
                tDir.setText(c.getDireccion());
                tEmail.setText(c.getEmail());
                tWeb.setText(c.getWeb());
                tFoto.setText(c.getFoto());
                tX.setText(Float.toString(c.getGPS_x()));
                tY.setText(Float.toString(c.getGPS_y()));
            }
        }
    }

    public void modificaContacto() {
        int id = Integer.parseInt(tId.getText().toString());
        if ( id > 0) {
            c = new Contacto(
                    Integer.parseInt(tId.getText().toString()),
                    tNom.getText().toString(),
                    tTel.getText().toString(),
                    tDir.getText().toString(),
                    tEmail.getText().toString(),
                    tWeb.getText().toString(),
                    tFoto.getText().toString(),
                    Float.parseFloat(tX.getText().toString()),
                    Float.parseFloat(tY.getText().toString()));
            nreg_afectados = bd.modificar(c);
            if (nreg_afectados <= 0) {
                Toast.makeText(this,"ERROR : No se ha modificado el registro",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"Registro modificado.",Toast.LENGTH_LONG).show();
            }
        }
    }
    */
}
