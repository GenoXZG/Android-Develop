package com.example.calculadora;
// Alumno: Zarate Gonzalez Luis David
// Materia: Programacion Movil
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Calculadora extends AppCompatActivity {

    private TextView tvPantalla;
    private double operando1 = 0;
    private double operando2 = 0;
    private String operadorActual = "";
    private boolean esperarNuevoNumero = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        tvPantalla = findViewById(R.id.tvPantalla);
        configurarBotones();
    }

    private void configurarBotones() {

        View.OnClickListener listenerNumeros = v -> {
            Button botonPresionado = (Button) v;
            String numeroBoton = botonPresionado.getText().toString();

            if (esperarNuevoNumero) {
                tvPantalla.setText(numeroBoton);
                esperarNuevoNumero = false;
            } else {
                tvPantalla.append(numeroBoton);
            }
        };
        findViewById(R.id.btnPorcentaje).setOnClickListener(v -> tvPantalla.setText("0"));
        findViewById(R.id.btnSigno).setOnClickListener(v -> tvPantalla.setText("0"));


        int[] idsNumeros = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : idsNumeros) {
            findViewById(id).setOnClickListener(listenerNumeros);
        }

        findViewById(R.id.btnPunto).setOnClickListener(v -> {
            if (esperarNuevoNumero) {
                tvPantalla.setText("0.");
                esperarNuevoNumero = false;
            } else if (!tvPantalla.getText().toString().contains(".")) {
                tvPantalla.append(".");
            }
        });


        View.OnClickListener listenerOperadores = v -> {
            Button botonPresionado = (Button) v;

            if(!tvPantalla.getText().toString().isEmpty() && !tvPantalla.getText().toString().equals(".")) {
                if(!operadorActual.isEmpty() && !esperarNuevoNumero) {
                    calcularResultado();
                }
                operando1 = Double.parseDouble(tvPantalla.getText().toString());
                operadorActual = botonPresionado.getText().toString();
                esperarNuevoNumero = true;
            }
        };

        findViewById(R.id.btnSuma).setOnClickListener(listenerOperadores);
        findViewById(R.id.btnResta).setOnClickListener(listenerOperadores);
        findViewById(R.id.btnMultiplicacion).setOnClickListener(listenerOperadores);
        findViewById(R.id.btnDivision).setOnClickListener(listenerOperadores);

        findViewById(R.id.btnIgual).setOnClickListener(v -> calcularResultado());

        findViewById(R.id.btnLimpiar).setOnClickListener(v -> limpiarCalculadora());

        findViewById(R.id.btnBorrar).setOnClickListener(v -> {
            String textoActual = tvPantalla.getText().toString();
            if (textoActual.length() > 0 && !esperarNuevoNumero) {
                textoActual = textoActual.substring(0, textoActual.length() - 1);
                tvPantalla.setText(textoActual.isEmpty() ? "0" : textoActual);
                if(textoActual.isEmpty()) esperarNuevoNumero = true;
            }
        });
    }

    private void calcularResultado() {
        if (operadorActual.isEmpty() || esperarNuevoNumero) return; // Validación anti-crasheo

        operando2 = Double.parseDouble(tvPantalla.getText().toString());
        double resultado = 0;

        switch (operadorActual) {
            case "+": resultado = operando1 + operando2; break;
            case "-": resultado = operando1 - operando2; break;
            case "*": resultado = operando1 * operando2; break;
            case "/":
                if(operando2 == 0) {
                    tvPantalla.setText("Error");
                    esperarNuevoNumero = true;
                    operadorActual = "";
                    return;
                }
                resultado = operando1 / operando2;
                break;
        }

        if(resultado == (long) resultado) {
            tvPantalla.setText(String.format("%d", (long) resultado));
        } else {
            tvPantalla.setText(String.valueOf(resultado));
        }

        operadorActual = "";
        esperarNuevoNumero = true;
        operando1 = resultado;
    }

    private void limpiarCalculadora() {
        tvPantalla.setText("0");
        operando1 = 0;
        operando2 = 0;
        operadorActual = "";
        esperarNuevoNumero = true;
    }
}