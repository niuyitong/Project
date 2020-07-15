package dao;

import entiy.User;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public int updateUser(User upUser){
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "update usermessage set gender=?,age=?,address=?,qq=?,email=? where id=?";
        int ret = 0;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,upUser.getGender());
            ps.setInt(2,upUser.getAge());
            ps.setString(3,upUser.getAddress());
            ps.setString(4,upUser.getQq());
            ps.setString(5,upUser.getEmail());
            ps.setInt(6,upUser.getId());
            ret = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,null);
        }
        return ret;
    }
    public  User find(int id){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from usermessage where id=?";
        User user = null;
        try{
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAge(rs.getInt("age"));
                user.setAddress(rs.getString("address"));
                user.setQq(rs.getString("qq"));
                user.setEmail(rs.getString("email"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,rs);
        }
        return user;
    }
    public int delete(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "delete from usermessage where id = ?";
        int ret = 0;
        try{
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ret = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,null);
        }
        return ret;
    }
    public int add(User user){
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "insert into usermessage(name,username,password,gender,age,address,qq,email)values(?,?,?,?,?,?,?,?)";
        int ret = 0;
        try{
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,user.getName());
            ps.setString(2,user.getUsername());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getGender());
            ps.setInt(5,user.getAge());
            ps.setString(6,user.getAddress());
            ps.setString(7,user.getQq());
            ps.setString(8,user.getEmail());
            ret = ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,null);
        }
        return ret;
    }
    public User login(User loginUser){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from usermessage where username=? and password=?;";
        User user = null;
        try{
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,loginUser.getUsername());
            ps.setString(2,loginUser.getPassword());
            rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("登录成功");
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                user.setAge(rs.getInt("age"));
                user.setQq(rs.getString("qq"));
                user.setEmail(rs.getString("email"));
            }else{
                System.out.println("登录失败");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DBUtil.close(con,ps,rs);
        }
        return user;
    }

}
