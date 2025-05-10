package com.dam.caretimes3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DoctorActivity extends AppCompatActivity {

    ListView listaCitas;
    ArrayList<String> citasTexto = new ArrayList<>();
    String nombreDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        listaCitas = findViewById(R.id.listaCitas);
        nombreDoctor = getIntent().getStringExtra("usuario");

        new ObtenerCitasTask().execute(nombreDoctor);
    }

    private class ObtenerCitasTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String doctor = params[0];
            ArrayList<String> resultado = new ArrayList<>();

            try {
                URL url = new URL("http://10.0.2.2:8080/citas?doctor=" + doctor); // este endpoint hay que crearlo si no existe
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String linea;
                while ((linea = reader.readLine()) != null) {
                    response.append(linea);
                }

                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String citaTexto = obj.getString("fecha") + " - " + obj.getString("hora") +
                            " con " + obj.getString("paciente") + ": " + obj.getString("motivo");
                    resultado.add(citaTexto);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(ArrayList<String> citas) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(DoctorActivity.this, android.R.layout.simple_list_item_1, citas);
            listaCitas.setAdapter(adapter);
        }
    }
}
