package com.example.agendajava;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactoDao {

    @Query("SELECT * FROM Contacto")
    List<ContactoEntity> obtenerTodos();

    @Query("Select * FROM Contacto WHERE ID_contacto = :ID_contacto")
    ContactoEntity obtenerContacto(Integer ID_contacto);

    @Insert
    void agregarContacto(ContactoEntity...contactos);

    @Update
    void actualizarContacto(ContactoEntity...contactos);

    @Delete
    void eliminarContacto(ContactoEntity...contactos);

}
