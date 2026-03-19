package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ActualizarMascota extends AppCompatActivity {

    private EditText edtTipoA, edtNombreA, edtColorA, edtPesoA;
    private Button btnActualizarMascota;
    private int mascotaId;

    // URL base para la API
    private final String URL_BASE = "http://192.168.18.177:3000/mascotas/";
    private RequestQueue requestQueue;

    private void loadUI(){
        edtTipoA = findViewById(R.id.edtTipoA);
        edtNombreA = findViewById(R.id.edtNombreA);
        edtColorA = findViewById(R.id.edtColorA);
        edtPesoA = findViewById(R.id.edtPesoA);
        btnActualizarMascota = findViewById(R.id.btnActualizarMascota);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_mascota);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        mascotaId = getIntent().getIntExtra("ID",0);
        Toast.makeText(getApplicationContext(),"ID: "+ mascotaId, Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(this);

        obtenerMascotaPorId(mascotaId);

        btnActualizarMascota.setOnClickListener(v -> validarYActualizar());
    }

    private void obtenerMascotaPorId(int id) {
        String url = URL_BASE + id;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject mascota = response.getJSONObject("mascota");
                            edtTipoA.setText(mascota.getString("tipo"));
                            edtNombreA.setText(mascota.getString("nombre"));
                            edtColorA.setText(mascota.getString("color"));
                            edtPesoA.setText(String.valueOf(mascota.getDouble("pesokg")));
                        } else {
                            Toast.makeText(this, "No se encontró la mascota", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("JSONError", e.toString());
                        Toast.makeText(this, "Error parseando datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    Log.e("VolleyErro", error.toString());
                }
        );

        requestQueue.add(request);
    }

    private void validarYActualizar() {
        String tipo = edtTipoA.getText().toString().trim();
        String nombre = edtNombreA.getText().toString().trim();
        String color = edtColorA.getText().toString().trim();
        String pesoStr = edtPesoA.getText().toString().trim();

        if (tipo.isEmpty()) { edtTipoA.setError("Ingrese tipo"); edtTipoA.requestFocus(); return; }
        if (!tipo.equals("Perro") && !tipo.equals("Gato")) { edtTipoA.setError("Solo Perro o Gato"); edtTipoA.requestFocus(); return; }
        if (nombre.isEmpty()) { edtNombreA.setError("Ingrese nombre"); edtNombreA.requestFocus(); return; }
        if (color.isEmpty()) { edtColorA.setError("Ingrese color"); edtColorA.requestFocus(); return; }
        if (pesoStr.isEmpty()) { edtPesoA.setError("Ingrese peso"); edtPesoA.requestFocus(); return; }

        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
            if (peso <= 0) { edtPesoA.setError("Peso inválido"); edtPesoA.requestFocus(); return; }
        } catch (NumberFormatException e) {
            edtPesoA.setError("Ingrese un número válido"); edtPesoA.requestFocus(); return;
        }

        actualizarMascota(tipo, nombre, color, peso);
    }

    private void actualizarMascota(String tipo, String nombre, String color, double peso) {
        String url = URL_BASE + mascotaId;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tipo", tipo);
            jsonObject.put("nombre", nombre);
            jsonObject.put("color", color);
            jsonObject.put("pesokg", peso);
        } catch (JSONException e) { Log.e("JSONError", e.toString());}

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonObject,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {Log.e("JSONError", e.toString());}
                },
                error -> {
                    Toast.makeText(this, "Error en la petición", Toast.LENGTH_SHORT).show();
                    Log.e("Error en la petición", error.toString());
                }
        );

        requestQueue.add(request);
    }
}