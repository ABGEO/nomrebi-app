package dev.abgeo.nomrebi.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import dev.abgeo.nomrebi.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvVersion = view.findViewById(R.id.tvVersion);
        TextView tvCopyright = view.findViewById(R.id.tvCopyright);
        String version = "0";
        try {
            version = view.getContext().getPackageManager()
                    .getPackageInfo(view.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        tvVersion.setText(String.format(getResources().getString(R.string.app_version), version));
        tvCopyright.setText(String.format(getResources().getString(R.string.app_copyright), Calendar.getInstance().get(Calendar.YEAR)));
    }
}
