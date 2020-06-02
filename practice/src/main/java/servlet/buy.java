package servlet;

import commen.OrderStatus;
import entity.Account;
import entity.Goods;
import entity.Order;
import entity.OrderItem;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class buy {
    public static void buyGoods(String s, HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String[] strs = s.split(",");
        List<Goods> goodsList = new ArrayList<>();

        Writer writer = resp.getWriter();

        for(int i=0; i<strs.length; i++){
            String[] goods = strs[i].split("-");
            Goods good = getGoods(Integer.valueOf(goods[0]));
            if(good!=null){
                int num = good.getStock();
                if(num<Integer.valueOf(goods[1])){
                    writer.write("<p>存货不足，购买失败</p>");
                    writer.write("<a href=\"index.html\">点击回到主界面</a>");
                    return;
                }else{
                    good.setBuyGoodsNum(Integer.valueOf(goods[1]));
                    goodsList.add(good);
                }
            }else{
                writer.write("<p>"+goods[0]+"此商品不存在</p>");
            }
        }
        //创建订单
        Order order = new Order();
        order.setId(String.valueOf(System.currentTimeMillis()));
        HttpSession session = req.getSession();
        Account account = (Account)session.getAttribute("user");
        order.setAccount_id(account.getId());
        order.setAccount_name(account.getUsername());
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss"
        );
        String createTime = format.format(date);
        order.setCreate_time(createTime);

        //订单项
        int totalMoney = 0;
        int actualMoney = 0;
        for (Goods good : goodsList ){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setGoodsId(good.getId());
            orderItem.setGoodsName(good.getName());
            orderItem.setGoodsIntroduce(good.getIntroduce());
            orderItem.setGoodsNum(good.getBuyGoodsNum());
            orderItem.setGoodsUnit(good.getUnit());
            orderItem.setGoodsPrice(good.getPriceInt());
            orderItem.setGoodsDiscount(good.getDiscount());
            order.orderItemList.add(orderItem);
            int money = good.getBuyGoodsNum()*good.getPriceInt();

            totalMoney += money;
            actualMoney = totalMoney*good.getDiscount()/100;
        }
        order.setTotal_money(totalMoney);
        order.setActual_amount(actualMoney);
        order.setOrder_status(OrderStatus.PLAYING);
        session.setAttribute("order",order);
        session.setAttribute("goodsList",goodsList);

        resp.getWriter().println("<html>");
        resp.getWriter().println("<p>"+"【用户名称】:"+order.getAccount_name()+"</p>");
        resp.getWriter().println("<p>"+"【订单编号】:"+order.getId()+"</p>");
        resp.getWriter().println("<p>"+"【订单状态】:"+order.getOrder_statusDesc()+"</p>");
        resp.getWriter().println("<p>"+"【创建时间】:"+order.getCreate_time()+"</p>");

        resp.getWriter().println("<p>"+"编号  "+"名称   "+"数量  "+"单位  "+"单价（元）   "+"</p>");
        resp.getWriter().println("<ol>");
        for (OrderItem orderItem  : order.orderItemList) {
            resp.getWriter().println("<li>" + orderItem.getGoodsName() +" " + orderItem.getGoodsNum()+ " "+
                    orderItem.getGoodsUnit()+" " + orderItem.getGoodsPrice()+"</li>");
        }
        resp.getWriter().println("</ol>");
        resp.getWriter().println("<p>"+"【总金额】:"+order.getTotal_money() +"</p>");
        resp.getWriter().println("<p>"+"【优惠金额】:"+order.getDiscount() +"</p>");
        resp.getWriter().println("<p>"+"【应支付金额】:"+order.getActual_amount() +"</p>");
        //这个标签<a href = > 只会以get方式请求，所以buyGoodsServlet的 doGet方法
        resp.getWriter().println("<a href=\"buyGoodsServlet\">确认</a>");
        //resp.getWriter().println("<form action=\"buyGoodsServlet\" method=\"post\"><button type=\"submit\">确认</button></form>");
        resp.getWriter().println("<a href= \"index.html\">取消</a>");
        resp.getWriter().println("</html>");
    }
    public static Goods getGoods(int id){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Goods goods = null;

        try {
            String sql = "select * from goods where id = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);

            rs = ps.executeQuery();

            if(rs.next()) {
                goods = new Goods();
                goods.setId(rs.getInt("id"));
                goods.setName(rs.getString("name"));
                goods.setIntroduce(rs.getString("introduce"));
                goods.setStock(rs.getInt("stock"));
                goods.setUnit(rs.getString("unit"));
                goods.setPrice(rs.getInt("price"));
                goods.setDiscount(rs.getInt("discount"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,ps,rs);
        }
        return goods;
    }
}
