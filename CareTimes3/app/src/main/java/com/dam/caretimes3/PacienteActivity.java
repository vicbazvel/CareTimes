package com.dam.caretimes3;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PacienteActivity extends AppCompatActivity {

    ListView citasListView;
    final String CITAS_API_URL = "http://10.0.2.2:9000/citasPaciente"; // Asegúrate de que la URL es correcta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);  // Layout donde muestras las citas

        citasListView = findViewById(R.id.citasListView);  // Asegúrate de que tienes este ListView en tu layout

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        new CitasTask().execute(username);
    }

    private class CitasTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String usuario = params[0];
            ArrayList<String> citas = new ArrayList<>();

            try {
                String urlString = CITAS_API_URL + "?nombre=" + usuario;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }

                        JSONArray jsonResponse = new JSONArray(response.toString());
                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject cita = jsonResponse.getJSONObject(i);
                            String citaStr = cita.getString("fecha") + " - " + cita.getString("motivo");
                            citas.add(citaStr);
                        }
                    }
                } else {
                    // Manejo de errores
                    citas.add("Error al cargar citas.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return citas;
        }

        @Override
        protected void onPostExecute(ArrayList<String> citas) {
            super.onPostExecute(citas);
            // Aquí adaptamos el ArrayAdapter para mostrar las citas
            if (citas.isEmpty()) {
                Toast.makeText(PacienteActivity.this, "No tienes citas pendientes.", Toast.LENGTH_SHORT).show();
            } else {
                // Usamos un ArrayAdapter para conectar los datos con el ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        PacienteActivity.this,
                        R.layout.item_cita,  // Este es el layout que hemos creado para cada item de la lista
                        citas               // Lista de citas que hemos obtenido
                );
                citasListView.setAdapter(adapter);  // Establecemos el adaptador al ListView
            }
        }
    }
}
