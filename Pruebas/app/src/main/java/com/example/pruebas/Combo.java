package com.example.pruebas;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Combo extends Activity {
    Spinner comboNC;
    EditText et1;

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.vista);

        comboNC = findViewById(R.id.comboopciones);

        String colores [] = {"Rojo","Verde","Azul"};

        ArrayAdapter<String> ad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                colores
        );

        comboNC.setAdapter(ad);

        comboNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarColor();
            }
        });

        et1 = findViewById(R.id.textocambio);

    }

    public void cambiarColor(){
        comboNC.getSelectedItemPosition();
    }
}