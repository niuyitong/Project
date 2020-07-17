package dao;

import entiy.User;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class UserDao {
    /**
     * 查询共有多少条记录
     * @param map
     * @return
     */
    public int findAllRecord(Map<String, String[]> map) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Object> list = new ArrayList<>();
        String sql = "select count(*) from usermessage where 1=1 ";
        //select * from usermessage where 1=1 and name like ? and adress like ?;
        StringBuilder sb = new StringBuilder(sql);
        Set<String> set = map.keySet();
        for(String key : set){
            if(key!=null && !"".equals(key)){
                sb.append(" and ").append(key).append(" like ? ");
                list.add("%"+map.get(key)[0]+"%");//value模糊查询
            }
        }
        int ret = 0;
        try{
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sb.toString());
            setValues(ps,list.toArray());
            rs = ps.executeQuery();
            if(rs.next()){
                ret = rs.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,rs);
        }
        return ret;
    }
    /**
     * 查询 （模糊查询或者全查询）
     * @param start
     * @param rows
     * @param map  map{name:"小明"，address:"上海"}
     * @return
     */
    public List<User> findByPage(int start, int rows, Map<String, String[]> map) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Object> list = new ArrayList<>();
        List<User> userList = new ArrayList<>();

        String sql = "select * from usermessage where 1=1 ";
        //select * from usermessage where 1=1 and name like ? and adress like ?;
        StringBuilder sb = new StringBuilder(sql);
        Set<String> set = map.keySet();
        for(String key : set){
            if(key!=null && !"".equals(key)){
                sb.append(" and ").append(key).append(" like ? ");
                list.add("%"+map.get(key)[0]+"%");//value模糊查询
            }
        }
        sb.append(" limit ?,?");
        list.add(start);
        list.add(rows);

        try{
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sb.toString());
            setValues(ps,list.toArray());
            rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAge(rs.getInt("age"));
                user.setAddress(rs.getString("address"));
                user.setQq(rs.getString("qq"));
                user.setEmail(rs.getString("email"));
                userList.add(user);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(con,ps,rs);
        }
        return userList;
    }
    public void setValues(PreparedStatement ps,Object[] list) throws SQLException {
        for(int i=0; i<list.length; i++){
            ps.setObject(i+1,list[i]);
        }
    }

    /**
     * 更新用户
     * @param upUser
     * @return
     */
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

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
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

    /**
     * 删除用户
     * @param id
     * @return
     */
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

    /**
     * 添加用户
     * @param user
     * @return
     */
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

    /**
     * 登录
     * @param loginUser
     * @return
     */
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
