package com.example.appmascotas;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    private EditText edtTipoR, edtNombreR, edtColorR, edtPesoR;
    private Button btnRegistrarMascota;

    String tipo, nombre, color;
    double peso;

    //Enviar / Recibir
    RequestQueue requestQueue;
    private final String URL = "http://192.168.101.26:3000/mascotas";


    private void loadUI() {
        edtTipoR = findViewById(R.id.edtTipoR);
        edtNombreR = findViewById(R.id.edtNombreR);
        edtColorR = findViewById(R.id.edtColorR);
        edtPesoR = findViewById(R.id.edtPesoR);
        btnRegistrarMascota = findViewById(R.id.btnRegistrarMascota);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        loadUI();

        btnRegistrarMascota.setOnClickListener(v -> {validarMascota();});
    }


    private void resetUI(){
        edtTipoR.setText(null);
        edtNombreR.setText(null);
        edtColorR.setText(null);
        edtPesoR.setText(null);
    }

    private void validarMascota() {

        // Validar que no estén vacíos
        if (edtTipoR.getText().toString().trim().isEmpty()) {
            edtTipoR.setError("Ingrese el tipo de mascota");
            edtTipoR.requestFocus();
            return ;
        }

        tipo = edtTipoR.getText().toString();


        if (!tipo.equals("Perro") && !tipo.equals("Gato")) {
            edtTipoR.setError("Solo se permite Perro o Gato");
            edtTipoR.requestFocus();
            return ;
        }

        if (edtNombreR.getText().toString().trim().isEmpty()) {
            edtNombreR.setError("Ingrese el nombre");
            edtNombreR.requestFocus();
            return ;
        }

        if (edtColorR.getText().toString().trim().isEmpty()) {
            edtColorR.setError("Ingrese el color");
            edtColorR.requestFocus();
            return ;
        }

        if (edtPesoR.getText().toString().trim().isEmpty()) {
            edtPesoR.setError("Ingrese el peso");
            edtPesoR.requestFocus();
            return ;
        }

        nombre = edtNombreR.getText().toString();
        color = edtColorR.getText().toString();


        try {
            peso = Double.parseDouble(edtPesoR.getText().toString().trim());
            if (peso <= 0) {
                edtPesoR.setError("Ingrese un peso válido");
                edtPesoR.requestFocus();
                return ;
            }
        } catch (NumberFormatException e) {
            edtPesoR.setError("Ingrese un número válido");
            edtPesoR.requestFocus();
            return ;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mascotas");
        builder.setMessage("¿Seguro desea registrar?");

        builder.setPositiveButton("Si", (a,b) ->{

            registrarMascota();
            resetUI();
        } );
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void registrarMascota(){
        //Comunicacion
        requestQueue = Volley.newRequestQueue(this);


        //requiere JSON / datos a enviar
        JSONObject jsonObject = new JSONObject();

        try {
            //Asignar valores a las cajas
            jsonObject.put("tipo", tipo);
            jsonObject.put("nombre", nombre);
            jsonObject.put("color", color);
            jsonObject.put("pesokg", peso);

        }catch (JSONException e){
            Log.e("Error", e.toString());
        }

        //Definir objeto (respuesta obtener)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //Exito

                        try {
                            String mensaje = jsonObject.getString("message");
                            Log.d("Resultado", jsonObject.toString());
                            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        NetworkResponse response = volleyError.networkResponse;

                        //Evaluar por codigo de error
                        if(response != null && response.data != null ){
                            //Capturar el codigo de error 4xx, 5xx
                            int statusCode = response.statusCode;
                            String errorJson = new String(response.data);

                            Log.e("VolleyError", "Codigo "+ statusCode);
                            Log.e("VolleyError", "Codigo "+ errorJson);
                        }else {
                            Log.e("VolleyError", "Sin respuesta de error");
                        }

                        Log.e("Error", volleyError.toString());
                    }
                }
        );

        //Ejecutamos el proceso
        requestQueue.add(jsonObjectRequest);
    }

}