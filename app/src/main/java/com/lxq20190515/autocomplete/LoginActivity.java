package com.lxq20190515.autocomplete;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private BufferedReader reader;
    private EditText account;
    private EditText password;
    private String result;
    private String method;//get或set
    //private JSONArray json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        method=getIntent().getStringExtra("method");
        setTitle(method);
    }

    //登录按钮事件，选择提交方法
    public void selectMethod(View view){
        if (method.equals("get"))loginGet();else loginPost();
    }
    //使用get请求方法登录
    public void loginGet() {
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

    //使用POST请求方法登录
    public void loginPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                int acc = Integer.parseInt(account.getText().toString());
                String postData = "account=" + acc + "&password=" + password.getText().toString();
                URL url;
                try {
                    //请求
                    url = new URL("http://10.83.216.23/login_post.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());
                    dataOut.writeBytes(postData);

                    //得到
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    final String answer = reader.readLine();
                    getJson(answer);
                    inputStream.close();
                    reader.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //注册按钮事件
    public void register(View view) {
        Toast.makeText(this, "未完成该功能", Toast.LENGTH_SHORT).show();
        if (account.getText().toString().equals("")) {

        }
    }

    //解析json数据
    public void getJson(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //======使用JSONArray======
                try {
                    Log.e("json",json);
                    JSONArray jsonArray=new JSONArray(json);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if(jsonObject.getString("answer").equals("success")){
                            Intent intent=new Intent(LoginActivity.this, SuccessActivity.class);
                            intent.putExtra("name",jsonObject.getString("name"));
                            intent.putExtra("json_data",json);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "欢迎回来："+jsonObject.getString("name"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //=========以下代码使用的是Gson解析json==========
                /*Gson gson = new Gson();
                List<Information> information = gson.fromJson(json, new TypeToken<List<Information>>() {
                }.getType());
                for (Information info : information) {
                    if (info.getAnswer().equals("success")) {
                        Intent intent=new Intent(LoginActivity.this, SuccessActivity.class);
                        intent.putExtra("name",info.getName());
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "欢迎回来："+info.getName(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }*/

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("登录结果")
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage("登录失败！\n账号或密码错误")
                        .show();
            }
        });
    }
}
