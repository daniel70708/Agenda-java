package com.example.agendajava;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;
import java.util.List;

public class ContactoSingleton {
    public static ContactoSingleton contactoSingleton;
    private ContactoDao contactoDao;

    public ContactoSingleton(Context context) {
        //Instanciamos la base de datos
        Context appContext = context.getApplicationContext();
        AppDatabase database = Room.databaseBuilder(appContext, AppDatabase.class, "Contactos")
                .createFromAsset("database/Contactos.db") //Creamos la base de datos con este archivo y poblamos con algunos registros la base de datos
                .allowMainThreadQueries()
                .build();
        contactoDao = database.contactoDao();
    }


    public static ContactoSingleton get(Context context) {
        if (contactoSingleton == null) {
            contactoSingleton = new ContactoSingleton(context);
        }
        return contactoSingleton;
    }
    //Llamadas a los métdos del DAO (altas, bajas, cambios y obtener registros)
   public List<ContactoEntity> obtenerTodos(){
        return contactoDao.obtenerTodos();
   }

   public ContactoEntity obtenerContacto(int ID_contacto){
        return contactoDao.obtenerContacto(ID_contacto);
   }

   public void agregarContacto(ContactoEntity contacto){
        contactoDao.agregarContacto(contacto);
   }

    public void actualizarContacto(ContactoEntity contacto){
        contactoDao.actualizarContacto(contacto);
    }
    public void eliminarContacto(ContactoEntity contacto){
        contactoDao.eliminarContacto(contacto);
    }
}
