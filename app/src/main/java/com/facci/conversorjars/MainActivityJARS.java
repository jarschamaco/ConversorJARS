package com.facci.conversorjars;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityJARS extends AppCompatActivity {
    final String[] datos = new String[]{"Dólar","Euro","Peso Mexicano"};
    private Spinner SmonedaActual;
    private Spinner SmonedaCambio;
    private EditText Cambioet;
    private TextView tvresultado;

    final private double FactorDolarEuro = 0.87;
    final private double FactorPesoDolar=0.54;
    //final private double FactorDolarPeso=0.05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_jars);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,datos);
        SmonedaActual=(Spinner)findViewById(R.id.SmonedaActual);
        SmonedaActual.setAdapter(adaptador);
        SmonedaCambio=(Spinner)findViewById(R.id.SmonedaCambio);
        SharedPreferences preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String tmpMonedaActual=preferencias.getString("monedaactual","");
        String tmpMonedaCambio=preferencias.getString("monedacambio","");
        if (!tmpMonedaActual.equals("")){
            int indice= adaptador.getPosition(tmpMonedaActual);
            SmonedaActual.setSelection(indice);
        }
        if (!tmpMonedaCambio.equals("")){
            int indice= adaptador.getPosition(tmpMonedaCambio);
            SmonedaCambio.setSelection(indice);
        }
    }
    public void ClickConvertirMoneda(View v){
        SmonedaActual=(Spinner)findViewById(R.id.SmonedaActual);
        SmonedaCambio=(Spinner)findViewById(R.id.SmonedaCambio);
        Cambioet=(EditText)findViewById(R.id.Cambioet);
        tvresultado=(TextView)findViewById(R.id.tvresultado);
        String monedaactual = SmonedaActual.getSelectedItem().toString();
        String monedacambio = SmonedaCambio.getSelectedItem().toString();
        double valorCambio = Double.parseDouble(Cambioet.getText().toString());
        double Resultado = ProcesarConversion(monedaactual,monedacambio, valorCambio);
        if (Resultado>0){
            tvresultado.setText(String.format("Por %5.2f %s, usted recibira %5.2f %s",valorCambio,monedaactual,Resultado,monedacambio));
            Cambioet.setText("");
            SharedPreferences preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("monedaactual",monedaactual);
            editor.putString("monedacambio",monedacambio);
            editor.commit();

        }else{
            tvresultado.setText(String.format("Usted recibira ..."));
            Toast.makeText(MainActivityJARS.this,"Las opciones elegidas no tienen un factor de conversión ",Toast.LENGTH_LONG).show();

        }
    }
    private double ProcesarConversion(String monedaactual, String monedacambio, double valorCambio){
        double resultadoconversion=0;
        switch  (monedaactual){
            case "Dólar":
                if (monedacambio.equals( "Euro"))
                    resultadoconversion=valorCambio*FactorDolarEuro;

                if (monedacambio.equals( "Peso Mexicano"))
                    resultadoconversion=valorCambio/FactorPesoDolar ;


                break;
            case "Euro":
                if (monedacambio.equals( "Dólar"))
                    resultadoconversion=valorCambio/FactorDolarEuro;

                break;
            case "Peso Mexicano":
                if (monedacambio.equals( "Dólar"))
                    resultadoconversion=valorCambio*FactorPesoDolar;

                break;

        }
        return resultadoconversion;
    }
}
