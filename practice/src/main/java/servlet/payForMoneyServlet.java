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
import java.util.List;

@WebServlet("/payfor")
public class payForMoneyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("Text/html;charset=utf-8");
        HttpSession session = req.getSession();
        Account account = (Account)session.getAttribute("user");
        List<Goods> goodsList = account.getMylist();
            StringBuilder sb = new StringBuilder();
            for(Goods goods:goodsList){
                sb.append(goods.getId()+"-"+goods.getBuyGoodsNum()+",");
            }
            buy.buyGoods(sb.toString(),req,resp);
    }
}
