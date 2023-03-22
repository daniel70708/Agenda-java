package com.example.agendajava;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.agendajava.databinding.FragmentContactoBinding;
import com.google.android.material.snackbar.Snackbar;

public class ContactoFragment extends Fragment {
    private FragmentContactoBinding binding;
    //variable para acceder a las funciones del activityMain
    private AppCompatActivity activity;
    //Clase con la que accedemos a la referencia de la base de datos
    private ContactoSingleton contactoSingleton;
    //variable para saber si se esta editando o guardando
    private boolean esModoEdicion = false;
    //Variable en dlizaronde vamos a almacenar el contacto en el modo de actua
    private ContactoEntity contacto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactoSingleton = ContactoSingleton.get(requireContext());
        //Tomamos los argumentos del fragment lista
        int ID_contacto = getArguments().getInt(getString(R.string.arg_id));
        /*Verificamos si es modo actualizar o guardar (si en los argumentos recibimos un -1, quiere decir que estamos
        * en el modo de guardar y si recibimos cualquier otro número es el ID del contacto*/
        if(ID_contacto!= -1){
            esModoEdicion = true;
            obtenerContacto(ID_contacto);
        }else{
            esModoEdicion = false;
            contacto = new ContactoEntity(0, "", "", "", "", "");
        }
        //Actualizamos el nombre de la barra de acciones dependiendo en que modo se encuentre
        iniciarBarraAcciones();
        //Actualiza la imagen de acuerdo al texto del input imagen o podemos dar click en guardar o actualizar en la base de datos
        actualizarEditTextImagen();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        /*Cuando destruimos la vista regresamos el nombre a Lista de contactos y eliminamos el menú de guardar, así como
        * la flecha para regresar al fragment anterior*/
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setTitle(getString(R.string.lista_contactos));
        setHasOptionsMenu(false);

    }
    /*Actualizamos el nombre de la vista (puede ser para guardar o para actualizar), inflamos el menú de guardar y
    * activamos el menú de regresar*/
    private void iniciarBarraAcciones(){
        activity = (AppCompatActivity) getActivity();

        if (esModoEdicion){
            activity.getSupportActionBar().setTitle(getString(R.string.actualizar_contacto));
        }else{
            activity.getSupportActionBar().setTitle(getString(R.string.agregar_contacto));
        }
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }
    /*Obtenemos el contacto de acuerdo al ID que recibimos como argumento del fragment de la lista y llenamos los
    * editText con todos los campos del contacto para que pueda ser actualizado*/
    private void obtenerContacto(int ID_contacto) {
        contacto = contactoSingleton.obtenerContacto(ID_contacto);
        llenarCampos();
    }

    private void llenarCampos() {
        binding.etNombre.setText(contacto.nombre);
        binding.etBirthday.setText(contacto.fecha);
        binding.etTelefono.setText(contacto.telefono);
        binding.etNotas.setText(contacto.nota);
        binding.etImagen.setText(contacto.rutaImagen);
        cargarImagen(contacto.rutaImagen);
    }

    /*Cuando nos encontremos en el editText de ruta de la imagen, al modificar con una nueva ruta vamos a
    * actualizar el recuadro de la imagen, para que el usuario vea la imagen que esta ingresando. Y al terminar
    * puede dar click en la palomita en el teclado para guardar los cambios*/
    private void actualizarEditTextImagen(){
        binding.etImagen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nuevaRuta = binding.etImagen.getText().toString().trim();
                cargarImagen(nuevaRuta);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etImagen.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    guardarRegistro();
                }
                return false;
            }
        });
    }
    /*Función que carga la imagen, cuando ingresamos una ruta el editText de imagen*/
    private void cargarImagen(String rutaImagen) {
        Glide.with(requireContext())
                .load(rutaImagen)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgFoto);
    }

    //Inflamos la vista el menú de guardar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_guardar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*Acciones cuando el usuario interactue con la actionBar*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            //Regresar al fragment anterior
            case android.R.id.home:
                activity.onBackPressed();
                return true;
                //Guardar o actualizar el contacto
            case R.id.action_guardar:
                guardarRegistro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*Creamos un nuevo contacto con los datos que ingreso el usuario, para validar que no tengamos campos vacíos y
    * dependiendo la acción a realizar guardaremos o actualizaremos. Si es el caso de actualizar le asignamos el ID
    * del contacto antes de actualizar*/
    private void guardarRegistro(){
        ContactoEntity nuevoContacto = new ContactoEntity(0, binding.etNombre.getText().toString().trim(),binding.etBirthday.getText().toString().trim(), binding.etTelefono.getText().toString().trim(), binding.etNotas.getText().toString().trim(), binding.etImagen.getText().toString().trim());
        if (validarCampos()){
            if(esModoEdicion){
                nuevoContacto.ID_contacto = contacto.ID_contacto;
                contactoSingleton.actualizarContacto(nuevoContacto);
                Snackbar.make(binding.getRoot(), R.string.exito_actualizar,Snackbar.LENGTH_LONG).show();
            }else{
                contactoSingleton.agregarContacto(nuevoContacto);
                Snackbar.make(binding.getRoot(), R.string.exito_guardar,Snackbar.LENGTH_LONG).show();
            }
            activity.onBackPressed();
        }
    }

    /*Validamos que los campos de nombre, telefono y ruta imagen no se encuentren vacíos*/
    private boolean validarCampos(){
        boolean esCorrecto = true;
        if(binding.etNombre.getText().toString().trim().isEmpty()){
            binding.etNombre.setError(getString(R.string.obligatorio));
            binding.etNombre.requestFocus();
            esCorrecto = false;
        }

        if(binding.etTelefono.getText().toString().trim().isEmpty()){
            binding.etTelefono.setError(getString(R.string.obligatorio));
            binding.etTelefono.requestFocus();
            esCorrecto = false;
        }

        if(binding.etImagen.getText().toString().trim().isEmpty()){
            binding.etImagen.setError(getString(R.string.obligatorio));
            binding.etImagen.requestFocus();
            esCorrecto = false;
        }

        return esCorrecto;
    }

}