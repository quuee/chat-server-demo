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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private MinioUtil minioUtil;

    @RequestMapping(value = "images",method = RequestMethod.POST)
    public Result imageFileUpload(@RequestParam("files") List<MultipartFile> files) {
        boolean exists = minioUtil.bucketExists("images");
        List<String> temp = new ArrayList<>(files.size());
        if(exists){
            for (MultipartFile file : files) {
                ObjectWriteResponse response = minioUtil.uploadFile(file, "images");
                String imageUrl = minioUtil.getPresignedObjectUrl("images", response.object(), null, null);
                temp.add(imageUrl);
            }

            return Result.ok(temp);
        }
        return Result.error("文件存储服务器错误");


    }
}
