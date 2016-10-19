package com.example.nico.yintent.my;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.nico.yintent.R;

import java.util.ArrayList;
import java.util.List;

public class MyContactNewActivity extends AppCompatActivity {
    private TextView tvMyContactNew1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contact_new);
        tvMyContactNew1=(TextView)findViewById(R.id.tvMyContactNew1);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_userbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.find_user:

                ContentResolver cr=getContentResolver();
                Cursor c=cr.query(ContactsContract.Contacts.CONTENT_URI,
                        new String[]{"_id", "display_name"}, null, null, null);
                List<String> contacts=new ArrayList<String>();
                while (c.moveToNext()){
                    int _id=c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
                    String display_name=c.getString(c.getColumnIndex("display_name"));
                    /*String phone=c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    Log.e("My12306","phone:"+phone);*/

                    //查找电话
                    Cursor c2=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    +"=?",new String[]{_id+""},null);
                    String number=null;
                    while(c2.moveToNext()){
                        number=c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    c2.close();
                    contacts.add(display_name+"("+number.replaceAll("","").replaceAll("-","")+")");
                }
                c.close();
                if (contacts.size()==0){
                    new AlertDialog.Builder(MyContactNewActivity.this).setTitle("请选择").setMessage("通讯录为空")
                            .setNegativeButton("取消",null).show();
                }else {
                    final String[] items=new String[contacts.size()];
                    contacts.toArray(items);

                    new AlertDialog.Builder(MyContactNewActivity.this)
                            .setTitle("请选择")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tvMyContactNew1.setText(items[which]);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消",null).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
