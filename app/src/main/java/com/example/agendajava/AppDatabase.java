package com.example.agendajava;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/*Se creo un archivo en la ruta assets/database/Contacto.db que se va a encargar de crear la base de datos
* con ese archivo, además de agregar varios registros (para evitar que la applicación arranque vacia). Por eso
* la versión de la base de datos es la primera*/
@Database(entities = {ContactoEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactoDao contactoDao();
}
