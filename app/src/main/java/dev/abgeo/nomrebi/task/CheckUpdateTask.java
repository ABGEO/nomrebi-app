package dev.abgeo.nomrebi.task;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import dev.abgeo.nomrebi.fragment.UpdateDialog;


public class CheckUpdateTask extends AsyncTask<Void, Void, Map<String, String>> {

    private static final String TAG = "CheckUpdateTask";
    private final String REPO = "ABGEO/nomrebi-app";
    private final Context context;

    public CheckUpdateTask(Context context) {
        this.context = context;
    }

    private JSONObject getLatestRelease() {
        JSONObject response = new JSONObject();
        try {
            try (InputStream is = new URL("https://api.github.com/repos/" + REPO + "/releases/latest").openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                response = new JSONObject(sb.toString());
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "getLatestRelease: Exception", e);
        }

        return response;
    }

    protected Map<String, String> doInBackground(Void... params) {
        try {
            JSONObject latestRelease = getLatestRelease();
            String latestVersion = latestRelease.getString("tag_name");
            String releaseNotes = latestRelease.getString("body");
            String version = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;

            Map<String, String> data = new HashMap<>();
            data.put("latestVersion", latestVersion);
            data.put("currentVersion", version);
            data.put("releaseNotes", releaseNotes);

            return data;
        } catch (PackageManager.NameNotFoundException | JSONException e) {
            Log.e(TAG, "doInBackground: Exception", e);
        }

        return null;
    }

    protected void onPostExecute(Map<String, String> data) {
        if (null != data) {
            ComparableVersion currentVersion = new ComparableVersion("v" + data.get("currentVersion"));
            ComparableVersion latestVersion = new ComparableVersion("" + data.get("latestVersion"));

            if (0 < currentVersion.compareTo(latestVersion)) {
                AppCompatActivity activity = (AppCompatActivity) context;
                UpdateDialog dialog = new UpdateDialog();

                Bundle args = new Bundle();
                String apkUrl = String.format(
                        "https://github.com/%s/releases/download/%s/nomrebi-%s.apk",
                        REPO,
                        data.get("latestVersion"),
                        data.get("latestVersion").substring(1)
                );
                String releaseUrl = String.format(
                        "https://github.com/%s/releases/tag/%s",
                        REPO,
                        data.get("latestVersion")
                );

                args.putString("version", data.get("latestVersion"));
                args.putString("notes", data.get("releaseNotes"));
                args.putString("apkUrl", apkUrl);
                args.putString("releaseUrl", releaseUrl);

                dialog.setArguments(args);
                dialog.show(activity.getSupportFragmentManager(), "UpdateDialog");
            }
        }
    }

}
