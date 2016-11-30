/*!! - ojo: el campo de la foto es para el nombre del fichero, por lo que el campo de la tabla Fotos de la descripcion no va
* ahi...--> a lo mejor la descripcion es para el atributo contentDescription del ImageView del xml contacto
* Parte de ListView: queda añadir a MainActivity, etc*/
package com.example.caxidy.agendacontactos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ListActivity {
    BDContactos bd;
    Contacto contacto;
    Telefono telefono;
    Foto foto;
    long numReg;
    private final int SUBACTIVIDAD_ALTA=1;
    private final int SUBACTIVIDAD_ACTUALIZAR=2;
    int totalContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //!!!!!setSupportActionBar(toolbar); --> buscar problema y solucionarlo: compatibilidad entre ActionBar y ListActivity

        //Crear la BD de SQLite
        bd = new BDContactos(this);

        totalContactos=bd.consultarTotalContactos();
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
                }
                else
                    Toast.makeText(this, "Los campos Telefono y Foto no pueden estar vacios", Toast.LENGTH_LONG).show();
            }
            else if(requestCode==SUBACTIVIDAD_ACTUALIZAR){
                //!!tratar los datos de la actividad de borrado, modificacion...
            }
        }
    }

    //metodos que llaman a la BD:

    //!!alta,baja,consulta y modificar Contacto, Telefono, Foto -- faltan las funciones de los botones d agregar otro tel o foto
    public void altaContacto(Intent i) {
        totalContactos++;
        contacto = new Contacto(totalContactos, i.getExtras().get("nombre").toString(), i.getExtras().get("direccion").toString(),
                i.getExtras().get("email").toString(), i.getExtras().get("web").toString());
        numReg = bd.insertarContacto(contacto);
        if (numReg <= 0) {
            Toast.makeText(this, "ERROR : No se ha insertado ningun registro.", Toast.LENGTH_LONG).show();
            totalContactos--;
        } else {
            Toast.makeText(this, "Registro insertado (total: " + numReg + ")", Toast.LENGTH_LONG).show();
            //!!Añadir el nuevo registro al ListView, actualizar el ListView o lo que sea
        }
    }

    public void altaTel(Intent i) { //!!añadir lo necesario para que coja un arrayList con objetos telefono y añada 1 o mas
        int total=0;
        total = bd.consultarTotalTel(totalContactos);
        telefono = new Telefono(total+1, i.getExtras().get("telefono").toString(),totalContactos); //!!comprobar que el Extra de telefono no esta vacio
        numReg = bd.insertarTelefono(telefono);
        if (numReg <= 0) {
            Toast.makeText(this, "ERROR : No se ha insertado ningun registro.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Registro insertado (total: " + numReg + ")", Toast.LENGTH_LONG).show();
            //!!Añadir el nuevo registro al ListView, actualizar el ListView o lo que sea
        }
    }

    public void altaFoto(Intent i) { //!!añadir lo necesario para que coja un arrayList con objetos foto y añada 1 o mas
        int total=0;
        total = bd.consultarTotalFotos(totalContactos);
        foto = new Foto(total+1, i.getExtras().get("descFoto").toString(),"!!!!!!!nombreFich",totalContactos); //!!!
        numReg = bd.insertarFotos(foto);
        if (numReg <= 0) {
            Toast.makeText(this, "ERROR : No se ha insertado ningun registro.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Registro insertado (total: " + numReg + ")", Toast.LENGTH_LONG).show();
            //!!Añadir el nuevo registro al ListView, actualizar el ListView o lo que sea
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
