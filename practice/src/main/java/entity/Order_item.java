package entity;

import lombok.Data;

//浏览订单时用的
@Data
public class Order_item {
    private Integer id;
    private String order_id;
    private Integer goods_id;
    private String goods_name;
    private String goods_introduce;
    private Integer goods_num;
    private String goods_unit;
    private Integer goods_price;
    private Integer goods_discount;
}
