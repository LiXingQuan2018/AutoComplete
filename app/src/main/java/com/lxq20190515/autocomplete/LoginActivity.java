package com.lxq20190515.autocomplete;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private BufferedReader reader;
    private EditText account;
    private EditText password;
    private String result;
    private String success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
    }

    public void login(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                int acc = Integer.parseInt(account.getText().toString());
                String pass = password.getText().toString();
                try {
                    URL url = new URL("http://10.83.216.23/login_get.php?account=" + acc + "&password=" + pass);
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    result = reader.readLine();
                    //result = URLEncoder.encode(result,"utf-8");
                    success = URLEncoder.encode("success", "utf-8");
                    Log.e("result", result);
                    Log.e("result", result.getClass().toString());
                    Log.e("比较", result.equals(success) + "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.equals("success")) {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("登录结果")
                                        .setMessage("登录成功！\n" + result)
                                        .show();
                            } else {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("登录结果")
                                        .setMessage("登录失败！\n" + result)
                                        .show();
                            }
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
    public void register(View view){
        if(account.getText().toString().equals("")){
            Toast.makeText(this, "请先将资料填写完整", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
