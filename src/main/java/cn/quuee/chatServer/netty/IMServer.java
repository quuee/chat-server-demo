package cn.quuee.chatServer.netty;

public interface IMServer {

    /**
     * 用于判断服务是否启动完毕
     * @return
     */
    boolean isReady();

    void start();

    void stop();
}
