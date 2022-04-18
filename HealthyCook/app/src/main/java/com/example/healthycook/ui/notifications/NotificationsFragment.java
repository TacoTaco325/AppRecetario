package com.example.healthycook.ui.notifications;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthycook.DetalleReceta;
import com.example.healthycook.ListaRecetas;
import com.example.healthycook.R;
import com.example.healthycook.Receta;
import com.example.healthycook.RecetaAdaptador;
import com.example.healthycook.adminSqlLite;
import com.example.healthycook.databinding.FragmentNotificationsBinding;
import com.example.healthycook.usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements RecetaAdaptador.OnItemClickListener{

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    RecyclerView rclg;
    RecetaAdaptador rctAdaptador;
    ArrayList<Receta> lstReceta;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        rclg = root.findViewById(R.id.rclGuardados);
        rclg.setHasFixedSize(true);
        rclg.setLayoutManager(new LinearLayoutManager(getContext()));
        lstReceta = new ArrayList<>();
        listar();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void listar(){
        adminSqlLite admin = new adminSqlLite(getContext(),"DBReceta",null,1);
        SQLiteDatabase DB =  admin.getWritableDatabase();
        String url = "http://192.168.100.21/usuario/API/recetas.php?ID="+ usuario.id;
        StringRequest get = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int k = 0 ; k<jsonarray.length() ; k++){
                        JSONObject inde = jsonarray.getJSONObject(k);

                        Cursor fila = DB.rawQuery("select * from Receta where idreceta = '"+inde.getString("idreceta")+"'", null);
                        if (fila.moveToFirst()) {
                            lstReceta.add(new Receta(fila.getString(0), fila.getString(1),fila.getString(4), fila.getString(3) ));
                        }
                        Log.d("crack", inde.getString("idreceta"));
                    }
                    rctAdaptador = new RecetaAdaptador(getContext(),lstReceta);
                    rclg.setAdapter(rctAdaptador);
                    rctAdaptador.setOnItemClickListener(NotificationsFragment.this);
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getContext()).add(get);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetalleReceta.class);
        Receta rct = lstReceta.get(position);

        intent.putExtra("id",rct.getId());
        intent.putExtra("image", rct.getImagen());
        startActivity(intent);
    }
}