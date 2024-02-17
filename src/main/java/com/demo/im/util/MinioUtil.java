package com.demo.im.util;

import com.demo.im.config.minioConfig.BucketPolicyFactory;
import com.demo.im.config.minioConfig.BucketPolicyInterface;
import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;


    /**
     * 检查存储桶是否存在
     * @param bucketName
     * @return
     */
    public boolean bucketExists(String bucketName) {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
            return false;
        }
        return found;
    }

    /**
     * @param bucketName
     * @author
     * @description: 创建桶
     */
    public boolean createBucket(String bucketName) {
        try {
            if (ObjectUtils.isEmpty(bucketName)) {
                return false;
            }
            boolean isExist = bucketExists(bucketName);
            if (isExist) {
                log.info("Bucket {} already exists.", bucketName);
            } else {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * 删除桶
     * @param bucketName
     * @return
     */
    public boolean removeBucket(String bucketName) {

        try {
            boolean isExist = bucketExists(bucketName);
            if(isExist){
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
                return true;
            }
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 设置桶策略
     * @param bucketName
     * @param policy
     * @return
     */
    public boolean setBucketPolicy(String bucketName, String policy) {

        try {
            boolean isExist = bucketExists(bucketName);
            if(isExist){
                BucketPolicyInterface bucketPolicy = BucketPolicyFactory.getBucketPolicyInterface(policy);
                return bucketPolicy.createBucketPolicy(minioClient, bucketName);
            }
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 删除桶下面某个文件
     * @param objectKey
     * @param bucketName
     * @return
     */
    public boolean removeFile(String objectKey, String bucketName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectKey).build());
            return true;
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 上传
     * @param file
     * @param bucketName
     * @return
     */
    public ObjectWriteResponse uploadFile(MultipartFile file,String bucketName){
        ObjectWriteResponse response;
        if(file==null || file.isEmpty()){
            return null;
        }
        String filename = System.currentTimeMillis()+"_"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/")+1).replace(" ","_");
        try {
            InputStream inputStream = file.getInputStream();

            PutObjectArgs images = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();

            response = minioClient.putObject(images);

        }catch (Exception e){
            log.error("error: {}", e.getMessage(), e);
            return null;
        }
        return response;

    }

    public ObjectWriteResponse uploadFile(String bucketName, String objectKey, InputStream inputStream) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
    }

    /**
     *
     * @param objectKey
     * @param bucketName
     * @return
     */
    public InputStream downloadFile(String objectKey, String bucketName) {
        try {
            // 返回文件流
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectKey).build());
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 断点下载
     * @param objectKey
     * @param bucketName
     * @return
     */
    public InputStream downloadFile(String objectKey, String bucketName,long offset, long length) {
        return null;
    }

    /**
     * 获取文件外链
     * @param bucketName
     * @param objectKey
     * @param expires 外链有效时间
     * @param timeUnit
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public String getPresignedObjectUrl(String bucketName, String objectKey, Integer expires, TimeUnit timeUnit) {
        GetPresignedObjectUrlArgs args;
        if(expires == null){
            args = GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectKey)
                    .method(Method.GET)
                    .build();
        }else {
            args = GetPresignedObjectUrlArgs.builder()
                    .expiry(expires,timeUnit)
                    .bucket(bucketName)
                    .object(objectKey)
                    .method(Method.GET)
                    .build();
        }

        return minioClient.getPresignedObjectUrl(args);
    }

    /**
     * 将URLDecoder编码转成UTF8
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getUtf8ByURLDecoder(String str) throws UnsupportedEncodingException {
        String url = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        return URLDecoder.decode(url, "UTF-8");
    }


    //创建目录

}
