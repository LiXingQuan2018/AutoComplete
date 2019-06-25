package com.lxq20190515.autocomplete;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SuccessActivity extends AppCompatActivity {

    private String json="";
    private TextView thisWeek;
    private TextView lastWeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        thisWeek=findViewById(R.id.this_week);
        lastWeek=findViewById(R.id.last_week);
        setTitle(getIntent().getStringExtra("name")+"的步数记录");
        json=getIntent().getStringExtra("json_data");
        Log.e("json",json);
        try {
            //解析
            JSONArray jsonArray=new JSONArray(json);
            //获取本周
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                String thisWeekText="步数："+jsonObject.getInt("step")
                        +"\n时间："+jsonObject.getString("time")
                        +"\n卡路里："+jsonObject.getDouble("heat")
                        +"\n公里："+jsonObject.getDouble("km");
                thisWeek.setText(thisWeekText);

            String lastWeekText="步数："+jsonObject.getInt("step1")
                    +"\n时间："+jsonObject.getString("time1")
                    +"\n卡路里："+jsonObject.getDouble("heat1")
                    +"\n公里："+jsonObject.getDouble("km1");
            lastWeek.setText(lastWeekText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
