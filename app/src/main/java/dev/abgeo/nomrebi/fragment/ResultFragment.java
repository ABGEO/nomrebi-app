package dev.abgeo.nomrebi.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import dev.abgeo.nomrebi.task.DownloadImageTask;
import dev.abgeo.nomrebi.R;
import dev.abgeo.nomrebi.viewAdapter.ResultRecyclerViewAdapter;

public class ResultFragment extends Fragment {

    private String phone = "";
    private JSONObject additional = new JSONObject();
    private ArrayList<String> mResult = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, NavArgument> defaultArguments = NavHostFragment.findNavController(this)
                .getGraph()
                .getArguments();

        try {
            phone = (String) defaultArguments.get("phone").getDefaultValue();
            additional = new JSONObject((String) defaultArguments.get("additional").getDefaultValue());
            mResult = (ArrayList<String>) defaultArguments.get("names").getDefaultValue();

            if (!additional.getString("number").isEmpty()) {
                phone = additional.getString("number");
            }
        } catch (JSONException ignored) {
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_result_list, container, false);
        View list = view.findViewById(R.id.list);
        TextView tvPhone = view.findViewById(R.id.tvPhone);
        TextView tvName = view.findViewById(R.id.tvName);
        ImageView ivAvatar = view.findViewById(R.id.ivAvatar);

        tvPhone.setText(phone);

        try {
            String imageURL = additional.getString("image");
            String name = additional.getString("name");

            if (!name.isEmpty()) {
                tvName.setText(name);
            } else if (!mResult.isEmpty()) {
                tvName.setText(mResult.get(0));
            } else {
                tvName.setText(null);
            }

            if (!imageURL.isEmpty()) {
                new DownloadImageTask(ivAvatar).execute(imageURL);
            }
        } catch (Exception e) {
            Log.d("ResultFragment", "ResultFragmentException", e);
        }

        if (list instanceof RecyclerView) {
            Context context = list.getContext();
            RecyclerView recyclerView = (RecyclerView) list;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ResultRecyclerViewAdapter(mResult));
        }

        return view;
    }

}
