package com.example.nico.yintent.my;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.nico.yintent.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyContactEditActivity extends AppCompatActivity {
    ListView lvMyContactEdit;
    List<Map<String,Object>> data=null;
    SimpleAdapter adapter=null;
    Button btnMyContactEditSave=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lvMyContactEdit=(ListView)findViewById(R.id.lvMyContactEdit);
        btnMyContactEditSave=(Button)findViewById(R.id.btnMyContactEditSave);
        //接收数据
        Intent intent=getIntent();
        Map<String,Object> contact=(Map<String,Object>) intent.getSerializableExtra("row");
        //数据
        data=new ArrayList<Map<String,Object>>();
        //row1:姓名
        Map<String,Object> row1=new HashMap<String,Object>();
        String name=(String)contact.get("name");
        row1.put("key1","姓名");
        row1.put("key2",name.split("\\(")[0]);
        row1.put("key3",R.drawable.forward_25);
        data.add(row1);
        //row2:证件类型
        Map<String,Object> row2=new HashMap<String,Object>();
        String idCard=(String)contact.get("idCard");
        row2.put("key1","证件类型");
        row2.put("key2",idCard.split(":")[0]);
        row2.put("key3",null);
        data.add(row2);
        //row3:证件号码
        Map<String,Object> row3=new HashMap<String,Object>();
        row3.put("key1","证件号码");
        row3.put("key2",idCard.split(":")[1]);
        row3.put("key3",null);
        data.add(row3);
        //row4:乘客类型
        Map<String,Object> row4=new HashMap<String,Object>();
        row4.put("key1","乘客类型");
        row4.put("key2",name.split("\\(")[1].split("\\)")[0]);
        row4.put("key3",R.drawable.forward_25);
        data.add(row4);
        //row5:电话
        Map<String,Object> row5=new HashMap<String,Object>();
        final String  tel=(String)contact.get("tel");
        row5.put("key1","电话");
        row5.put("key2",tel.split(":")[1]);
        row5.put("key3", R.drawable.forward_25);
        data.add(row5);
        //适配器
        adapter=new SimpleAdapter(MyContactEditActivity.this,data,
                R.layout.item_my_contact_edit,new String[]{"key1","key2","key3"},
                new int[]{R.id.tvMyContactEditKey,R.id.tvMyContactEditValue,R.id.imMyContactEditFlag});
        lvMyContactEdit.setAdapter(adapter);

        lvMyContactEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (position) {
                    case 0:
                        final EditText editName = new EditText(MyContactEditActivity.this);
                        editName.setText((String) (data.get(position).get("key2")));
                        editName.selectAll();

                        new AlertDialog.Builder(MyContactEditActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("请输入姓名").setView(editName)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String name = editName.getText().toString();
                                                if (TextUtils.isEmpty(name)) {
                                                    setClosable(dialog,false);
                                                    editName.setError("请输入姓名");
                                                    editName.requestFocus();
                                                } else {
                                                    setClosable(dialog,true);
                                                    data.get(position).put("key2",
                                                            editName.getText().toString());
                                                    //更新ListView
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        }).setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setClosable(dialog,true);
                                    }
                                }).show();
                        break;
                    case 3:
                        final String[] types = new String[]{"成人", "学生", "儿童", "其他"};
                        String key2 = (String) (data.get(position).get("key2"));
                        int idx = 0;
                        for (int i = 0; i < types.length; i++) {
                            if (types[1].equals(key2)) {
                                idx = i;
                                break;
                            }
                        }
                        new AlertDialog.Builder(MyContactEditActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert).setTitle("请选择乘客类型")
                                .setSingleChoiceItems(types, idx,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                data.get(position).put("key2", types[which]);
                                                adapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        }).setNegativeButton("取消", null).show();
                        break;
                    case 4:
                        final EditText editTel = new EditText(MyContactEditActivity.this);
                        editTel.setText((String) (data.get(position).get("key2")));
                        editTel.selectAll();

                        new AlertDialog.Builder(MyContactEditActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("请输入电话号").setView(editTel)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String tel = editTel.getText().toString();
                                                if (TextUtils.isEmpty(tel)) {
                                                    setClosable(dialog,false);
                                                    editTel.setError("请输入电话号");
                                                    editTel.requestFocus();
                                                } else {
                                                    setClosable(dialog,true);
                                                    data.get(position).put("key2",
                                                            editTel.getText().toString());
                                                    //更新ListView
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        }).setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setClosable(dialog,true);
                                    }
                                }).show();
                        break;
                }
            }
        });
        btnMyContactEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1将数据保存到服务上//2finish()
                finish();
            }
        });
    }
    private void setClosable(DialogInterface dialog,boolean b){
        Field field;
        try {
            field=dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,b);
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
