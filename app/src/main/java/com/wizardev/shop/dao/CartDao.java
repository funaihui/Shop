package com.wizardev.shop.dao;

import android.database.Cursor;
import android.util.Log;

import com.wizardev.shop.bean.Cart;
import com.wizardev.shop.bean.Wares;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by wizardev on 17-6-16.
 */

public class CartDao {
    private static final String TAG = "CartDao";

    public  void add2Cart(Wares wares){
        Cart cart = new Cart();
        if (selectAllShop()==null){
            Log.i(TAG, "add2Cart: null");
            cart.setCount(1);
            cart.setDescription(wares.getDescription());
            cart.setImgUrl(wares.getImgUrl());
            cart.setName(wares.getName());
            cart.setPrice(wares.getPrice());
            cart.setProductId(wares.getId().intValue());
            cart.setSelect(true);
            cart.save();
        }else {
            Cart oneCart = selectOne(wares.getId().intValue());
            if (oneCart==null){
                Log.i(TAG, "oneCart: null");
                cart.setCount(1);
                cart.setDescription(wares.getDescription());
                cart.setImgUrl(wares.getImgUrl());
                cart.setName(wares.getName());
                cart.setPrice(wares.getPrice());
                cart.setProductId(wares.getId().intValue());
                cart.setSelect(true);
                cart.save();
            }else {

                int count = oneCart.getCount();
                Log.i(TAG, "add2Cart: "+count);
                cart.setCount(count+1);
                cart.update(oneCart.getId());
            }
        }
    }

    public  List<Cart> selectAllShop(){
        List<Cart> cartList = null;
        cartList = DataSupport.findAll(Cart.class,true);
        if (cartList!=null&&!cartList.isEmpty()){
            return cartList;
        }
        return cartList;
    }
    public  Cart selectOne(int productId){
        Cart cart = null;
        Cursor cursor = DataSupport.findBySQL("select * from cart where productId=" + productId);
        if (cursor.moveToNext()){
            cart = new Cart();
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            Log.i(TAG, "selectOne: "+count);
            cart.setId(id);
            cart.setCount(count);
            return cart;
        }
        return cart;
    }
}
