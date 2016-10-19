package com.example.nico.yintent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.nico.yintent.Utils.CONSTANT;
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

public class SplashActivity extends AppCompatActivity {
    Handler handler=new Handler() {
        //回调方法

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    String jsessionid = (String) msg.obj;
                    int result = msg.arg1;
                    if (0 == result) {
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (1 == result) {
                        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        //记录JSESSIONID
                        editor.putString("Cookie", jsessionid);
                        editor.commit();

                        Intent intent=new Intent();
                        intent.setClass(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    break;
                case 2:
                    Toast.makeText(SplashActivity.this,"服务器错误，请重试",
                            Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去除标题行


        setContentView(R.layout.activity_splash);
        //自动登录
        SharedPreferences pref=getSharedPreferences("user", 0);
        final String username=pref.getString("username","");
        final String password=pref.getString("password","");
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Intent intent=new Intent();
            intent.setClass(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            if (!NetUtils.check(SplashActivity.this)){
                Toast.makeText(SplashActivity.this,
                       getString(R.string.network_check),Toast.LENGTH_SHORT).show();
                return;//后续代码不执行

            }
            new Thread(){
              public void run(){
                  Message msg=handler.obtainMessage();
                  //访问服务器锻，验证用户名/密码
                  HttpPost post=new HttpPost(CONSTANT.HOST+"/Login");
                  //设置参数
                  List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();
                  params.add(new BasicNameValuePair("username",username));
                  params.add(new BasicNameValuePair("password",password));
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
                          }//发送消息
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
              }

    }
}
