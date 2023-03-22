package com.example.agendajava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.agendajava.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mostrarFragmentLista();
    }
    //Cuando la applicación se ejecuta, nos muestra el fragmento de la lista de contactos
    public void mostrarFragmentLista(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, ContactoListFragment.class, null)
                .setReorderingAllowed(true)
                .commit();
    }
}