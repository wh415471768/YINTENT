package com.example.nico.yintent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketDetailsStep2Activity extends AppCompatActivity {
    ListView lvTicketDetailsStep2;
    List<Map<String, Object>> data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details_step2);

        lvTicketDetailsStep2 = (ListView) findViewById(R.id.lvTicketDetailStep2);
        data = new ArrayList<Map<String, Object>>();
        Map<String, Object> row1 = new HashMap<String, Object>();
        row1.put("seatName", "软座");
        row1.put("seatNum", "200张");
        row1.put("seatPrice", "￥188.5");
        data.add(row1);

        Map<String, Object> row2 = new HashMap<String, Object>();
        row2.put("seatName", "硬座");
        row2.put("seatNum", "88张");
        row2.put("seatPrice", "￥148.8");
        data.add(row2);

        Map<String, Object> row3 = new HashMap<String, Object>();
        row3.put("seatName", "无座");
        row3.put("seatNum", "100张");
        row3.put("seatPrice", "￥148.5");
        data.add(row3);
        lvTicketDetailsStep2.setAdapter(new TicketDetailsStep2Adapter());
    }

    class TicketDetailsStep2Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {

                holder = new ViewHolder();
                //创建convertView加载行布局
                convertView = LayoutInflater.from(TicketDetailsStep2Activity.this).inflate(R.layout.item_ticket_details_step2, null);
                holder.tvTicketDetailsStep2SeatName = (TextView) convertView.findViewById(R.id.tvTicketDetailsStep2SeatName);
                holder.tvTicketDetailsStep2SeatNum = (TextView) convertView.findViewById(R.id.tvTicketDetailsStep2SeatNum);
                holder.tvTicketDetailsStep2SeatPrice = (TextView) convertView.findViewById(R.id.tvTicketDetailsStep2SeatPrice);
                holder.btnTicketDetailsStep2Order = (Button) convertView.findViewById(R.id.btnTicketDetailsStep2Order);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();

            }
            holder.tvTicketDetailsStep2SeatName.setText(data.get(position).get("seatName").toString());
            holder.tvTicketDetailsStep2SeatNum.setText(data.get(position).get("seatNum").toString());
            holder.tvTicketDetailsStep2SeatPrice.setText(data.get(position).get("seatPrice").toString());
            holder.btnTicketDetailsStep2Order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TicketDetailsStep2Activity.this, "button click", Toast.LENGTH_SHORT).show();

                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tvTicketDetailsStep2SeatName;
            TextView tvTicketDetailsStep2SeatNum;
            TextView tvTicketDetailsStep2SeatPrice;
            Button btnTicketDetailsStep2Order;
        }
    }
}
