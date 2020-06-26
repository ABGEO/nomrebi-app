package dev.abgeo.nomrebi.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.abgeo.nomrebi.R;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private static final String API_URL = "https://simpleapi.info/apps/numbers-info/info.php?results=json";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageButton btnSearch = view.findViewById(R.id.btnSearch);
        final ProgressBar pbSearch = view.findViewById(R.id.pbSearch);
        final EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String phoneNumber = etPhoneNumber.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(view.getContext());

                if (phoneNumber.isEmpty()) {
                    etPhoneNumber.setError(getResources().getString(R.string.error_phone_number_is_empty));
                    etPhoneNumber.requestFocus();
                    return;
                }

                btnSearch.setEnabled(false);
                btnSearch.setImageResource(0);
                pbSearch.setVisibility(View.VISIBLE);
                etPhoneNumber.setEnabled(false);

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        API_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject responseConverted = new JSONObject(response);

                                    if (responseConverted.has("res") && responseConverted.get("res").equals("yes")) {
                                        JSONArray items = responseConverted.getJSONArray("items");
                                        ArrayList<String> mItems = new ArrayList<>();

                                        for (int i = 0; i < items.length(); i++) {
                                            mItems.add(items.getString(i));
                                        }

                                        NavArgument.Builder builder = new NavArgument.Builder();
                                        builder.setDefaultValue(mItems);
                                        NavController controller = NavHostFragment.findNavController(SearchFragment.this);
                                        controller.getGraph().addArgument("result", builder.build());
                                        controller.navigate(R.id.action_SearchFragment_to_resultFragment);
                                    } else {
                                        Log.i(TAG, "onResponse: No Result");
                                        etPhoneNumber.setError(getResources().getString(R.string.error_no_result));
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "onResponse: ", e);
                                }

                                btnSearch.setEnabled(true);
                                btnSearch.setImageResource(R.drawable.ic_search_48dp);
                                pbSearch.setVisibility(View.INVISIBLE);
                                etPhoneNumber.setEnabled(true);
                                etPhoneNumber.requestFocus();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "onErrorResponse: ", error);

                                if (
                                        error instanceof NetworkError
                                        || error instanceof AuthFailureError
                                        || error instanceof TimeoutError
                                ) {
                                    Snackbar.make(view, "დაფიქსირდა ინტერნეტთან დაკავშირების შეცდომა.", Snackbar.LENGTH_LONG)
                                            .show();
                                }

                                btnSearch.setEnabled(true);
                                btnSearch.setImageResource(R.drawable.ic_search_48dp);
                                pbSearch.setVisibility(View.INVISIBLE);
                                etPhoneNumber.setEnabled(true);
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
