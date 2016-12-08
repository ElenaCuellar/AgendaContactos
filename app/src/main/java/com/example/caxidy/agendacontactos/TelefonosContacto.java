package com.example.caxidy.agendacontactos;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_telefonos_contacto);

        listaT = (ListView) findViewById(R.id.lista_telefonos);
        arrayTels = new ArrayList<>();

        idContacto = getIntent().getExtras().getInt("idContac");
        //!!sacar los telefonos del contacto y pasarlos al arraylist --Z hacer metodo en la clase de la bd

        adp = new AdaptadorTelefonos(this,arrayTels);
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
}
