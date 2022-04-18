package com.example.healthycook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;

public class DetalleReceta extends AppCompatActivity {

    TextView DNombre, DCalorias, DPlato, DComida, DCocina, lblCalorias, lblPlato, lblComida, lblCocina;
    ListView DIngredientes;
    ImageView img, icon;
    Button DPasos, Guardar, Ingredintebtn,trad;
    Double Calorias;
    String ID, Nombre, Receta, Image, Tipo,  Comida, Plato;
    int lis = 1,count = 1;;
    DecimalFormat format = new DecimalFormat("#.##");
    adminSqlLite admin = new adminSqlLite(this,"DBReceta",null,1);
    ArrayList<String> lstIngrediente = new ArrayList<String>();
    ArrayList<String> lstTranslate = new ArrayList<String>();

    usuario obj = new usuario();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_receta);

        DNombre = findViewById(R.id.DetalleNombre);
        DCalorias = findViewById(R.id.DetalleCalorias);
        DPlato = findViewById(R.id.DetallePlato);
        DComida = findViewById(R.id.DetalleComida);
        DCocina = findViewById(R.id.DetalleCocina);
        DIngredientes = findViewById(R.id.DetalleIngrediente);
        img = findViewById(R.id.DetalleImagen);
        icon = findViewById(R.id.Icon);
        DPasos = findViewById(R.id.DetallePasos);
        Guardar = findViewById(R.id.btnGuardar);
        Ingredintebtn = findViewById(R.id.btnIngrediente);
        trad = findViewById(R.id.btnTraductor);
        lblCalorias = findViewById(R.id.Calorias);
        lblPlato = findViewById(R.id.DISH);
        lblComida = findViewById(R.id.Cocina);
        lblCocina = findViewById(R.id.Comida);

        Bundle extra = this.getIntent().getExtras();
        ID = extra.getString("id");
        Image = extra.getString("image");
        Picasso.with(this).load(Image).into(img);
        icon.bringToFront();


        ID_EXISTENTE();

        DPasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Receta));
                startActivity(intent);
            }
        });
    }

    public void ID_EXISTENTE(){
        String url = "http://192.168.100.21/usuario/API/recetas.php?idreceta="+ID+"&idusuario="+usuario.id;

        JsonObjectRequest get = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String r = response.getString("idreceta");
                    if (r.length()!=0){
                        Guardar.setText("Eliminar");
                        Guardar.setBackgroundColor(Color.rgb(179, 0, 0));
                        Database();

                    }
                } catch (JSONException e) {
                    Guardar.setText("Guardar");
                    Guardar.setBackgroundColor(Color.rgb(0, 153, 0));
                    Api();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(get);

        SQLiteDatabase DB =  admin.getWritableDatabase();
        lstIngrediente.clear();
        Cursor fila = DB.rawQuery("select * from Receta where idreceta='"+ID+"'", null);
        if (fila.getCount()==0){
            Guardar.setText("Guardar");
            Guardar.setBackgroundColor(Color.rgb(0, 153, 0));
            Api();
        }else{
            Guardar.setText("Eliminar");
            Guardar.setBackgroundColor(Color.rgb(179, 0, 0));
            Database();
        }
    }

    public void TranslateClick(View view){
        DIngredientes.setAdapter(null);
        lstTranslate.clear();
        if (count == 1){
            datosReceta();
            ingreReceta();
            trad.setText("ENGLISH");
            lblCalorias.setText("CALORIAS");
            lblPlato.setText("TIPO DE PLATO");
            lblCocina.setText("TIPO DE COCINA");
            lblComida.setText("TIPO DE COMIDA");
            Ingredintebtn.setText("INGREDIENTES");
            count = 0;
        }else {
            Ingredintebtn.setText("INGREDIENTS");
            lblCalorias.setText("CALORIES");
            lblPlato.setText("TYPE OF DISH");
            lblCocina.setText("TYPE OF COUSINE");
            lblComida.setText("TYPE OF MEAL");
            trad.setText("ESPAÑOL");
            ID_EXISTENTE();
            count = 1;
        }
    }

    public void datosReceta(){
        traductor(DNombre.getText().toString());
        traductor(DPlato.getText().toString());
        traductor(DCocina.getText().toString());
        traductor(DComida.getText().toString());
    }

    public void ingreReceta(){
        for (int i = 0; i < lstIngrediente.size() ; i++){
            traductorIngre(lstIngrediente.get(i));
            Log.d("Name", lstIngrediente.get(i));
        }
        DIngredientes.setAdapter(null);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lstTranslate);
        DIngredientes.setAdapter(adapter);
    }

    public void traductorIngre(String text){

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        Translator Translator =
                Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        Translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        Translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                lstTranslate.add(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("trl",e.toString());
            }
        });
    }


    public void traductor(String text){

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build();
        Translator Translator =
                Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        Translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        Translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (DNombre.getText().equals(text)){
                    DNombre.setText(s);
                }
                if (DPlato.getText().equals(text)){
                    DPlato.setText(s);
                }
                if (DCocina.getText().equals(text)){
                    DCocina.setText(s);
                }
                if (DComida.getText().equals(text)){
                    DComida.setText(s);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("trl",e.toString());
            }
        });
    }

    public void post(){
        Intent i = new Intent(this, login.class);
        String url = "http://192.168.100.21/usuario/API/recetausu.php";
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    Toast.makeText(DetalleReceta.this, json.getString("mensaje"), Toast.LENGTH_SHORT).show();
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
                params.put("idreceta",ID);
                params.put("idusuario",""+usuario.id);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(post);
    }

    public void Opcion(View v){
        if (Guardar.getText().equals("Guardar")){
            try {
                SQLiteDatabase DB =  admin.getWritableDatabase();
                ContentValues datos = new ContentValues();

                datos.put("idreceta",ID);
                datos.put("Nombre",Nombre);
                datos.put("Receta",Receta);
                datos.put("Image",Image);
                datos.put("Calorias",Double.parseDouble(format.format(Calorias)));
                datos.put("Tipo",Tipo);
                datos.put("Comida",Comida);
                datos.put("Plato", Plato);

                DB.insert("Receta",null,datos);


                for (int i = 0 ; i<lstIngrediente.size();i++){
                    ContentValues ingredientes = new ContentValues();
                    ingredientes.put("id_receta",ID);
                    ingredientes.put("Nombre", lstIngrediente.get(i));
                    DB.insert("Ingredientes",null,ingredientes);
                }

                DB.close();

                post();

                Toast.makeText(DetalleReceta.this, "Guardado en Mis Recetas", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(DetalleReceta.this, "Error"+e, Toast.LENGTH_SHORT).show();
            }
            Guardar.setText("Eliminar");
            Guardar.setBackgroundColor(Color.rgb(179, 0, 0));
        }else {
            String ur = "http://192.168.100.21/usuario/API/recetas.php?idreceta="+ID+"&idusuario="+usuario.id;
            StringRequest delete = new StringRequest(Request.Method.DELETE, ur, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        Toast.makeText(DetalleReceta.this, json.getString("mensaje"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(this).add(delete);
            Guardar.setText("Guardar");
            Guardar.setBackgroundColor(Color.rgb(0, 153, 0));
        }
    }

    public void ListarIngredientes(View v){
        DIngredientes.setAdapter(null);
        if (Ingredintebtn.getText().equals("INGREDIENTS")){
            if (lis == 1){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lstIngrediente);
                DIngredientes.setAdapter(adapter);
                icon.setImageResource(R.drawable.up_arrow);
                lis = 0;
            }else {
                DIngredientes.setAdapter(null);
                icon.setImageResource(R.drawable.angle_arrow_down_icon_icons_com_73683);
                lis = 1;
            }
        }else {
            if (lis == 1){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lstTranslate);
                DIngredientes.setAdapter(adapter);
                icon.setImageResource(R.drawable.up_arrow);
                lis = 0;
            }else {
                DIngredientes.setAdapter(null);
                icon.setImageResource(R.drawable.angle_arrow_down_icon_icons_com_73683);
                lis = 1;
            }
        }

    }

    public void Database(){
        SQLiteDatabase BD =  admin.getWritableDatabase();
        Cursor fila = BD.rawQuery("select * from Receta where idreceta='"+ID+"'", null);
        if (fila.moveToFirst()){
            Nombre = fila.getString(1);
            DNombre.setText(Nombre);

            Receta = fila.getString(2);

            Calorias = fila.getDouble(4);
            DCalorias.setText(""+Calorias);

            Plato = fila.getString(7);
            DPlato.setText(Plato);

            Tipo = fila.getString(5);
            DCocina.setText(Tipo);

            Comida = fila.getString(6);
            DComida.setText(Comida);
        }

        Cursor Ingre = BD.rawQuery("select * from Ingredientes where id_receta='"+ID+"'", null);
        if (Ingre.moveToFirst()){
            do{
                lstIngrediente.add(Ingre.getString(1));
            }while (Ingre.moveToNext());
        }
        BD.close();
    }

    public void Api(){
        String url = "https://api.edamam.com/api/recipes/v2/"+ID+"?type=public&app_id=5133c3e5&app_key=b52dd65e3a34e511b914ff8e47b2c4a8%09";
        JsonObjectRequest get = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject recipe = response.getJSONObject("recipe");


                    Nombre = recipe.getString("label");

                    DNombre.setText(Nombre);

                    Receta = recipe.getString("url");

                    Calorias = recipe.getDouble("calories");
                    DCalorias.setText(format.format(Calorias));

                    JSONArray cousine = recipe.getJSONArray("cuisineType");
                    Tipo = cousine.getString(0);
                    DCocina.setText(Tipo);

                    JSONArray meal = recipe.getJSONArray("mealType");
                    Comida = meal.getString(0);
                    DComida.setText(Comida);

                    JSONArray dish = recipe.getJSONArray("dishType");
                    Plato = dish.getString(0);
                    DPlato.setText(Plato);

                    JSONArray ingredientes = recipe.getJSONArray("ingredients");

                    for (int k = 0 ; k<ingredientes.length() ; k++){
                        JSONObject inde = ingredientes.getJSONObject(k);
                        lstIngrediente.add(inde.getString("text"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(get);
    }

    //Compartir
    public void Compartir(View view){
        String ing = "";

        for(int i = 0; i < lstIngrediente.size(); i++) {
            ing += "* " + lstIngrediente.get(i) + "\n";
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,
                "Look at this incredible recipe I found it in Healthy Cook\n" +
                "**" + DNombre.getText() + "**\n"+"-----------------------------------\n"+
                "Calories: " + DCalorias.getText() + "\n"+"-----------------------------------\n"+
                "Type of meal: " + DComida.getText() + "\n"+"-----------------------------------\n"+
                "Type of dish: " + DPlato.getText() + "\n"+"-----------------------------------\n"+
                "Type of cousine: " + DCocina.getText() + "\n"+"-----------------------------------\n"+
                "Ingredients: \n" + ing + "\n"+"-----------------------------------\n"+
                "Steps: \n" + Receta
                 );
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"Elija la opcion para compartir"));
    }
}