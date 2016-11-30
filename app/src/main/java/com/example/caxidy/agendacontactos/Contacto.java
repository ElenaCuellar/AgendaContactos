package com.example.caxidy.agendacontactos;

import java.io.Serializable;

public class Contacto implements Serializable{
    int id;
    String nombre, direccion, email,webBlog;

    public Contacto(){
        id=0;
        nombre= direccion= email=webBlog="";
    }
    public Contacto(int id, String nombre, String direccion, String email, String web){
        this.id=id;
        this.nombre=nombre;
        this.direccion=direccion;
        this.email=email;
        this.webBlog=web;
    }

    public int getID(){return id;}
    public String getNombre(){return nombre;}
    public String getDireccion(){return direccion;}
    public String getEmail(){return email;}
    public String getWeb(){return webBlog;}
}
