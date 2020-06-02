package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Account;
import entity.Goods;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@WebServlet("/shop")
public class myShopServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("Text/html;charset=utf-8");
        HttpSession session = req.getSession();
        Account account = (Account)session.getAttribute("user");
        List<Goods> goodsList = account.getMylist();

        //推到前端
        ObjectMapper objectMapper = new ObjectMapper();
        Writer writer = resp.getWriter();
        objectMapper.writeValue(writer,goodsList);
        writer.write(writer.toString());
    }
}
