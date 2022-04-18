package com.example.healthycook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListaRecetas extends AppCompatActivity implements RecetaAdaptador.OnItemClickListener {

    RecyclerView rcl;
    int api;
    EditText buscartxt;
    RecetaAdaptador rctAdaptador;
    ArrayList<Receta> lstReceta;
    String URICOMPLETA, ID, Nombre, Image, Calorias, opcion;
    DecimalFormat format = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_recetas);

        buscartxt = findViewById(R.id.txtBuscar);
        rcl = findViewById(R.id.rclGuardados);
        rcl.setHasFixedSize(true);
        rcl.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = this.getIntent().getExtras();
        api = extra.getInt("api");
        opcion = extra.getString("opcion");

        lstReceta = new ArrayList<>();

        if (!opcion.isEmpty()){
            buscartxt.setVisibility(View.INVISIBLE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rcl.getLayoutParams();
            params.topMargin = 0;
            rcl.setLayoutParams(params);
            api();
        }else {
            buscartxt.setVisibility(View.VISIBLE);
        }

        buscartxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i){
                    case EditorInfo.IME_ACTION_SEARCH:
                        if (buscartxt.length()>0){
                            opcion = buscartxt.getText().toString();
                            lstReceta.clear();
                            api();
                        }
                    break;
                }
                return false;
            }
        });

    }

    public void api(){
        String url = "https://api.edamam.com/api/recipes/v2?type=public&q="+opcion+"&app_id=5133c3e5&app_key=b52dd65e3a34e511b914ff8e47b2c4a8";
        JsonObjectRequest get = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray hits = response.getJSONArray("hits");
                    for (int i = 0 ; i<hits.length() ; i++){
                        JSONObject index = hits.getJSONObject(i);
                        JSONObject recipe = index.getJSONObject("recipe");

                        URICOMPLETA = recipe.getString("uri");
                        String[] asd = URICOMPLETA.split("_");
                        ID = asd[1];

                        Nombre = recipe.getString("label");
                        Image = recipe.getString("image");
                        Calorias = format.format(recipe.getDouble("calories"));

                        lstReceta.add(new Receta(ID,Nombre,Calorias,Image));
                    }
                    rctAdaptador = new RecetaAdaptador(ListaRecetas.this,lstReceta);
                    rcl.setAdapter(rctAdaptador);
                    rctAdaptador.setOnItemClickListener(ListaRecetas.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Nombre",error.toString());
            }
        });
        Volley.newRequestQueue(this).add(get);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetalleReceta.class);
        Receta rct = lstReceta.get(position);
        intent.putExtra("id",rct.getId());
        intent.putExtra("image", rct.getImagen());
        startActivity(intent);
    }
}