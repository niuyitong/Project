package servlet;

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

//下架
@WebServlet("/delGoods")
public class goodsSoldOutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("Text/html;charset=utf-8");
        String ID = req.getParameter("id");
        int id = Integer.valueOf(ID);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Writer writer = resp.getWriter();

        try{
            String sql = "delete from goods where id=?";
            con = DBUtil.getConnection(true);
            ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            int ret = ps.executeUpdate();
            if(ret==1){
                System.out.println("下架成功");
            }else{
                System.out.println("下架失败");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,null);
        }
    }
}
