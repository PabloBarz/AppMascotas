package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

    private final String URL = "http://192.168.101.26:3000/mascotas";
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
                        Log.e("Resultado: ", jsonArray.toString());
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
            ArrayAdapter<String> adapter;
            ArrayList<String> lsMascotas = new ArrayList<>();

            for(int i = 0; i < jsonMascotas.length(); i++){
                JSONObject jsonObject = jsonMascotas.getJSONObject(i);
                lsMascotas.add(jsonObject.getString("Tipo") + " -> " + jsonObject.getString("Nombre") );
            }

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lsMascotas);
            lsvMascotas.setAdapter(adapter);

        }catch (Exception e){
            Log.e("ErrorJSON", e.toString());
        }
    }
}