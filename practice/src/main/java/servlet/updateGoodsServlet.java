
package servlet;

import entity.Goods;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/updategoods")
public class updateGoodsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("text/html;charset=utf-8");

        System.out.println("我进来了");
        String goodsID = req.getParameter("goodsID");
        String name = req.getParameter("name");
        String stock = req.getParameter("stock");
        String introduce = req.getParameter("introduce");
        String unit = req.getParameter("unit");
        String price = req.getParameter("price");
        String discount = req.getParameter("discount");

        int goodsId = Integer.valueOf(goodsID);
        int realPrice = new Double(Double.valueOf(price)*100).intValue();
        Writer writer = resp.getWriter();

        Goods good = getGoods(goodsId);
        if(good==null){
            writer.write("<h1>此商品不存在</h1>");
        }else{
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                String sql = "update goods set name=?, stock=?,introduce=?,unit=?,price=?,discount=? where id=?";
                con = DBUtil.getConnection(true);
                ps = con.prepareStatement(sql);
                ps.setString(1,name);
                ps.setInt(2,Integer.valueOf(stock));
                ps.setString(3,introduce);
                ps.setString(4,unit);
                ps.setInt(5,realPrice);
                ps.setInt(6,Integer.valueOf(discount));
                ps.setInt(7,goodsId);
                int ret = ps.executeUpdate();
                if(ret!=0){
                    writer.write("<h1>商品更新成功</h1>");
                    resp.sendRedirect("goodsbrowse.html");
                }else{
                    writer.write("<h1>商品更新失败</h1>");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                DBUtil.close(con,ps,rs);
            }
        }

    }
    private Goods getGoods(int id){
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