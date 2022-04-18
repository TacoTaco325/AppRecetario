package com.example.healthycook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText Rusu, RContraseña, RCorreo, Rfecha;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Rusu = findViewById(R.id.txtRusuario);
        RContraseña = findViewById(R.id.txtRcontraseña);
        RCorreo = findViewById(R.id.txtRcorreo);
        Rfecha = findViewById(R.id.txtRFecha);
        btn = findViewById(R.id.btnRegister);

    }

    public void register_click(View view){
        if (Rusu.length()!=0 || RContraseña.length()!=0 || Rfecha.length()!=0 || RCorreo.length()!=0){
            post();
        }else {
            Toast.makeText(register.this,"Campos Incompletos", Toast.LENGTH_SHORT).show();
        }
    }

    public void post(){
        Intent i = new Intent(this, login.class);
        String url = "http://192.168.100.21/usuario/API/recetas.php";
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("mensaje").equals("Usuario registrado")) {
                        startActivity(i);
                    }
                    Toast.makeText(register.this, json.getString("mensaje"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("usuario",Rusu.getText().toString());
                params.put("contrasena",RContraseña.getText().toString());
                params.put("fecha",Rfecha.getText().toString());
                params.put("correo",RCorreo.getText().toString());

                return params;
            }
        };
        Volley.newRequestQueue(this).add(post);
    }

}