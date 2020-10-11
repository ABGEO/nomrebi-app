package dev.abgeo.nomrebi.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import dev.abgeo.nomrebi.R;

public class UpdateDialog extends AppCompatDialogFragment {

    String version;
    String notes;
    String apkUrl;
    String releaseUrl;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        version = getArguments().getString("version");
        notes = getArguments().getString("notes");
        apkUrl = getArguments().getString("apkUrl");
        releaseUrl = getArguments().getString("releaseUrl");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_update_dialog, null);

        builder.setView(view)
                .setTitle(R.string.update_dialog_title)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(apkUrl));
                        startActivity(browserIntent);
                    }
                });

        TextView tvNewVersion = view.findViewById(R.id.tvNewVersion);
        TextView tvUpdateNotes = view.findViewById(R.id.tvUpdateNotes);
        TextView tvReleaseUrl = view.findViewById(R.id.tvReleaseUrl);

        tvNewVersion.setText(String.format(getResources().getString(R.string.new_version_is_available), version));
        tvUpdateNotes.setText(notes);

        tvReleaseUrl.setText(Html.fromHtml(String.format("<a href=\"%s\">%s</a>",
                releaseUrl, getResources().getString(R.string.release_url_title))));
        tvReleaseUrl.setMovementMethod(LinkMovementMethod.getInstance());

        return builder.create();
    }

}
