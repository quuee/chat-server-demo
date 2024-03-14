package cn.quuee.chatServer.netty;


import cn.quuee.chatServer.common.IMRedisKey;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IMServersLaunch implements CommandLineRunner {

    public static volatile long serverId = 0; // 服务器id

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private List<IMServer> imServers;

    public IMServersLaunch(List<IMServer> imServers) {
        this.imServers = imServers;
    }

    @Override
    public void run(String... args) throws Exception {
        // 初始化服务器id ，每部署一台服务，serverId自增
        String key = IMRedisKey.IM_MAX_SERVER_ID;
        serverId = redisTemplate.opsForValue().increment(key, 1);// 递增因子
        // 启动服务
        for (IMServer imServer : imServers) {
            imServer.start();
        }
    }

    /***
     * 判断服务器是否就绪
     *
     * @return
     **/
    public boolean isReady() {
        for (IMServer imServer : imServers) {
            if (!imServer.isReady()) {
                return false;
            }
        }
        return true;
    }

    @PreDestroy
    public void destroy() {
        // 停止服务
        for (IMServer imServer : imServers) {
            imServer.stop();
        }
    }

}
