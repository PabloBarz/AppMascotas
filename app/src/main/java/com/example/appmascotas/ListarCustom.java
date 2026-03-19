package com.example.appmascotas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarCustom extends AppCompatActivity implements  MascotaAdapter.OnAccionListener{
    RecyclerView recyclerMascota;

    MascotaAdapter adapter;

    ArrayList<Mascotas> listaMascotas;

    RequestQueue queue;

    private final String URL = "http://192.168.101.41:3000/mascotas";

    private void loadUI(){
        recyclerMascota = findViewById(R.id.rcwMascotas);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_custom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        listaMascotas = new ArrayList<>();
        adapter = new MascotaAdapter(this, listaMascotas, this); //Implementrar defincion de clases
        recyclerMascota.setLayoutManager(new LinearLayoutManager(this));
        recyclerMascota.setAdapter(adapter);

        obtenerDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerDatos();
    }

    private void obtenerDatos(){
        queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        renderizarDatos(jsonArray);
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

        queue.add(jsonArrayRequest);
    }

    private void renderizarDatos(JSONArray jsonMascotas){
        //Con los datos obtenidos cargamos la lista que ya esta vinculada a MascotaAdapter
        try {
            listaMascotas.clear();

            for (int i = 0;i < jsonMascotas.length();i++){
                JSONObject jsonObject = jsonMascotas.getJSONObject(i);
                listaMascotas.add(new Mascotas(
                        jsonObject.getInt("id"),
                        jsonObject.getString("tipo"),
                        jsonObject.getString("nombre"),
                        jsonObject.getString("color"),
                        jsonObject.getDouble("pesokg")
                ));
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.e("JSONErro", e.toString());
        }

    }

    @Override
    public void onEditar(int position, Mascotas mascotas) {
        Intent intent = new Intent(getApplicationContext(), ActualizarMascota.class);
        intent.putExtra("ID", mascotas.getId());
        startActivity(intent);
    }

    @Override
    public void onEliminar(int position, Mascotas mascotas) {

    }
}