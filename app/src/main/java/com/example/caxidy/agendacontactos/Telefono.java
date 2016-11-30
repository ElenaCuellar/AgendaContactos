package com.example.caxidy.agendacontactos;

import java.io.Serializable;

public class Telefono implements Serializable {
    int idTel, idContacto;
    String telefono;

    public Telefono(){
        idTel=0;
        idContacto=0;
        telefono="";
    }

    public Telefono(int id, String tel, int idC){
        idTel=id;
        idContacto=idC;
        telefono=tel;
    }

    public Telefono(String tel){
        idTel=0;
        idContacto=0;
        telefono=tel;
    }

    public int getId(){return idTel;}
    public int getIdContacto(){return idContacto;}
    public String getTelefono(){return telefono;}
}
