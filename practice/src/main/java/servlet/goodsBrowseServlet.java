package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Goods;
import util.DBUtil;

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
import java.util.ArrayList;
import java.util.List;

//商品浏览
@WebServlet("/goods")
public class goodsBrowseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("Text/html;charset=utf-8");
        List<Goods> goodsList = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = "select * from goods";
            con = DBUtil.getConnection(true);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Goods good = new Goods();
                good.setId(rs.getInt("id"));
                good.setName(rs.getString("name"));
                good.setIntroduce(rs.getString("introduce"));
                good.setStock(rs.getInt("stock"));
                good.setUnit(rs.getString("unit"));
                good.setPrice(rs.getInt("price"));
                good.setDiscount(rs.getInt("discount"));

                goodsList.add(good);
            }
            System.out.println(goodsList);

            ObjectMapper objectMapper = new ObjectMapper();
            Writer writer = resp.getWriter();
            objectMapper.writeValue(writer,goodsList);
            writer.write(writer.toString());

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DBUtil.close(con,ps,rs);
        }
    }
}
