package servlet;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//加购物车
@WebServlet("/buyGoodsDirect")
public class buyGoodsDirect extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("Text/html;charset=utf-8");
        //通过商品id得到商品
        String ID = req.getParameter("id");
        int goodID = Integer.valueOf(ID);
        Goods goods = buy.getGoods(goodID);
        goods.setBuyGoodsNum(1);
        //加入account中
        HttpSession session = req.getSession();
        Account account = (Account)session.getAttribute("user");
        account.getMylist().add(goods);
        session.setAttribute("user",account);
        }
}
