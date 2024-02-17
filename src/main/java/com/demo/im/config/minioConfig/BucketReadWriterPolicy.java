package com.demo.im.config.minioConfig;

import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class BucketReadWriterPolicy implements BucketPolicyInterface{

    /**
     * 桶占位符
     */
    private static final String BUCKET_PARAM = "${bucket}";

    /**
     * bucket权限-只读
     */
    private static final String READ_WRITE = "";

    @Override
    public boolean createBucketPolicy(MinioClient client, String bucket) {
        try {
            client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(READ_WRITE.replace(BUCKET_PARAM, bucket)).build());
            return true;
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                 | IllegalArgumentException | IOException e) {

            e.printStackTrace();
            log.error("error: {}", e.getMessage(), e);
        }
        return false;

    }
}
