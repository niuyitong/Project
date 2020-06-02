package entity;

import lombok.Data;

//选商品时打印小票用的
@Data
public class OrderItem {
    private Integer id;
    private String orderId;
    private Integer goodsId;
    private String goodsName;
    private String goodsIntroduce;
    private Integer goodsNum;
    private String goodsUnit;
    private Integer goodsPrice;
    private Integer goodsDiscount;

    public double getGoodsPrice() {
        return goodsPrice * 1.0 / 100;
    }

    public int getGoodsPriceInt() {
        return goodsPrice;
    }
}
