package com.wizardev.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wizardev.shop.R;
import com.wizardev.shop.adapter.CartAdapter;
import com.wizardev.shop.bean.Cart;
import com.wizardev.shop.customView.MyToolbar;
import com.wizardev.shop.dao.CartDao;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


public class CartFragment extends Fragment implements View.OnClickListener {
    @ViewInject(R.id.select_all)
    private CheckBox mCheckBox;
    @ViewInject(R.id.total_price)
    private TextView mTotalPriceText;
    private CartDao cartDao;
    private List<Cart> carts;
    @ViewInject(R.id.rv_cart)
    private RecyclerView mCartRecycleView;
    @ViewInject(R.id.mytoolbar)
    private MyToolbar myToolbar;
    @ViewInject(R.id.bt_buy)
    private Button mBuyButton;
    @ViewInject(R.id.btn_del)
    private Button mDeleteButton;
    private static final int EDIT=1;
    private static final int FINISH=2;
    private Button mRightButton;
    private CartAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        x.view().inject(this,view);
        getCartDatas();
        showRightButton();
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.delCart();
            }
        });
        return view;
    }

    public void getCartDatas() {
        cartDao = CartDao.getInstance();
        carts = cartDao.selectAllShop();
        showProductFromLocal();
    }

    private void showProductFromLocal() {
        if (carts!=null&&!carts.isEmpty()){
            mAdapter = new CartAdapter(getContext(),carts, R.layout.cart_item,mCheckBox,mTotalPriceText);
            mCartRecycleView.setAdapter(mAdapter);
            mCartRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            mCartRecycleView.setHasFixedSize(true);
           // mCartRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL_LIST));
        }
    }
    private void showRightButton(){
        myToolbar.setRightButtonText(getString(R.string.edit));
        mRightButton = myToolbar.getRightButton();
        mRightButton.setOnClickListener(this);
        mRightButton.setTag(EDIT);

    }
    private void showDelControl(){
        mRightButton.setText("完成");
        mTotalPriceText.setVisibility(View.GONE);
        mBuyButton.setVisibility(View.GONE);
        mDeleteButton.setVisibility(View.VISIBLE);
        mRightButton.setTag(FINISH);
        mAdapter.selectAllOrNot(false);
        mCheckBox.setChecked(false);

    }

    private void  hideDelControl(){

        mTotalPriceText.setVisibility(View.VISIBLE);
        mBuyButton.setVisibility(View.VISIBLE);


        mDeleteButton.setVisibility(View.GONE);
        mRightButton.setText(getString(R.string.edit));
        mRightButton.setTag(EDIT);
        mAdapter.selectAllOrNot(true);
        mAdapter.showTotalPrice();
        mCheckBox.setChecked(true);
    }




    @Override
    public void onClick(View v) {


        int tag = (int) v.getTag();
        if(EDIT == tag){

            showDelControl();
        }
        else if(FINISH == tag){

            hideDelControl();
        }



    }
}
