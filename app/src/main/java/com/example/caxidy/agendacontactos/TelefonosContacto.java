package com.example.caxidy.agendacontactos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class TelefonosContacto extends AppCompatActivity {
    ListView listaT;
    ArrayList<Telefono> arrayTels;
    AdaptadorTelefonos adp;
    int idContacto;
    BDContactos bd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_telefonos_contacto);

        listaT = (ListView) findViewById(R.id.lista_telefonos);
        arrayTels = new ArrayList<>();

        idContacto = getIntent().getExtras().getInt("idContac");
        bd = new BDContactos(this);

        arrayTels=bd.obtenerTelefonos(idContacto);
        if(arrayTels==null)
            arrayTels = new ArrayList<>();

        adp = new AdaptadorTelefonos(this, arrayTels);
        adp.notifyDataSetChanged();
        listaT.setAdapter(adp);
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        adp = null;
        adp = new AdaptadorTelefonos(this,arrayTels);
        adp.notifyDataSetChanged();
        listaT.setAdapter(adp);
    }

    @Override
    public void onBackPressed(){
        Intent datos = new Intent();
        datos.putExtra("primerTel",bd.consultarTelefono(idContacto).getTelefono());
        setResult(RESULT_OK,datos);
        finish();
    }

    public void actualizarAdaptador(){
        arrayTels=bd.obtenerTelefonos(idContacto);
        if(arrayTels==null)
            arrayTels = new ArrayList<>();
        adp.actualizarDatos(arrayTels);
        listaT.setAdapter(adp);
    }
}
