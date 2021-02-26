package com.changgou;

import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author pengmin
 * @date 2020/11/10 16:43
 */

public class FileTest {

    //@Test
    public void upload() throws Exception{
        ClientGlobal.init("F:\\2.Java Project\\changgou\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fastDFS_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] file = storageClient.upload_file("E:\\3.重要文件\\图片\\1.jpg", "jpg", null);
        for (String s : file) {
            System.out.println(s);
        }
    }

    //@Test
    public void download()throws Exception{
        ClientGlobal.init("F:\\2.Java Project\\changgou\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fastDFS_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        byte[] bytes = storageClient.download_file("group1", "M00/00/00/rBEKYl-qXF6AVASrABuvlYFiMq4198.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\pengmin\\Desktop\\123.jpg"));
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

   // @Test
    public void getFileInfo()throws Exception{
        ClientGlobal.init("F:\\2.Java Project\\changgou\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fastDFS_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        FileInfo fileInfo = storageClient.get_file_info("group1", "M00/00/00/rBEKYl-qXF6AVASrABuvlYFiMq4198.jpg");
        System.out.println(fileInfo);
    }

    //@Test
    public void delete()throws Exception{
        ClientGlobal.init("F:\\2.Java Project\\changgou\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fastDFS_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        storageClient.delete_file("group1", "M00/00/00/rBEKYl-qXF6AVASrABuvlYFiMq4198.jpg");
    }
}
