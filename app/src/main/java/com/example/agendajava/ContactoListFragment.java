package com.example.agendajava;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agendajava.databinding.FragmentContactoListBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class ContactoListFragment extends Fragment implements OnClickListener{
    private FragmentContactoListBinding binding;
    private ContactoSingleton contactoSingleton;
    private AdaptadorContactos adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactoListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Llamos al la clase, para poder acceder a los métodos de la base de datos
        contactoSingleton = ContactoSingleton.get(requireContext());
        iniciarRecyclerview();
        //Modificamos el nombre en la barra de acciones
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(getString(R.string.lista_contactos));

        binding.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarFragmentContacto(null);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*Cuando mostramos el fragment de contacto, hay dos opciones disponibles (guardar o actualizar), si el método
    * recibe un null quiere decir que vamos a guardar un registro por lo que le enviamos a la vista un -1.
    * Si recibimos algún argumento significa que es ID del contacto para poder ser cargado en los campos, para
    * posteriormente ser actualizado*/
    private void mostrarFragmentContacto(Bundle arg){
        FragmentManager fragmentManager = getParentFragmentManager();
        ContactoFragment contactoFragment = new ContactoFragment();
        if (arg != null){
            contactoFragment.setArguments(arg);
        }else{
            Bundle args = new Bundle();
            args.putInt(getString(R.string.arg_id), -1);
            contactoFragment.setArguments(args);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, contactoFragment)
                .setReorderingAllowed(true)
                .addToBackStack("null") // name can be null
                .commit();
    }

    /*Obtenemos todos los registros de la base de datos y se lo pasamos al adaptador para poder ser mostrado en
    * el recyclerview*/
    private void iniciarRecyclerview(){
        adaptador = new AdaptadorContactos(obtenerContactos(), this);
        binding.recyclerviewLista.setHasFixedSize(true);
        binding.recyclerviewLista.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerviewLista.setAdapter(adaptador);
    }
    /*Obtenemos todoss los registros de la base de dato*/
    private List<ContactoEntity> obtenerContactos(){
        List<ContactoEntity> lista;
        return lista = contactoSingleton.obtenerTodos();

    }

    /*OnClickListener*/
    /*Cuando el usuario da click en algún item del recyclerview, el adaptador nos regresa el ID del contacto
    * para poderlo pasarlo como argumento al fragment de contacto para poder ser cargado en los editText
    * y para poder ser actualizado*/
    @Override
    public void onClick(int ID_agenda) {
        Bundle args = new Bundle();
        args.putInt(getString(R.string.arg_id), ID_agenda);
        mostrarFragmentContacto(args);
    }

    /*Cuando el usurio da un click largo en algún item del recyclerview, el adaptador nos regresa el contacto
    * para posteriormente mostrar un alertDialog indicando si desea eliminar el registro. Si el usuario
    * elimina el registro actualizamos la lista de contactos del adaptador para poder ver los cambios
    * en el recyclerview*/
    @Override
    public boolean eliminarContacto(ContactoEntity contacto) {
       new MaterialAlertDialogBuilder(requireContext())
               .setTitle(getResources().getString(R.string.tituloDialog))
               .setMessage(getResources().getString(R.string.mensajeDialog))
               .setPositiveButton(getString(R.string.confirmarDialog), new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       contactoSingleton.eliminarContacto(contacto);
                       adaptador.eliminarContacto(contacto);
                   }
               })
               .setNegativeButton(getString(R.string.cancelarDialog), null)
               .show();
        return true;
    }
}