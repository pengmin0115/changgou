package com.changgou.utils;
import com.changgou.file.FastDfsFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author pengmin
 * @date 2020/11/10 18:14
 */

public class FastDfsClient {
    static {
        try {
            ClassPathResource pathResource = new ClassPathResource("fastDFS_client.conf");
            ClientGlobal.init(pathResource.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] upload(FastDfsFile fastDfsFile){
        String[] png = new String[0];
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            NameValuePair[] metaList = new NameValuePair[]{
                new NameValuePair(fastDfsFile.getName())
            };
            png = storageClient.upload_file(fastDfsFile.getContent(),fastDfsFile.getExt(), metaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return png;
    }

    public static byte[] download(String groupName, String remoteFileName){
        byte[] bytes = new byte[0];
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            bytes = storageClient.download_file(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static Boolean delete(String groupName, String remoteFileName){
        int i = 0;
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            i = storageClient.delete_file(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i==0;
    }

    public static FileInfo getFileInfo(String groupName, String remoteFileName){
        FileInfo file_info = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            file_info = storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_info;
    }
}
