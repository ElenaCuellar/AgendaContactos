package com.example.caxidy.agendacontactos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class FotosContacto extends AppCompatActivity {
    ListView listaF;
    ArrayList<Foto> arrayFot;
    AdaptadorFotos adp;
    int idContacto;
    BDContactos bd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_fotos_contacto);

        listaF = (ListView) findViewById(R.id.lista_fotos);
        arrayFot = new ArrayList<>();

        idContacto = getIntent().getExtras().getInt("idContacF");
        bd = new BDContactos(this);

        arrayFot=bd.obtenerFotos(idContacto);
        if(arrayFot==null)
            arrayFot = new ArrayList<>();

        adp = new AdaptadorFotos(this, arrayFot);
        adp.notifyDataSetChanged();
        listaF.setAdapter(adp);
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        adp = null;
        adp = new AdaptadorFotos(this,arrayFot);
        adp.notifyDataSetChanged();
        listaF.setAdapter(adp);
    }

    @Override
    public void onBackPressed(){
        Intent datos = new Intent();
        datos.putExtra("primeraFoto",bd.consultarFoto(idContacto).getNombreFichero());
        setResult(RESULT_OK,datos);
        finish();
    }

    public void actualizarAdaptador(){
        arrayFot=bd.obtenerFotos(idContacto);
        if(arrayFot==null)
            arrayFot = new ArrayList<>();
        adp.actualizarDatos(arrayFot);
        listaF.setAdapter(adp);
    }
}
