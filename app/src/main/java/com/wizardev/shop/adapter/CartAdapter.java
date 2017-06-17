package com.wizardev.shop.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wizardev.shop.R;
import com.wizardev.shop.bean.Cart;
import com.wizardev.shop.customView.NumberAddSubView;

import org.litepal.crud.DataSupport;

import java.util.Iterator;
import java.util.List;

/**
 * Created by wizardev on 17-6-17.
 */

public class CartAdapter extends SimpleAdapter<Cart> implements BaseAdapter.OnItemClickListener {
    private CheckBox mCheckBox;
    private TextView mTotalPrice;
    private List<Cart> list;
    private TextView mItemPrice;
    private static final String TAG = "CartAdapter";

    public CartAdapter(Context mContext, List<Cart> list, int mResId, CheckBox checkBox, TextView textView) {
        super(mContext, list, mResId);
        this.mCheckBox = checkBox;
        this.mTotalPrice = textView;
        this.list = list;
        showTotalPrice();
        setOnItemClickListener(this);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectAllOrNot(mCheckBox.isChecked());
                showTotalPrice();

            }
        });
    }

    @Override
    public void bindView(final BaseViewHolder viewHolder, final Cart item) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_cart);
        simpleDraweeView.setImageURI(item.getImgUrl());
        ((CheckBox) viewHolder.getView(R.id.cb_select_cart)).setChecked(item.isSelect());
        viewHolder.getTextView(R.id.text_title_cart).setText(item.getName());
        mItemPrice = viewHolder.getTextView(R.id.text_price);
        mItemPrice.setText("￥" + item.getPrice());
        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHolder.getView(R.id.addOrSub);
        numberAddSubView.setValue(item.getCount());
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                item.update(item.getId());
                showTotalPrice();

            }

            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
                item.update(item.getId());
                showTotalPrice();
            }
        });
    }

    private float getTotalPrice() {
        float total = 0;
        if (list == null || list.isEmpty()) {
            return total;
        } else {

            for (Cart cart :
                    list) {
                if (cart.isSelect()) {
                    total += cart.getCount() * cart.getPrice();
                }

            }
            return total;
        }

    }

    public void showTotalPrice() {
        float total = getTotalPrice();
        mTotalPrice.setText(Html.fromHtml("合计￥<span style='color:#eb4f38'>" + total + "</span>"));
    }

    @Override
    public void onItemClick(View view, int position) {
        Cart cart = getItem(position);
        cart.setSelect(!cart.isSelect());
        notifyItemChanged(position);
        checkListen();
        showTotalPrice();
    }

    private void checkListen() {
        int count = 0;
        int checkNum = 0;
        if (list != null) {
            count = list.size();

            for (Cart cart : list) {
                if (!cart.isSelect()) {
                    mCheckBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }

            if (count == checkNum) {
                mCheckBox.setChecked(true);
            }

        }
    }

    public void selectAllOrNot(boolean isChecked) {
        if (list == null || list.isEmpty())
            return;
        int i = 0;
        for (Cart cart : list) {
            cart.setSelect(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    public void delCart() {
        if(list == null || list.isEmpty())
            return ;

//        for (ShoppingCart cart : datas){
//
//            if(cart.isChecked()){
//                int position = datas.indexOf(cart);
//                cartProvider.delete(cart);
//                datas.remove(cart);
//                notifyItemRemoved(position);
//            }
//        }


        for(Iterator iterator = list.iterator(); iterator.hasNext();){

            Cart cart = (Cart) iterator.next();
            if(cart.isSelect()){
                int position = list.indexOf(cart);
                DataSupport.delete(Cart.class,cart.getId());
                iterator.remove();
                notifyItemRemoved(position);
            }

        }
    }
}
