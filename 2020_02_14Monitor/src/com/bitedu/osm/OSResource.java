package com.bitedu.osm;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
//资源获取
public class OSResource {
    //获取系统资源  OperatingSystemMXBean为接口
    private static OperatingSystemMXBean mxBean = ManagementFactory.
            getPlatformMXBean(OperatingSystemMXBean.class);

    //记录60个坐标点，也就是统计周期
    private static final int DATA_LENGTH = 60;
    //存放坐标的数组
    private static XYPair[] xyPairs = new XYPair[DATA_LENGTH];
    private static int firstIndex = DATA_LENGTH;

    static{
        for(int i = 0; i < xyPairs.length; i++){
            xyPairs[i] = new XYPair(0,0);
        }
    }
    public static class XYPair{
        private  double x;//x轴
        private  double y;

        public XYPair(double x, double y){
            this.x = x;
            this.y = y;
        }
        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

    public static XYPair[] getCPUPercentage(){
        //获取CPU占用率
        double cpu = mxBean.getSystemCpuLoad();
        moveCPUData(cpu);
        return xyPairs;
    }
    //获取操作系统版本
    public static String getOSName(){
        return mxBean.getName();
    }
    //获取系统版本
    public static String getCpuArch(){
        return mxBean.getArch();
    }
    //获取cpu内核
    public static String getCpuCore(){
        return ""+Runtime.getRuntime().availableProcessors();
    }
    private static void moveCPUData(double cpuPercentage){
        int movIdx = -1;
        if (firstIndex == 0){
            movIdx = firstIndex + 1;
        }else {
            movIdx = firstIndex;
            firstIndex--;
        }
        for (; movIdx < xyPairs.length; ++movIdx){
            xyPairs[movIdx-1].setX(xyPairs[movIdx].getX()-1);
            xyPairs[movIdx-1].setY(xyPairs[movIdx].getY());
        }
        movIdx--;
        xyPairs[movIdx] = new XYPair(movIdx, cpuPercentage); }
}
