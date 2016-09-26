package com.jcunited.imageurlintent;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by jj on 26/09/2016.
 */
public class ImageShare {

    public interface Callback {
        void onSuccess(Intent imageIntent);
        void onFail();
    }


    private Context context;

    private ImageShare(Context context){
        this.context = context;
    }

    public static ImageShare with(Context context) {
        ImageShare imageShare = new ImageShare(context);
        return imageShare;
    }

    public void request(final String path, final Callback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Uri uri = saveImageFromUrlPath(path);
                if (uri == null) {
                    callback.onFail();
                } else {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image

                    callback.onSuccess(shareIntent);
                }
            }
        };

        Thread backThread = new Thread(runnable);
        backThread.start();
    }


    private Uri saveImageFromUrlPath(String path){
        String mime = "png";
        URL url = null;
        InputStream input = null;

        Uri resultUri = null;

        try {
            mime = getMime(path);
            url = new URL(path);
            input = url.openStream();
        }
        catch (Exception e) {
            e.printStackTrace();
            return resultUri;
        }

        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_" + System.currentTimeMillis() + "." + mime);
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }

                resultUri = Uri.fromFile(file);

            } finally {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultUri;
    }

    private String getMime(String file) throws Exception {
        String[] splits = file.split("\\.");

        if (splits == null || splits.length < 1) {
            throw new Exception("Has no extension.");
        }

        return splits[splits.length - 1];
    }

}
