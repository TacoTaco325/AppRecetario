package com.example.healthycook.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthycook.ListaRecetas;
import com.example.healthycook.R;
import com.example.healthycook.Receta;
import com.example.healthycook.RecetaAdaptador;
import com.example.healthycook.adminSqlLite;
import com.example.healthycook.databinding.FragmentHomeBinding;
import com.example.healthycook.ui.notifications.NotificationsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener{

    Button pizza, chicken, meat, salad, pig;
    String ID, Nombre, Receta, Image, Tipo,  Comida, Plato;
    Double Calorias;
    ArrayList<String> lstIngrediente = new ArrayList<String>();
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pizza = root.findViewById(R.id.btnPizza);
        chicken = root.findViewById(R.id.btnChicken);
        meat = root.findViewById(R.id.btnMeat);
        salad = root.findViewById(R.id.btnSalad);
        pig = root.findViewById(R.id.btnPig);

        pizza.setOnClickListener(this);
        chicken.setOnClickListener(this);
        meat.setOnClickListener(this);
        salad.setOnClickListener(this);
        pig.setOnClickListener(this);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ListaRecetas.class);
        switch (view.getId()){
            case R.id.btnPizza:
                intent.putExtra("opcion",pizza.getText());
                startActivity(intent);
            break;
            case R.id.btnChicken:
                intent.putExtra("opcion",chicken.getText());
                startActivity(intent);
            break;
            case R.id.btnMeat:
                intent.putExtra("opcion",meat.getText());
                startActivity(intent);
            break;
            case R.id.btnSalad:
                intent.putExtra("opcion",salad.getText());
                startActivity(intent);
            break;
            case R.id.btnPig:
                intent.putExtra("opcion",pig.getText());
                startActivity(intent);
            break;
        }
    }

    /*public void receta(){
        adminSqlLite admin = new adminSqlLite(getContext(),"DBReceta",null,1);
        SQLiteDatabase DB =  admin.getWritableDatabase();
        String url = "http://192.168.100.21/usuario/API/recetas.php?ID=1";
        StringRequest get = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int k = 0 ; k<jsonarray.length() ; k++){
                        JSONObject inde = jsonarray.getJSONObject(k);
                        ID = inde.getString("idreceta");

                        Cursor fila = DB.rawQuery("select * from Receta where idreceta = '"+inde.getString("idreceta")+"'", null);
                        if (fila.getCount()==0) {
                            Log.d("crack", ID);
                        }

                        Log.d("crack", inde.getString("idreceta"));
                    }
                } catch (JSONException e) {
                    Log.d("crack", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("crack", error.toString());
            }
        });
        Volley.newRequestQueue(getContext()).add(get);
    }

    public void api(){
        String url = "https://api.edamam.com/api/recipes/v2/"+ID+"?type=public&app_id=5133c3e5&app_key=b52dd65e3a34e511b914ff8e47b2c4a8%09";
        JsonObjectRequest get = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("crack", response.toString());
                try {
                    JSONObject recipe = response.getJSONObject("recipe");


                    Nombre = recipe.getString("label");


                    Receta = recipe.getString("url");

                    Calorias = recipe.getDouble("calories");

                    JSONArray cousine = recipe.getJSONArray("cuisineType");
                    Tipo = cousine.getString(0);

                    JSONArray meal = recipe.getJSONArray("mealType");
                    Comida = meal.getString(0);

                    JSONArray dish = recipe.getJSONArray("dishType");
                    Plato = dish.getString(0);

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
        Volley.newRequestQueue(getContext()).add(get);
    }*/
}