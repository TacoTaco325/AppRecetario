package com.example.healthycook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

    EditText usu, con;
    Button validar;
    String ID, usuario, contraseña, fecha, correo;
    usuario objUsuario = new usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usu = findViewById(R.id.txtUsuario);
        con = findViewById(R.id.txtContraseña);

    }


    public void registernow(View view){
        Intent i = new Intent(this, register.class);
        startActivity(i);
    }
    public void login(View view){
        validar();
    }

    public void validar(){
        Intent i = new Intent( this,MainActivity.class);
        String url = "http://192.168.100.21/usuario/API/recetas.php?usuario="+usu.getText().toString()+"&contrasena="+con.getText().toString();
        JsonObjectRequest get = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ID = response.getString("ID");
                    usuario = response.getString("usuario");
                    contraseña = response.getString("contrasena");
                    fecha = response.getString("fecha");
                    correo = response.getString("correo");
                    objUsuario.setId(Integer.parseInt(ID));
                    objUsuario.setUsuario(usuario);
                    objUsuario.setContraseña(contraseña);
                    objUsuario.setFecha(fecha);
                    objUsuario.setCorreo(correo);
                    startActivity(i);
                } catch (JSONException e) {
                    Toast.makeText(login.this, "Usuario y/o Contraseña Inconrrecta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("crack",error.toString());
            }
        });

        Volley.newRequestQueue(this).add(get);
    }



}