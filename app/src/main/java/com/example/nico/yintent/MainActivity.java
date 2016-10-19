package com.example.nico.yintent;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.nico.yintent.my.MyAccountActivity;
import com.example.nico.yintent.my.MyContactActivity;
import com.example.nico.yintent.my.MyPasswordActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    private ArrayList<Fragment> fragmentArrayList;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new Checkchange());
        viewPager=(ViewPager)findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(new onCheckchange());
        fragmentArrayList=new ArrayList<Fragment>();
        MyFragment myFragment=new MyFragment();
        OrderFragment orderFragment=new OrderFragment();
        TicketFragment ticketFragment=new TicketFragment();

        fragmentArrayList.add(myFragment);
        fragmentArrayList.add(orderFragment);
        fragmentArrayList.add(ticketFragment);
        viewPager.setAdapter(new MyFragerPagerAdapter(getSupportFragmentManager()));
    }
    class MyFragerPagerAdapter extends FragmentPagerAdapter {
        public MyFragerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size() ;
        }





    }
    class Checkchange implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int current=0;
            switch (checkedId){
                case R.id.shouye:
                    current=0;break;
                case R.id.xiexie:
                    current=1;break;
                case R.id.wozhui:
                    current=2;break;

            }
            if(viewPager.getCurrentItem()!=current){
                viewPager.setCurrentItem(current);
            }
        }
    }
    class onCheckchange implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {
            int current=viewPager.getCurrentItem();
            switch (current){
                case 0:
                    radioGroup.check(R.id.shouye);break;
                case 1:
                    radioGroup.check(R.id.xiexie);break;
                case 2:
                    radioGroup.check(R.id.wozhui);break;

            }
        }
    }

}
