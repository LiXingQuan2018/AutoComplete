package com.lxq20190515.autocomplete;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLImageActivity extends AppCompatActivity {
    private EditText tv_url;
    private ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url_image_acitivity);
        tv_url = findViewById(R.id.img_url);
        img = findViewById(R.id.img);

        tv_url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            HttpURLConnection connect;
                            URL url = new URL(tv_url.getText().toString());
                            connect = (HttpURLConnection) url.openConnection();
                            if (connect.getResponseCode() != 200) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(URLImageActivity.this, "访问失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }

                            InputStream in = connect.getInputStream();
                            Log.e("statu", "成功");
                            final Bitmap bitmap = BitmapFactory.decodeStream(in);
                            img.post(new Runnable() {
                                @Override
                                public void run() {
                                    img.setImageBitmap(bitmap);
                                    tv_url.setText("");
                                }
                            });
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return false;
            }
        });
    }
}
