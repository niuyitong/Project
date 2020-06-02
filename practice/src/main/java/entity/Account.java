package entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Account {
    private Integer id;
    private String username;
    private String password;

    public List<Goods> mylist = new ArrayList<>();//我的购物车
}
