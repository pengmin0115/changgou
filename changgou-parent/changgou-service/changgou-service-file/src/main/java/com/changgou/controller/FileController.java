package com.changgou.controller;

import com.changgou.file.FastDfsFile;
import com.changgou.utils.FastDfsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author pengmin
 * @date 2020/11/10 20:08
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${pic.url}")
    private String url;

    @RequestMapping("/upload")
    public String upload(@RequestParam(name = "file") MultipartFile multipartFile) throws IOException {
        try {
            if (!multipartFile.isEmpty()) {
                FastDfsFile dfsFile = new FastDfsFile(
                        multipartFile.getOriginalFilename(),
                        multipartFile.getBytes(),
                        StringUtils.getFilenameExtension(multipartFile.getOriginalFilename())
                );
                String[] upload = FastDfsClient.upload(dfsFile);
                // 返回文件路径;
                return url+upload[0]+"/"+upload[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
