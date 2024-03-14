package cn.quuee.chatServer.config.minioConfig;

import java.util.HashMap;
import java.util.Map;

public class BucketPolicyFactory {

    static Map<String, BucketPolicyInterface> operationMap = new HashMap<>();
    static {
        operationMap.put("read", new BucketReadPolicy());// 只读
        operationMap.put("write", new BucketWritePolicy());// 只写
        operationMap.put("read-write", new BucketReadWriterPolicy());// 读写
    }

    public static BucketPolicyInterface getBucketPolicyInterface(String policy) {
        BucketPolicyInterface object = operationMap.get(policy);
        if (object == null) {
            object = new BucketReadPolicy();
        }
        return object;
    }

}
