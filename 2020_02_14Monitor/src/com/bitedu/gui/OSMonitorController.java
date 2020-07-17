package com.bitedu.gui;

import com.bitedu.osm.FileScanner;
import com.bitedu.osm.FileTreeNode;
import com.bitedu.osm.OSResource;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OSMonitorController {
    //网格中的id
    @FXML private LineChart cpuChart;
    //树表中的id
    @FXML private TreeTableView<FileTreeNode> fileStat;
    @FXML private Text osType;//OS类型
    @FXML private Text cpuArch;
    @FXML private Text cpuCore;//cpu内核
    //定时器任务（每取一次就是一个任务）
    private TimerTask timerTask = null;
    //定时器线程执行定时任务，可以理解成干活的人(一个）
    private Timer timer = new Timer();
    private Stage primaryStage = null;
    //加载图标
    private final Image image = new Image(getClass().getClassLoader().getResourceAsStream("Folder.png"));
    //将主程序的舞台传过来
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    //CPU占用率
    public void handleCPUSelectionChanged(Event event) {

        //拿到tab页
        Tab tab = (Tab)event.getTarget();
        //如果tab页被选中
        if(tab.isSelected()){
            //匿名内部类
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    OSResource.XYPair[] xyPairs = OSResource.getCPUPercentage();
                    XYChart.Series series = new XYChart.Series();
                    //XYChart.Data记录一对坐标，XYChart.series记录多对坐标
                    //将数组中的坐标放到series中
                    for(OSResource.XYPair xyPair : xyPairs){
                        XYChart.Data data = new XYChart.Data(xyPair.getX(),xyPair.getY());
                        series.getData().add(data);
                    }

                    //将逻辑切换到主线程执行
                    Platform.runLater(
                            () ->{
                                //将上一次记录的数据清空，
                                if(cpuChart.getData().size()>0){
                                    cpuChart.getData().remove(0);//从0下标开始
                                }
                                //再将数据放到网格中，即cpuChart
                                cpuChart.getData().add(series);

                                osType.setText(OSResource.getOSName());
                                cpuArch.setText(OSResource.getCpuArch());
                                cpuCore.setText(OSResource.getCpuCore());
                            }
                    );
                }
            };
            //第二个参数 0 表示任务安排好以后，立即执行一次
            //第三个参数 表示执行周期时间，单位为毫秒
            timer.schedule(timerTask,0,1000);
        }else{
            //关闭时间任务
            if(timerTask != null){
                timerTask.cancel();
                timerTask = null;
            }
        }
    }
    //程序关闭时执行
    public void shutDown(){
        if(timer != null){
            timer.cancel();
        }
    }


    //磁盘目录
    public void handleSelectFile(ActionEvent actionEvent) {
        //将上次查看的文件清掉
        fileStat.setRoot(null);
        //目录选择器
        DirectoryChooser directoryChooser = new DirectoryChooser();
        //弹出目录框,并拿到选择的目录或文件
        File file = directoryChooser.showDialog(primaryStage);

        //内部类，放到线程里做，加快速度
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //将拿到的目录作为根目录，并设置名字
                FileTreeNode rootNode = new FileTreeNode();
                rootNode.setFile(file);
                rootNode.setFileName(file.getName());
                //将文件扫描
                FileScanner.scannerDirectory(rootNode);

                //渲染树
                //将目录加到树表中（将FileTreeNode转成TreeItem)
                TreeItem rootItem = new TreeItem(rootNode,new ImageView(image));
                //容许展开（目录）
                rootItem.setExpanded(true);
                fileTreeItem(rootNode,rootItem);
                //转到主线程执行
                Platform.runLater(
                        () ->{
                            fileStat.setRoot(rootItem);
                        }
                );
            }
        });
        //释放主线程
        thread.setDaemon(true);
        thread.start();
    }
    //进行转换（FileTreeNode-->FileTreeItem)
    private  void fileTreeItem(FileTreeNode rootNode, TreeItem rootItem){
        List<FileTreeNode> childs = rootNode.getChildrens();
        for(FileTreeNode node: childs){
            TreeItem item = new TreeItem(node);

            //给子目录设置图标
            if(node.getChildrens().size()>0){
                item.setGraphic(new ImageView(image));
            }
            rootItem.getChildren().add(item);
            //通过递归 将子目录的子目录转换
            fileTreeItem(node,item);
        }
    }
}
