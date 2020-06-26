package dev.abgeo.nomrebi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSearch = view.findViewById(R.id.btnSearch);
        final EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String phoneNumber = etPhoneNumber.getText().toString();
                final ArrayList<String> mItems = new ArrayList<>();
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                String url = "https://simpleapi.info/apps/numbers-info/info.php?results=json";

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject responseConverted = new JSONObject(response);

                                    if (responseConverted.has("res") && responseConverted.get("res").equals("yes")) {
                                        JSONArray items = responseConverted.getJSONArray("items");

                                        for (int i = 0; i < items.length(); i++) {
                                            mItems.add(items.getString(i));
                                        }

                                        NavArgument.Builder builder = new NavArgument.Builder();
                                        builder.setDefaultValue(mItems);
                                        NavController controller = NavHostFragment.findNavController(SearchFragment.this);
                                        controller.getGraph().addArgument("items", builder.build());
                                        controller.navigate(R.id.action_SearchFragment_to_resultFragment);
                                    } else {
                                        etPhoneNumber.setError(getResources().getString(R.string.error_no_result));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("That didn't work!");
                            }
                        }
                ) {
                    @Override
                    protected Map<String,String> getParams(){
                        HashMap<String, String> params = new HashMap<>();
                        params.put("number", phoneNumber);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });
    }

}
