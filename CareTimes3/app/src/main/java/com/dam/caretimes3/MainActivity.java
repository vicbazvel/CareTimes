package com.dam.caretimes3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    final String API_URL = "http://10.0.2.2:8080/login";  // Cambia si usas un dispositivo físico

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Asegúrate de tener este XML como pantalla de login

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String usuario = usernameEditText.getText().toString();
            String clave = passwordEditText.getText().toString();

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(MainActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                new LoginTask().execute(usuario, clave);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String clave = params[1];

            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("nombre", usuario);
                jsonInput.put("password", clave);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInput.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        return response.toString();  // Ej: "PACIENTE" o "DOCTOR"
                    }
                } else {
                    return "ERROR";
                }

            } catch (Exception e) {
                Log.e("LoginTask", "Error: ", e);
                return "ERROR";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String usuario = usernameEditText.getText().toString();

            // Guardamos el nombre de usuario y el rol en SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", usuario);
            editor.putString("role", result);
            editor.apply();

            // Navegamos según el rol del usuario
            if (result.equals("PACIENTE")) {
                Intent intent = new Intent(MainActivity.this, PacienteActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else if (result.equals("DOCTOR")) {
                Intent intent = new Intent(MainActivity.this, DoctorActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
