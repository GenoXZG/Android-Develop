package com.example.pruebas; // Asegúrate de que coincida con el paquete de tu proyecto

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    private EditText campoTexto;
    private Spinner spinnerColores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista);
        campoTexto = findViewById(R.id.textocambio);
        spinnerColores = findViewById(R.id.comboopciones);

        configurarSpinner();
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_colores,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerColores.setAdapter(adapter);

        spinnerColores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cambiarColorTexto(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void cambiarColorTexto(int posicionSeleccionada) {
        int colorResId;

        switch (posicionSeleccionada) {
            case 0: // Rojo
                colorResId = R.color.colorRojo;
                break;
            case 1: // Verde
                colorResId = R.color.colorVerde;
                break;
            case 2: // Azul
                colorResId = R.color.colorAzul;
                break;
            default:
                colorResId = R.color.black;
                break;
        }
        campoTexto.setTextColor(ContextCompat.getColor(this, colorResId));
    }
}