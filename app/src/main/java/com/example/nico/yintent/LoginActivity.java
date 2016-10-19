package com.example.nico.yintent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nico.yintent.Utils.CONSTANT;
import com.example.nico.yintent.Utils.Md5Utils;
import com.example.nico.yintent.Utils.NetUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button btnlogin;
    TextView tvLostPassword;
    EditText editUser=null;
    EditText editPassword=null;
    CheckBox ckLogin=null;
    ProgressDialog pDialog=null;
    Handler handler=new Handler(){
        //回调方法

        public void  handleMessage(android.os.Message msg){
           /* String str=(String)msg.obj;
            if (!TextUtils.isEmpty(str)){
                editUser.setError(str);
                editUser.requestFocus();
            }*/
            //关闭对话框
            if (pDialog!=null){
                pDialog.dismiss();
            }
            switch (msg.what){
                case 1:
                    String jsessionid=(String)msg.obj;
                    int result=msg.arg1;
                    if(0==result){
                        editUser.selectAll();
                        editUser.setError("用户名或密码错误");
                        editUser.requestFocus();
                    }else if (1==result){
                        SharedPreferences pref=getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        //记录JSESSIONID
                        editor.putString("Cookie",jsessionid);
                        //记录用户名或密码
                        if (ckLogin.isChecked()){
                            editor.putString("username",editUser.getText().toString());
                            editor.putString("password", Md5Utils.MD5(editPassword.getText().toString()));
                        }else {
                            //清空以前的登录信息
                            editor.remove("username");
                            editor.remove("password");
                        }
                        editor.commit();
                        //显示意图
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        //关闭LoginActivity
                        LoginActivity.this.finish();
                    }
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this,"服务器错误，请重试",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editPassword=(EditText)findViewById(R.id.editPassword);
        editUser=(EditText)findViewById(R.id.editUser);
        ckLogin=(CheckBox)findViewById(R.id.ckLogin);
        btnlogin=(Button)findViewById(R.id.btnlogin);
        tvLostPassword=(TextView)findViewById(R.id.tvLostPassword);
        tvLostPassword.setText(Html.fromHtml("<a href=\"http://www.12306.cn/LostPassword\">忘记密码</a>"));
        tvLostPassword.setMovementMethod(LinkMovementMethod.getInstance());

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("YINTENT", "Login Button Click");
                if (TextUtils.isEmpty(editUser.getText().toString())){
                    editUser.setError("请输入用户名");
                    editUser.requestFocus();
                }
                else if (TextUtils.isEmpty(editPassword.getText().toString())){
                    editPassword.setError("请输入密码");
                    editPassword.requestFocus();
                }
                else {
                    if (!NetUtils.check(LoginActivity.this)){
                        Toast.makeText(LoginActivity.this,getString(R.string.network_check),Toast.LENGTH_SHORT).show();
                        return;//后续代码不执行
                    }
                    //进度对话框
                    pDialog= ProgressDialog.show(LoginActivity.this,null,
                            "正在加载中...",false,true);
                    /*if (ckLogin.isChecked()){
                        SharedPreferences pref=getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("username",editUser.getText().toString());
                        editor.putString("password",editPassword.getText().toString());
                        editor.commit();
                    }
                    else {
                        SharedPreferences pref=getSharedPreferences("user",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.commit();
                    }*/

                    new Thread(){

                        public void run(){
                            Message msg=handler.obtainMessage();
                            msg.obj="用户名或密码错误!";

                            //访问服务器端，验证用户名/密码
                            HttpPost post = new HttpPost(CONSTANT.HOST + "/Login");
                            //设置参数
                            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                            params.add(new BasicNameValuePair("username", editUser.getText().toString()));
                            params.add(new BasicNameValuePair("password", Md5Utils.MD5(editPassword.getText().toString())));

                            UrlEncodedFormEntity entity;
                            try {
                                entity = new UrlEncodedFormEntity(params, "UTF-8");
                                post.setEntity(entity);
                                //发送请求
                                DefaultHttpClient client = new DefaultHttpClient();
                                //超时设置
                                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                                        CONSTANT.REQUEST_TIMEOUT);
                                client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                                        CONSTANT.SO_TIMEOUT);

                                HttpResponse response = client.execute(post);

                                //处理结果
                                if (response.getStatusLine().getStatusCode() == 200) {
                                    //打印响应的结果
                                  //  Log.v("My12306", EntityUtils.toString(response.getEntity()));
                                //xml解析
                                    XmlPullParser parser= Xml.newPullParser();//pull解析器
                                    parser.setInput(response.getEntity().getContent(),"UTF-8");
                                    int type=parser.getEventType();
                                    String result=null;
                                    while (type!=XmlPullParser.END_DOCUMENT){
                                        switch (type){
                                            case XmlPullParser.START_TAG:
                                                if ("result".equals(parser.getName())){
                                                    result=parser.nextText();
                                                    Log.d("My12306","result:"+result);
                                                }
                                                break;
                                        }
                                        type=parser.next();
                                    }
                                    //记录JSESSIONID
                                    String value="";
                                    List<Cookie> cookies=client.getCookieStore().getCookies();
                                    for (Cookie cookie:cookies){
                                        if ("JSESSIONID".equals(cookie.getName())){
                                            value=cookie.getValue();
                                            Log.d("My12306","JSESSIONID:"+value);
                                            break;
                                        }
                                    }
                                    //发送消息
                                   msg.what=1;
                                   msg.arg1=Integer.parseInt(result);
                                   msg.obj="JSESSIONID="+value;
                                }else {
                                    msg.what=2;
                                }
                                //关闭连接
                                client.getConnectionManager().shutdown();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                msg.what=2;
                            } catch (ClientProtocolException e) {
                                e.printStackTrace();
                                msg.what=2;
                            } catch (IOException e) {
                                e.printStackTrace();
                                msg.what=2;
                            }catch (XmlPullParserException e){
                                e.printStackTrace();
                                msg.what=2;
                            }


                            //假设验证失败

                            //Message msg=handler.obtainMessage();
                            //msg.obj="用户名或密码错误!";
                            handler.sendMessage(msg);

                        };
                    }.start();
                //显示意图
                //Intent intent = new Intent();
                //intent.setClass(LoginActivity.this, MainActivity.class);
                //startActivity(intent);
                //关闭LoginActivity
                //finish();
                }
            }
        });
    }
}
