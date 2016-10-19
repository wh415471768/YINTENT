package com.example.nico.yintent.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/8/30.
 */
public class NetUtils {
    public static boolean check(Context context){
        try {
            ConnectivityManager connectivity=(ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity!=null){
                //获取网络连接管理的对象
                NetworkInfo info=connectivity.getActiveNetworkInfo();
                //判断当前网络是否已连接
                if (info.getState()==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
