package dao;

import entiy.User;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UserDaoTest {
    @Test
    public void findAllRecord(){
        UserDao userDao = new UserDao();
        Map<String,String[]> map = new HashMap<>();
        //String[] strs = {"林"};
        //map.put("address",strs);
        String[] strs = {""};
        map.put("address",strs);
        int ret = userDao.findAllRecord(map);
        System.out.println(ret);
    }
    @Test
    public void findByPage(){
        UserDao userDao = new UserDao();
        Map<String,String[]> map = new HashMap<>();
        String[] strs = {"林"};
        map.put("address",strs);
        List<User> userList =userDao.findByPage(0,10,map);
        System.out.println(userList);
    }
    @Test
    public void updateUser(){
        UserDao userDao = new UserDao();
        User user = new User();
        user.setId(8);
        user.setName("小明");
        user.setUsername("xiaoming");
        user.setGender("男");
        user.setAge(20);
        user.setQq("913451387");
        user.setEmail("daria87@qq.com");
        user.setAddress("黑桦林");
        user.setPassword("123456");
        int ret = userDao.updateUser(user);
        if(ret==0){
            System.out.println("更新失败");
        }else{
            System.out.println("更新成功");
        }
    }
    @Test
    public void find(){
        UserDao userDao = new UserDao();
        User ret = userDao.find(4);
        System.out.println(ret);
    }
    @Test
    public void delete(){
        UserDao userDao = new UserDao();
        int ret = userDao.delete(2);
        if(ret==0){
            System.out.println("删除失败");
        }else{
            System.out.println("删除成功");
        }
    }
    @Test
    public void login() {

        UserDao userDao = new UserDao();
        User user = new User();
        user.setUsername("zhangfei");
        user.setPassword("123");
        User ret = userDao.login(user);
        System.out.println(ret);
        /*
        user.setId(5);
        user.setName("小明");
        user.setUsername("xiaoming");
        user.setGender("男");
        user.setAge(20);
        user.setQq("913451387");
        user.setEmail("daria87@qq.com");
        user.setAddress("白桦林");
        user.setPassword("123456");
        */
    }
    @Test
    public void add() {
        UserDao userDao = new UserDao();
        User user = new User();
        user.setName("小华");
        user.setUsername("xiaoming");
        user.setGender("男");
        user.setAge(20);
        user.setEmail("daria87@qq.com");
        user.setAddress("白桦林");
        user.setPassword("123456");
        int ret = userDao.add(user);
        if(ret==0){
            System.out.println("添加失败");
        }else{
            System.out.println("添加成功");
        }
    }
}