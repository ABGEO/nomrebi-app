package dev.abgeo.nomrebi.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import dev.abgeo.nomrebi.R;
import dev.abgeo.nomrebi.viewAdapter.ResultRecyclerViewAdapter;

public class ResultFragment extends Fragment {

    private ArrayList<String> mItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, NavArgument> defaultArguments = NavHostFragment.findNavController(this)
                .getGraph()
                .getArguments();

        mItems = (ArrayList<String>) defaultArguments.get("items").getDefaultValue();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_result_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ResultRecyclerViewAdapter(mItems));
        }

        return view;
    }

}
