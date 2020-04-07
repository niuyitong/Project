package com.bitedu.osm;

import java.io.File;

public class FileScanner {
    //统计拿到的目录的子目录
    public static void scannerDirectory(FileTreeNode node){
        //拿到当前目录的子目录数组
        File[] files = node.getFile().listFiles();
        if (files == null) {
            return;
        }
        //遍历子目录
        for (File file : files) {
            FileTreeNode child = new FileTreeNode();
            child.setFile(file);
            child.setFileName(file.getName());
            //如果当前子目录还是个目录，继续递归
            if (file.isDirectory()) {
                scannerDirectory(child);
            } else { //当前子目录是个文件，则记录文件大小
                child.setTotalLength(file.length());
            }
            //将当前目录的大小与子目录的大小加起来
            node.setTotalLength(node.getTotalLength() + child.getTotalLength());
            //将子目录保存在父节点中
            node.addChildNode(child);
        }
    }
}
