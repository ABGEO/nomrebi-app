package dev.abgeo.nomrebi.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView ivImage;

    public DownloadImageTask(ImageView ivImage) {
        this.ivImage = ivImage;
    }

    protected Bitmap doInBackground(String... urls) {
        try {
            InputStream in = new java.net.URL(urls[0]).openStream();
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("DownloadImageTask", "Exception", e);
        }

        return null;
    }

    protected void onPostExecute(Bitmap result) {
        if (null != result) {
            ivImage.setImageBitmap(result);
        }
    }

}
