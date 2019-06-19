package com.lxq20190515.autocomplete;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView auto;
    private TextView test;
    private StringBuffer response;
    private BufferedReader reader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auto=findViewById(R.id.auto_tv);
        test=findViewById(R.id.test_tv);

        auto.addTextChangedListener(watcher);
    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final HttpURLConnection connection;
                    try {
                        URL url=new URL("http://193.112.100.153/mis/api.php?types=search&source=tencent&name="
                                +auto.getText().toString());
                        //URL url=new URL("http://10.83.216.23/");
                        connection=(HttpURLConnection)url.openConnection();
                        InputStream inputStream=connection.getInputStream();
                        reader=new BufferedReader(new InputStreamReader(inputStream));
                        response=new StringBuffer();
                        String line;
                        while ((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        test.post(new Runnable() {
                            @Override
                            public void run() {
                                test.setText(response.toString());
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("查看网络图片").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, URLImageActivity.class));
                return false;
            }
        });
        menu.add("GET登录测试").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
