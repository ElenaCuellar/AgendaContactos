package com.example.caxidy.agendacontactos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            "nombre VARCHAR(50), direccion VARCHAR(50), webBlog VARCHAR(100))";

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
            valores.put("webBlog", c.getWeb());
            numReg = db.insert("contactos", null, valores);
        }
        db.close();
        return numReg;
    }

    public long insertarTelefono(Telefono tel) {
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
        /* en este metodo utilizaremos ContentValues */
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
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
        /* en este metodo utilizaremos ContentValues */
            ContentValues valores = new ContentValues();
            valores.put("ifFoto", fot.getId());
            valores.put("nomFichero",fot.getNombreFichero());
            valores.put("observFoto",fot.getDescripcionFoto());
            valores.put("idContacto",fot.getIdContacto());
            numReg = db.insert("fotos", null, valores);
        }
        db.close();
        return numReg;
    }

    /*DELETE de Contactos, Telefonos y Fotos*/

    public long borrarContacto(Contacto c) {
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("contactos", "id=" + c.getID(), null);
        }
        db.close();
        return numReg;
    }

    public long borrarTelefono(Telefono tel) {
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("telefonos", "idTelefonos=" + tel.getId(), null);
        }
        db.close();
        return numReg;
    }

    public long borrarFoto(Foto fot) {
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            numReg = db.delete("fotos", "idFoto=" + fot.getId(), null);
        }
        db.close();
        return numReg;
    }

    /*UPDATE de Contactos, Telefonos y Fotos*/

    public long modificarContacto(Contacto c){
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", c.getNombre());
            valores.put("direccion", c.getDireccion());
            valores.put("webBlog", c.getWeb());
            numReg = db.update("contactos", valores, "id=" +
                    c.getID(), null);
        }
        db.close();
        return numReg;
    }

    public long modificarTelefonos(Telefono tel){
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("telefono", tel.getTelefono());
            valores.put("idContacto", tel.getIdContacto());
            numReg = db.update("telefonos", valores, "idTelefonos=" +
                    tel.getId(), null);
        }
        db.close();
        return numReg;
    }

    public long modificarFotos(Foto fot){
        long numReg = -1;
        /* Abrimos la BD de Escritura */
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("nomFichero", fot.getNombreFichero());
            valores.put("observFoto", fot.getDescripcionFoto());
            valores.put("idContacto", fot.getIdContacto());
            numReg = db.update("fotos", valores, "ifFoto=" +
                    fot.getId(), null);
        }
        db.close();
        return numReg;
    }

    /*SELECT de Contactos, Telefonos y Fotos*/

    public Contacto consultarContacto(int id) {
        /* Abrimos la BD de Lectura */
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"id", "nombre", "direccion", "webBlog"};
            Cursor c = db.query("contactos", campos, "id=" + id, null, null,
                    null, null, null);
            if (c.moveToFirst())
                contacto = new Contacto(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
            c.close();
        }
        db.close();
        return contacto;
    }

    public Telefono consultarTelefono(int id) {
        /* Abrimos la BD de Lectura */
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idTelefonos", "telefonos", "idContacto"};
            Cursor c = db.query("telefonos", campos, "idTelefonos=" + id, null, null,
                    null, null, null);
            if (c.moveToFirst())
                telefono = new Telefono(c.getInt(0), c.getString(1), c.getInt(2));
            c.close();
        }
        db.close();
        return telefono;
    }

    public Foto consultarFoto(int id) {
        /* Abrimos la BD de Lectura */
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] campos = {"idFoto", "nomFichero", "observFoto", "idContacto"};
            Cursor c = db.query("fotos", campos, "idFoto=" + id, null, null,
                    null, null, null);
            if (c.moveToFirst())
                foto = new Foto(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
            c.close();
        }
        db.close();
        return foto;
    }
}
