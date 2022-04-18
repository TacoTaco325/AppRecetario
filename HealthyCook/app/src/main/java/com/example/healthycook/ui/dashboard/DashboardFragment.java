package com.example.healthycook.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthycook.ListaRecetas;
import com.example.healthycook.R;
import com.example.healthycook.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    Button pie, pancake, waffle, flan, cookie, cake, muffin;
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pie = root.findViewById(R.id.btnPie);
        pancake = root.findViewById(R.id.btnPancake);
        waffle = root.findViewById(R.id.btnWaffle);
        flan = root.findViewById(R.id.btnFlan);
        cookie = root.findViewById(R.id.btnCookie);
        cake = root.findViewById(R.id.btnCake);
        muffin = root.findViewById(R.id.btnMuffin);

        pie.setOnClickListener(this);
        pancake.setOnClickListener(this);
        waffle.setOnClickListener(this);
        flan.setOnClickListener(this);
        cookie.setOnClickListener(this);
        cake.setOnClickListener(this);
        muffin.setOnClickListener(this);

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
            case R.id.btnPie:
                intent.putExtra("opcion",pie.getText());
                startActivity(intent);
                break;
            case R.id.btnPancake:
                intent.putExtra("opcion",pancake.getText());
                startActivity(intent);
                break;
            case R.id.btnWaffle:
                intent.putExtra("opcion",waffle.getText());
                startActivity(intent);
                break;
            case R.id.btnFlan:
                intent.putExtra("opcion",flan.getText());
                startActivity(intent);
                break;
            case R.id.btnCookie:
                intent.putExtra("opcion",cookie.getText());
                startActivity(intent);
                break;
            case R.id.btnCake:
                intent.putExtra("opcion",cake.getText());
                startActivity(intent);
                break;
            case R.id.btnMuffin:
                intent.putExtra("opcion",muffin.getText());
                startActivity(intent);
                break;
        }
    }
}