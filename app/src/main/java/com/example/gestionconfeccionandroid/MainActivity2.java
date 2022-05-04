package com.example.gestionconfeccionandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    String nombreUsuario, cargoUsuario;
    TextView usuario, cargo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nombreUsuario = getIntent().getStringExtra("nombre");
        cargoUsuario = getIntent().getStringExtra("cargo");

        usuario = findViewById(R.id.txvNombre);
        cargo = findViewById(R.id.txvCargo);

        usuario.setText(nombreUsuario);
        cargo.setText(cargoUsuario);
    }
}