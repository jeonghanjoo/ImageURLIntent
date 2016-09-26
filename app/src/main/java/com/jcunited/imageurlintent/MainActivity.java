package com.jcunited.imageurlintent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String png = "http://api.mojitok.com/parse/files/com.platfarm.mojitok.server/f41eec04cfbcb55ae4c8ef91ef1b9f7a_095_00037_03-36-16.png";
                String gif = "http://api.mojitok.com/parse/files/com.platfarm.mojitok.server/193dfd53d05ec80261ab2654e162606d_020_00051_04-37.gif";
                requestShareImageFromUrlPath(png);
            }
        });


    }

    private void requestShareImageFromUrlPath(String path) {
        ImageShare.with(this).request(path, new ImageShare.Callback() {
            @Override
            public void onSuccess(final Intent imageIntent) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(Intent.createChooser(imageIntent, "Share Image"));
                    }
                });
            }

            @Override
            public void onFail() {

            }
        });
    }


}
