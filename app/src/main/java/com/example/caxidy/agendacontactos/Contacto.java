package com.example.caxidy.agendacontactos;

public class Contacto {
    int id;
    String nombre, direccion, email,webBlog;
    //float GPS_x, GPS_y;

    public Contacto(){
        id=0;
        nombre= direccion= email=webBlog="";
        //GPS_x=0;
        //GPS_y=0;
    }
    public Contacto(int id, String nombre, String direccion, String email, String web){
        this.id=id;
        this.nombre=nombre;
        this.direccion=direccion;
        this.email=email;
        this.webBlog=web;
        //this.GPS_x=GPS_x;
        //this.GPS_y=GPS_y;
    }

    public int getID(){return id;}
    public String getNombre(){return nombre;}
    public String getDireccion(){return direccion;}
    public String getEmail(){return email;}
    public String getWeb(){return webBlog;}
    //public float getGPS_x(){return GPS_x;}
    //public float getGPS_y(){return GPS_y;}
}
