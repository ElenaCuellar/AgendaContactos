package com.example.caxidy.agendacontactos;

public class Foto {
    int idFoto, idContacto;
    String nombreFichero,descripcionFoto;

    public Foto(){
        idFoto=0;
        idContacto=0;
        nombreFichero=descripcionFoto="";
    }

    public Foto(int id, String nom,String desc,int idC){
        idFoto=id;
        idContacto=idC;
        nombreFichero=nom;
        descripcionFoto=desc;
    }

    public Foto(String nom,String desc,int idC){
        idFoto=0;
        idContacto=0;
        nombreFichero=nom;
        descripcionFoto=desc;
    }

    public int getId(){return idFoto;}
    public int getIdContacto(){return idContacto;}
    public String getNombreFichero(){return nombreFichero;}
    public String getDescripcionFoto(){return descripcionFoto;}
}
