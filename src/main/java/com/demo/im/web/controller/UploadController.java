package com.demo.im.web.controller;

import com.demo.im.common.Result;
import com.demo.im.util.MinioUtil;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private MinioUtil minioUtil;

    @RequestMapping(value = "file", method = RequestMethod.POST)
    public Result imageFileUpload(@RequestParam("file") MultipartFile file,
                                  @RequestParam("filename") String filename,
                                  @RequestParam("mimeType") String mimeType) {
        boolean exists = false;
        String buketName="";
        if(mimeType.equals("image")){
            buketName = "images";
            exists = minioUtil.bucketExists(buketName);
            if (!exists) {
                minioUtil.createBucket(buketName);
            }
        }else if(mimeType.equals("audio")){
            buketName = "voices";
            exists = minioUtil.bucketExists(buketName);
            if (!exists) {
                minioUtil.createBucket(buketName);
            }
//            ObjectWriteResponse response = minioUtil.uploadFile(file, buketName);
//            InputStream in = minioUtil.downloadFile(buketName,response.object());
//            return Result.ok(in);
        }else if(mimeType.equals("video")){
            buketName = "videos";
            exists = minioUtil.bucketExists(buketName);
            if (!exists) {
                minioUtil.createBucket(buketName);
            }
        }
        ObjectWriteResponse response = minioUtil.uploadFile(file, buketName);
        String fileUrl = minioUtil.getPresignedObjectUrl(buketName, response.object(), null, null);
//        return Result.ok("http://192.168.1.7:9000/"+buketName+"/"+response.object());
        return Result.ok(fileUrl);



    }
}
