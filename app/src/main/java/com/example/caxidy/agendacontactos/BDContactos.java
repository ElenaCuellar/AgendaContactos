package com.example.caxidy.agendacontactos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BDContactos extends SQLiteOpenHelper {

    private static Contacto contacto;
    private static Telefono telefono;
    private static Foto foto;
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "AgendaContactos.db";
    private static final String NOMBRE_TABLA_CONTACTOS = "Contactos";
    private static final String NOMBRE_TABLA_TELEFONOS = "Telefonos";
    private static final String NOMBRE_TABLA_FOTOS = "Fotos";
    private static final String insC = "CREATE TABLE Contactos (id INT PRIMARY KEY," +
            "nombre VARCHAR(50), direccion VARCHAR(50), email VARCHAR(50), webBlog VARCHAR(100))";

    private static final String insTel = "CREATE TABLE Telefonos (idTelefonos INT PRIMARY KEY," +
            "telefono VARCHAR(45), idContacto INT, " +
            "FOREIGN KEY (idContacto) REFERENCES Contactos(id))";

    private static final String insF = "CREATE TABLE Fotos (idFoto INT PRIMARY KEY," +
            "nomFichero VARCHAR(50), observFoto VARCHAR(255), idContacto INT, " +
            "FOREIGN KEY (idContacto) REFERENCES Contactos(id))";

    public BDContactos(Context context) {
        super(context, NOMBRE_BASEDATOS,null,VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(insC);
        db.execSQL(insTel);
        db.execSQL(insF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_CONTACTOS);
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_TELEFONOS);
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA_FOTOS);

        onCreate(db);
    }

    /*INSERT de Contactos, Telefonos y Fotos*/

    public long insertarContacto(Contacto c) {
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
        /* en este metodo utilizaremos ContentValues */
            ContentValues valores = new ContentValues();
            valores.put("id", c.getID());
            valores.put("nombre", c.getNombre());
            valores.put("direccion", c.getDireccion());
            valores.put("email", c.getEmail());
            valores.put("webBlog", c.getWeb());
            numReg = db.insert("contactos", null, valores);
        }
        db.close();
        return numReg;
    }

    public long insertarTelefono(Telefono tel) {
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("idTelefonos", tel.getId());
            valores.put("idContacto",tel.getIdContacto());
            valores.put("telefono",tel.getTelefono());
            numReg = db.insert("telefonos", null, valores);
        }
        db.close();
        return numReg;
    }

    public long insertarFotos(Foto fot) {
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("idFoto", fot.getId());
            valores.put("nomFichero",fot.getNombreFichero());
            valores.put("observFoto",fot.getDescripcionFoto());
            valores.put("idContacto",fot.getIdContacto());
            numReg = db.insert("fotos", null, valores);
        }
        db.close();
        return numReg;
    }

    /*DELETE de Contactos, Telefonos y Fotos*/

    public long borrarContacto(int idC) {
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("contactos", "id=" + idC, null);
        }
        db.close();
        return numReg;
    }

    public long borrarTelefonos(int idContacto) {
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("telefonos", "idContacto=" + idContacto, null);
        }
        db.close();
        return numReg;
    }

    public long borrarFotos(int idContacto) {
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("fotos", "idContacto=" + idContacto, null);
        }
        db.close();
        return numReg;
    }

    public long borrarUntel(int idTel){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("telefonos", "idTelefonos=" + idTel, null);
        }
        db.close();
        return numReg;
    }

    public long borrarUnaFot(int idFot){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("fotos", "idFoto=" + idFot, null);
        }
        db.close();
        return numReg;
    }

    /*UPDATE de Contactos, Telefonos y Fotos*/

    public long modificarContacto(Contacto c){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", c.getNombre());
            valores.put("direccion", c.getDireccion());
            valores.put("email", c.getEmail());
            valores.put("webBlog", c.getWeb());
            numReg = db.update("contactos", valores, "id=" +
                    c.getID(), null);
        }
        db.close();
        return numReg;
    }

    public long modificarTelefono(Telefono t, String tel){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("telefono", tel);
            numReg = db.update("telefonos", valores, "idTelefonos=" + t.getId(), null);
        }
        db.close();
        return numReg;
    }

    public long modificarFoto(Foto f, String descripcion){
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("observFoto", descripcion);
            numReg = db.update("fotos", valores, "idFoto=" + f.getId(), null);
        }
        db.close();
        return numReg;
    }

    /*SELECT de Contactos, Telefonos y Fotos*/

    public Contacto consultarContacto(int id) {
        contacto=null;
        /* Abrimos la BD de Lectura */
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"id", "nombre", "direccion", "email", "webBlog"};
            Cursor c = db.query("contactos", campos, "id=" + id, null, null,
                    null, null, null);
            if (c.moveToFirst())
                contacto = new Contacto(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
            c.close();
        }
        db.close();
        return contacto;
    }

    //Devolver todos los contactos de la tabla Contactos
    public ArrayList<Contacto> obtenerContactos(){
        ArrayList<Contacto> arrayContactos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"id", "nombre", "direccion", "email", "webBlog"};
            Cursor c = db.query("contactos", campos,null, null, null,
                    null, null, null);
            if (c.moveToFirst())
                do {
                    contacto = new Contacto(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                    arrayContactos.add(contacto);
                }while(c.moveToNext());
            c.close();
        }
        db.close();
        if(arrayContactos.size()>0)
            return arrayContactos;
        else
            return null;
    }

    public ArrayList<Telefono> obtenerTelefonos(int idContacto){
        ArrayList<Telefono> arrayT = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idTelefonos","telefono","idContacto"};
            Cursor c = db.query("telefonos", campos,"idContacto="+idContacto, null, null,
                    null, null, null);
            if (c.moveToFirst())
                do {
                    telefono = new Telefono(c.getInt(0), c.getString(1), c.getInt(2));
                    arrayT.add(telefono);
                }while(c.moveToNext());
            c.close();
        }
        db.close();
        if(arrayT.size()>0)
            return arrayT;
        else
            return null;
    }

    public ArrayList<Foto> obtenerFotos(int idContacto){
        ArrayList<Foto> arrayF = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idFoto","nomFichero","observFoto","idContacto"};
            Cursor c = db.query("fotos", campos,"idContacto="+idContacto, null, null,
                    null, null, null);
            if (c.moveToFirst())
                do {
                    foto = new Foto(c.getInt(0), c.getString(1),c.getString(2), c.getInt(3));
                    arrayF.add(foto);
                }while(c.moveToNext());
            c.close();
        }
        db.close();
        if(arrayF.size()>0)
            return arrayF;
        else
            return null;
    }

    public Telefono consultarTelefono(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idTelefonos", "telefono", "idContacto"};
            Cursor c = db.query("telefonos", campos, "idContacto=" + id, null, null,
                    null, null, null);
            if (c.moveToFirst())
                telefono = new Telefono(c.getInt(0), c.getString(1), c.getInt(2));
            c.close();
        }
        db.close();
        return telefono;
    }

    public Foto consultarFoto(int id) {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idFoto", "nomFichero", "observFoto", "idContacto"};
            Cursor c = db.query("fotos", campos, "idContacto=" + id, null, null,
                    null, null, null);
            if (c.moveToFirst())
                foto = new Foto(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
            c.close();
        }
        db.close();
        return foto;
    }

    /*Consulta el ultimo registro*/

    public int consultarUltimoContacto(){
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"id"};
            Cursor c = db.query("contactos", campos, null, null, null,
                    null, "id DESC");
            if (c.moveToFirst())
                return c.getInt(0);
            c.close();
        }
        db.close();
        return -1;
    }

    public int consultarUltimoTelefono(){
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idTelefonos"};
            Cursor c = db.query("telefonos", campos, null, null, null,
                    null, "idTelefonos DESC");
            if (c.moveToFirst())
                return c.getInt(0);
            c.close();
        }
        db.close();
        return -1;
    }

    public int consultarUltimaFoto(){
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idFoto"};
            Cursor c = db.query("fotos", campos, null, null, null,
                    null, "idFoto DESC");
            if (c.moveToFirst())
                return c.getInt(0);
            c.close();
        }
        db.close();
        return -1;
    }

}
