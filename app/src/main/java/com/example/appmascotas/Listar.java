package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Listar extends AppCompatActivity {

    ListView lsvMascotas;

    private JSONArray jsonMascotasArray;

    private final String URL = "http://192.168.101.41:3000/mascotas";
    private void loadUI(){
        lsvMascotas = findViewById(R.id.lsvMascotas);
    }
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        loadUI();
        obtenerDatos();

        lsvMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Mascotas mascotaSeleccionada = (Mascotas) parent.getItemAtPosition(position);

                int idMascota = mascotaSeleccionada.getId();
                String nombre = mascotaSeleccionada.getNombre();
                String tipo = mascotaSeleccionada.getTipo();
                String color = mascotaSeleccionada.getColor();
                double peso = mascotaSeleccionada.getPesokg();

                // Crear el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Listar.this); // Cambié a Listar.this, ya que es el contexto adecuado.
                builder.setTitle("Mascota: " + nombre);
                builder.setMessage("ID de la mascota: " + idMascota + "\nTipo de la mascota: " + tipo + "\nColor de la mascota: " + color + "\nPeso: " + peso + " kg");

                // Botón OK con null para que se cierre el diálogo al presionar
                builder.setPositiveButton("Ok", null);

                // Crear y mostrar el AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
    }

    private void obtenerDatos(){
        requestQueue = Volley.newRequestQueue(this);

        //Que nos devuelve el WS con el metodo GET

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        renderizarListView(jsonArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error", volleyError.toString());
                        Toast.makeText(getApplicationContext(), "No se obtuvieron los datos", Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(jsonArrayRequest);
    }

    private void renderizarListView(JSONArray jsonMascotas){
        try {
            ArrayAdapter<Mascotas> adapter;
            ArrayList<Mascotas> lsMascotas = new ArrayList<>();

            for (int i = 0; i < jsonMascotas.length(); i++) {
                JSONObject jsonObject = jsonMascotas.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String tipo = jsonObject.getString("tipo");
                String nombre = jsonObject.getString("nombre");
                String color = jsonObject.getString("color");
                double pesokg = jsonObject.getDouble("pesokg");

                // Agregar la mascota a la lista
                lsMascotas.add(new Mascotas(id, tipo, nombre, color, pesokg));
            }

            adapter = new ArrayAdapter<Mascotas>(this, android.R.layout.simple_list_item_1, lsMascotas) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                    }

                    TextView textView = convertView.findViewById(android.R.id.text1);
                    Mascotas mascota = getItem(position);
                    if (mascota != null) {
                        textView.setText(mascota.getTipo() + " -> " + mascota.getNombre());
                    }
                    return convertView;
                }
            };
            lsvMascotas.setAdapter(adapter);

        }catch (Exception e){
            Log.e("ErrorJSON", e.toString());
        }
    }
}