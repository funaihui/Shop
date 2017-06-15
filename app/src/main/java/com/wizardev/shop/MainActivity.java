package com.wizardev.shop;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.wizardev.shop.fragment.CartFragment;
import com.wizardev.shop.fragment.CategoryFragment;
import com.wizardev.shop.fragment.HomeFragment;
import com.wizardev.shop.fragment.HotFragment;
import com.wizardev.shop.fragment.MineFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost fragmentTabHost;
    private String texts[] = {"首页", "类别", "热卖", "购物车", "我的"};
    private int imageButton[] = {R.drawable.selector_icon_home,
            R.drawable.selector_icon_category, R.drawable.selector_icon_hot,
            R.drawable.selector_icon_cart, R.drawable.selector_icon_mine};
    private Class fragmentArray[] = {HomeFragment.class, CategoryFragment.class,
            HotFragment.class, CartFragment.class, MineFragment.class};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏颜色
        Statebar.setStatuebarColor(MainActivity.this,getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_main);
        // 实例化tabhost
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.maincontent);
        fragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        for (int i = 0; i < texts.length; i++) {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));
            fragmentTabHost.addTab(spec, fragmentArray[i], null);

        }
    }

    private View getView(int i) {
        //取得布局实例
        View view = View.inflate(MainActivity.this, R.layout.tab_indicator, null);

        //取得布局对象
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_tab);
        TextView textView = (TextView) view.findViewById(R.id.txt_indicator);

        //设置图标
        imageView.setImageResource(imageButton[i]);
        //设置标题
        textView.setText(texts[i]);
        return view;
    }
}
