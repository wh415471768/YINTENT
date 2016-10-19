package com.example.nico.yintent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nico.yintent.Utils.CONSTANT;
import com.example.nico.yintent.Utils.NetUtils;
import com.example.nico.yintent.my.MyAccountActivity;
import com.example.nico.yintent.my.MyContactActivity;
import com.example.nico.yintent.my.MyPasswordActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends android.support.v4.app.Fragment {

    Button btnLogout=null;
    ListView lvMylist=null;
    ProgressDialog pDialog=null;
    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnLogout=(Button)getActivity().findViewById(R.id.btnLogout);
        lvMylist=(ListView) getActivity().findViewById(R.id.lvMylist);

        String[] data=getResources().getStringArray(R.array.my_list_data);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,data
        );
        lvMylist.setAdapter(adapter);
        lvMylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(getActivity(), MyContactActivity.class);
                        break;
                    case 1:
                        intent.setClass(getActivity(), MyAccountActivity.class);
                        break;
                    case 2:
                        intent.setClass(getActivity(), MyPasswordActivity.class);
                }
                startActivity(intent);
            }
        });

        MyButtonListener listener=new MyButtonListener();

        btnLogout.setOnClickListener(listener);


    }
    class MyButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            if (!NetUtils.check(getActivity())){
                Toast.makeText(getActivity(),getString(R.string.network_check),Toast.LENGTH_SHORT).show();
                return;
            }
            new LogoutTask().execute();
            /*Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();*/
            /*switch (v.getId()) {
                case R.id.btnContact:
                    intent.setClass(MainActivity.this, MyContactActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnAccount:
                    intent.setClass(MainActivity.this, MyAccountActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnPassword:
                    intent.setClass(MainActivity.this, MyPasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnLogout:
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;

            }
        */
        }

    }
    class LogoutTask extends AsyncTask<String,String,String>{
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=ProgressDialog.show(getActivity(),null,"正在加载中...",
                    false,true);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=null;
            HttpPost post=new HttpPost(CONSTANT.HOST+"/otn/Logout");
            //发送请求
            DefaultHttpClient client=new DefaultHttpClient();
            try {
                //设置jsessionid
                SharedPreferences pref=getActivity().getSharedPreferences(
                        "user", Context.MODE_PRIVATE);
                String value=pref.getString("Cookie","");
                BasicHeader header=new BasicHeader("Cookie",value);
                post.setHeader(header);
                //超时设置
                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                        CONSTANT.REQUEST_TIMEOUT);
                client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                        CONSTANT.SO_TIMEOUT);

                HttpResponse response = client.execute(post);
                //处理结果
                if (response.getStatusLine().getStatusCode() == 200) {
                    result= EntityUtils.toString(response.getEntity());
                    Log.d("My12306","Logout:"+result);
                }
                client.getConnectionManager().shutdown();
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
            return result;
        }
        protected void  onPostExecute(String result){
            super.onPostExecute(result);
            if (pDialog!=null)
                pDialog.dismiss();
            if ("\"1\"".equals(result)){
                Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }else if ("\"0\"".equals(result)){
                Toast.makeText(getActivity(),"退出登录失败",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(),"服务器错误，请重试",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}
