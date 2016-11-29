package com.example.caxidy.agendacontactos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    BDContactos bd;
    Contacto contacto;
    Telefono telefono;
    Foto foto;
    long numReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Crear la BD de SQLite
        bd = new BDContactos(this);
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
            //!!opcion de alta
            return true;
        }
        else if (id == R.id.config){
            //!!opcion de preferencias
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //metodos que llaman a la BD:

    /*public void altaContacto() {
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
        nreg_afectados = bd.insertar(c);
        if (nreg_afectados <= 0) {
            Toast.makeText(this,"ERROR : No se ha insertado ningun registro.",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Registro agregado (" + nreg_afectados + ")", Toast.LENGTH_LONG).show();
            listadoContactos();
        }
    }
    public void bajaContacto() {
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
