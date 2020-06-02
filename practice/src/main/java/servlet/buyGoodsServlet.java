
package servlet;

import commen.OrderStatus;
import entity.Account;
import entity.Goods;
import entity.Order;
import entity.OrderItem;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//下单
@WebServlet("/buyGoodsServlet")
public class buyGoodsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf8");

        HttpSession session = req.getSession();
        Order order = (Order) session.getAttribute("order");
        List<Goods> goodsList = (List<Goods>)session.getAttribute("goodsList");
        order.setOrder_status(OrderStatus.OK);
        //设置订单完成时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss"
        );
        String finishTime = format.format(date);
        order.setFinish_time(finishTime);

        //提交订单(写入浏览订单)
        boolean flg = commitOrder(order);

        //更新库存
        if(flg) {
            //更新库存
            for ( Goods goods : goodsList) {
                boolean isUpdate =updateAfterPay(goods,goods.getBuyGoodsNum());
                if(isUpdate) {
                    System.out.println("更新库存成功！");
                }else {
                    System.out.println("更新库存失败");
                    return;
                }
            }
            //清空购物车
            Account account = (Account)session.getAttribute("user");
            account.getMylist().clear();
        }else {
            System.out.println(flg);
            System.out.println("插入订单失败！2");
            return;
        }
        resp.sendRedirect("buyGoodsSuccess.html");
    }
    private boolean updateAfterPay(Goods goods, int buyGoodsNum){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "update goods set stock=? where id=?";
            con = DBUtil.getConnection(true);
            ps = con.prepareStatement(sql);
            ps.setInt(1,goods.getStock()-goods.getBuyGoodsNum());
            ps.setInt(2,goods.getId());
            int ret = ps.executeUpdate();
            if(ret==0){
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();

        }finally {
            DBUtil.close(con,ps,null);
        }
        return true;
    }
    public boolean commitOrder(Order order) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            String insertOrderSql = "insert into `order`" +
                    "(id, account_id, create_time, finish_time, " +
                    "actual_amount, total_money, order_status, " +
                    "account_name) values (?,?,?,?,?,?,?,?)";

            connection = DBUtil.getConnection(false);
            ps = connection.prepareStatement(insertOrderSql);

            ps.setString(1,order.getId());
            ps.setInt(2,order.getAccount_id());
            ps.setString(3,order.getCreate_time());
            ps.setString(4,order.getFinish_time());

            ps.setInt(5,order.getActualAmountInt());
            //ps.setInt(4,order.getTotal_money_fen());
            ps.setInt(6,order.getTotalMoneyInt());

            ps.setInt(7,order.getOrder_status().getFlg());
            ps.setString(8,order.getAccount_name());

            int ret = ps.executeUpdate();
            if(ret==0){
                System.out.println("插入订单失败！提交时");
                throw new RuntimeException("插入订单失败！提交时");
            }


            String insertOrderItemSql = "insert into order_item(order_id, goods_id, goods_name," +
                    "goods_introduce, goods_num, goods_unit," +
                    " goods_price, goods_discount) " +
                    "values (?,?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(insertOrderItemSql);

            for (OrderItem orderItem : order.getOrderItemList()) {
                ps.setString(1,orderItem.getOrderId());
                ps.setInt(2,orderItem.getGoodsId());
                ps.setString(3,orderItem.getGoodsName());
                ps.setString(4,orderItem.getGoodsIntroduce());
                ps.setInt(5,orderItem.getGoodsNum());
                ps.setString(6,orderItem.getGoodsUnit());
                ps.setInt(7,orderItem.getGoodsPriceInt());
                ps.setInt(8,orderItem.getGoodsDiscount());
                ps.addBatch();// 缓存
            }

            int[] effect = ps.executeBatch();//批量的插入
            for ( int i : effect) {
                if(i == 0) {
                    throw new RuntimeException("插入订单明细失败！");
                }
            }
            //批量插入没有发生任何的异常
            connection.commit();
        }catch (Exception e) {
            e.printStackTrace();
            //判断链接是否断开
            if(connection != null) {
                try {
                    //回滚
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        }finally {
            DBUtil.close(connection,ps,null);
        }
        return true;
    }

}