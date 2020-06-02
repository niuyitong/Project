package servlet;

import com.sun.net.httpserver.HttpServer;
import entity.Account;
import util.DBUtil;

import javax.servlet.ServletException;
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

@WebServlet("/login")
public class loginServlet extends HttpServlet {
    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("text/html; charset=utf-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Writer writer = resp.getWriter();

        try{
            String sql = "select id,username,password from account " +
                    "where username=?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();
            Account user = new Account();
            if(rs.next()){
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            if(user.getId()==null){
                writer.write("<h1>没有该用户:"+username+"</h1>");
            }else if(!(password.equals(user.getPassword()))){
                writer.write("<h1>用户名或密码错误</h1>");
            }else{
                HttpSession session = req.getSession();
                session.setAttribute("user",user);
                writer.write("<h1>登录成功</h1>");
                resp.sendRedirect("index.html");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }
}
