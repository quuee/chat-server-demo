
## chat-server-demo
flutter_chat_demo的服务端  
springboot + netty

### channelHandler生命周期
```
handlerAdded() –> channelRegistered() –> channelActive() –> channelRead() –> channelReadComplete()
```

