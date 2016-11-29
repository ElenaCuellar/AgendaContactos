package com.example.caxidy.agendacontactos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Alta extends AppCompatActivity {

    EditText tNombre, tTelefono, tDir, tEmail, tWeb, tFoto;
    Button bAlta, bTel, bFot;
    ArrayList<Telefono> listaTelefonos;
    ArrayList<Foto> listaFotos;

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

        listaTelefonos = new ArrayList<>();
        listaFotos = new ArrayList<>();
    }

    public void pasarDatosAlta(){
        boolean correcta=true;
        String tel = tTelefono.getText().toString(); //!!En vez de esto, puedo ver si los arraylist tienen minimo un registro
        String foto = tFoto.getText().toString();
        Intent datos = new Intent();
        if(tel.equals("") || foto.equals(""))
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
        //!!en este no se llama a finish(), por lo que las altas se producen al darle a Alta del contacto, que se cierra el activity
        //!!La idea aqui y en el de la imagen es simplemente que pase objetos Telefono o Foto a un arraylist, y luego este
        //arraylist se pasa a los datos del intent que se devuelven al mainActivity, y alli se tratan
        //Cada vez que se llama a este metodo (al pulsar en el boton de + correspondiente) se añade un objeto del tipo
        //correspondiente al arraylist de telefonos (o al de fotos) y sale un
        // Toast avisandolo.
        // Al pulsar en alta se añaden los datos de los arraylist en el data del intent
        Telefono tel;
        if(!tTelefono.getText().toString().equals("")) {
            tel = new Telefono(tTelefono.getText().toString());
            listaTelefonos.add(tel);
            Toast.makeText(getApplicationContext(),"Telefono agregado",Toast.LENGTH_SHORT).show();
        }
    }

    public void pasarDatosNuevaImg(){
        //!!en este no se llama a finish(), por lo que las altas se producen al darle a Alta del contacto, q s cierra el activity
        //!!Nombre fichero de la foto: loQPonemosEnElImageView.getPath()???
    }
}
