package servlet;

import util.DBUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;

//上架商品
@WebServlet("/inbound")
public class inboundServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("Text/html;charset=utf-8");
        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stock = req.getParameter("stock");
        String unit = req.getParameter("unit");
        String price = req.getParameter("price");
        String discount = req.getParameter("discount");

        int actualStock = Integer.valueOf(stock);
        int doublePrice = new Double((Double.valueOf(price)*100)).intValue();
        int discountInt = Integer.valueOf(discount);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Writer writer = resp.getWriter();

        try{
            String sql = "insert into goods(name,introduce,stock,unit,price,discount)values(?,?,?,?,?,?)";
            con = DBUtil.getConnection(true);
            ps = con.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2,introduce);
            ps.setInt(3,actualStock);
            ps.setString(4,unit);
            ps.setInt(5,doublePrice);
            ps.setInt(6,discountInt);
            int ret = ps.executeUpdate();
            System.out.println(doublePrice);
            if(ret==0){
                writer.write("<h1>商品上架失败</h1>");
            }else{
                writer.write("<h1>商品上架成功</h1>");
                resp.sendRedirect("mainPage.html");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,null);
        }
    }
}
