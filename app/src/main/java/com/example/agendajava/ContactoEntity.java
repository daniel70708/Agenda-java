package com.example.agendajava;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*Los únicos datos que pueden ser nulos son: fecha y nota, los demás son obligatorios*/
@Entity(tableName = "Contacto")
public class ContactoEntity {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    public int ID_contacto;
    @NotNull
    public String nombre;
    @Nullable
    public String fecha;
    @NotNull
    public String telefono;
    @Nullable
    public String nota;
    @NotNull
    public String rutaImagen;

    public ContactoEntity(int ID_contacto, @NotNull String nombre, @Nullable String fecha, @NotNull String telefono, @Nullable String nota, @NotNull String rutaImagen) {
        this.ID_contacto = ID_contacto;
        this.nombre = nombre;
        this.fecha = fecha;
        this.telefono = telefono;
        this.nota = nota;
        this.rutaImagen = rutaImagen;
    }
}
