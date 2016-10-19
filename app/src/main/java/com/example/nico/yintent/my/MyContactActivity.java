package com.example.nico.yintent.my;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.nico.yintent.R;
import com.example.nico.yintent.Utils.CONSTANT;
import com.example.nico.yintent.Utils.NetUtils;
import com.example.nico.yintent.been.Passenger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.ObjectConstructor;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyContactActivity extends AppCompatActivity {
    ListView lvMyContact = null;
    ProgressDialog pDialog = null;
    List<Map<String, Object>> data=null;
    SimpleAdapter adapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ActionBar bar=getActionBar();
        //bar.setDisplayHomeAsUpEnabled(true);

        lvMyContact = (ListView) findViewById(R.id.lvMyContact);
        //数据
        data = new ArrayList<Map<String, Object>>();
        //适配器
        //context:上下文//data：数据//resource:每一行的布局方式
        //from:Map中的key//to:布局中的组件id
        //1
        /*Map<String,Object> row=new HashMap<String,Object>();
        row.put("name","东方人(成人)");
        row.put("idCard","身份证:211211211121112111");
        row.put("tel","电话:13111111111");
        data.add(row);
        //2
        row=new HashMap<String,Object>();
        row.put("name","小王(成人)");
        row.put("idCard","身份证:122122122212221222");
        row.put("tel", "电话:13222222222");
        data.add(row);
        //3
        row=new HashMap<String,Object>();
        row.put("name","小明(学生)");
        row.put("idCard","学生证:123123123412341234");
        row.put("tel", "电话:13333333333");
        data.add(row);
        //shipeiqi
        //context:shangxiawen
        //data:shujv
        //resource:hangbuju
        //from:Map zhongde KEY
        //to:bujuzhongde zujian*/
        adapter = new SimpleAdapter(this, data, R.layout.item_my_contact,
                new String[]{"name", "idCard", "tel"}, new int[]{
                R.id.tvNameContact, R.id.tvIdCardContact, R.id.tvTelContact});
        //bangding
        lvMyContact.setAdapter(adapter);
        lvMyContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyContactActivity.this, MyContactEditActivity.class);
                intent.putExtra("row", (Serializable) data.get(position));//Map
                startActivity(intent);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        //访问服务器，更新数据
        //Toast.makeText(MyContactActivity.this,"刷新",Toast.LENGTH_SHORT).show();
        if (!NetUtils.check(MyContactActivity.this)) {
            Toast.makeText(MyContactActivity.this, getString(R.string.network_check), Toast.LENGTH_SHORT).show();
            return;//后续代码不执行
        }
        //进度对话框
        pDialog = ProgressDialog.show(MyContactActivity.this, null, "正在加载中...", false, true);
        new Thread() {

            public void run() {
                //获取message
                Message msg = handler.obtainMessage();
                HttpPost post = new HttpPost(CONSTANT.HOST + "/otn/PassengerList");
                //发送请求
                DefaultHttpClient client = new DefaultHttpClient();
                try {
                    //jsessionid
                    SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                    String value = pref.getString("Cookie", "");
                    BasicHeader header = new BasicHeader("Cookie", value);
                    post.setHeader(header);
                    //超时设置
                    client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                            CONSTANT.REQUEST_TIMEOUT);
                    client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                            CONSTANT.SO_TIMEOUT);
                    HttpResponse response = client.execute(post);
                    //处理结果
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String json = EntityUtils.toString(response.getEntity());
                        Gson gson = new GsonBuilder().create();
                        Passenger[] passengers = gson.fromJson(json, Passenger[].class);
                        //发送消息
                        msg.obj = passengers;
                        msg.what = 1;
                    } else {
                        msg.what = 2;
                    }
                    client.getConnectionManager().shutdown();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    msg.what = 2;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    msg.what = 2;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = 2;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    msg.what = 2;
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    msg.what = 3;//重新登录
                }
                //发送消息
                handler.sendMessage(msg);
            }
        }.start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //关闭对话框
            if (pDialog!=null){
                pDialog.dismiss();
                //清空data
                data.clear();
                switch (msg.what){
                    case 1:
                        //Passenger[]=>data
                        Passenger[] passengers=(Passenger[]) msg.obj;
                        for (Passenger passenger:passengers){
                            Map<String,Object> row=new HashMap<String,Object>();
                            row.put("name",passenger.getName()+"("+passenger.getType()+")");
                            row.put("idCard",passenger.getIdType()+":"+passenger.getId());
                            row.put("tel","电话:"+passenger.getTel());
                            data.add(row);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        Toast.makeText(MyContactActivity.this,"服务器错误，请重试",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MyContactActivity.this,"请重新登录",
                                Toast.LENGTH_SHORT).show();
                }
            }
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_userbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.find_user:
                //跳转添加新用户
                Intent intent=new Intent(MyContactActivity.this,MyContactNewActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


