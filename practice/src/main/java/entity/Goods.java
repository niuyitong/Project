package entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Goods {
    private Integer id;
    private String name;
    private String introduce;
    private Integer stock;//库存
    private String unit;//单位
    private Integer price;//商品价格  12.34  -》 1234
    private Integer discount;//折扣

    private Integer buyGoodsNum;//买的东西的数量


    //实际的价格
    public double getPrice() {
        return price * 1.0 / 100;
        //return price;
    }
    //这个是返回整数的价格
    public int getPriceInt() {
        return price;
    }



}
