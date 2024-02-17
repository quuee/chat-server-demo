package com.demo.im.config.minioConfig;

import io.minio.MinioClient;

public interface BucketPolicyInterface {

    /**
     *
     * @Title: createBucketPolicy
     * @Description: 设置桶策略
     * @param: @param client
     * @param: @param bucket
     * @param: @return
     * @return: boolean
     * @throws
     */
    boolean createBucketPolicy(MinioClient client, String bucket);

}
