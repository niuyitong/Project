package servlet;

import util.DBUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/register")
public class registerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("text/html;charset=utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Writer writer = resp.getWriter();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{
            String sql = "INSERT INTO account(username,password) values(?,?)";
            con = DBUtil.getConnection(true);
            ps = con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            int ret = ps.executeUpdate();
            if(ret==0){
                writer.write("<h1>注册失败</h1>");
            }else{
                writer.write("<h1>注册成功</h1>");
                resp.sendRedirect("login.html");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,null);
        }
    }
}
