package com.bitedu.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//javafx 主程序的要求：
//1. 必须继承javafx的Application 类
//2. 必须覆盖（Override)Application 类的start方法
public class OSMonitorApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // 1. 加载 .fxml 文件
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().//获取类加载器
                getResource("os_monitor_tab.fxml"));//加载出文件
        //2. 真正的加载
        Parent root = loader.load();
        //拿到controller
        OSMonitorController controller = loader.getController();
        //将舞台传到controller里去
        controller.setPrimaryStage(primaryStage);
        // 3. 创建一个场景对象
        Scene scene = new Scene(root, 800, 600);
        // 4. 给舞台对象设置标题
        primaryStage.setTitle("OS Monitor");
        // 5. 给舞台对象 stage 设置场景对象 scene
        primaryStage.setScene(scene);
        //用来关闭程序
        primaryStage.setOnCloseRequest((e) -> controller.shutDown());
        // 6. 展示舞台
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
