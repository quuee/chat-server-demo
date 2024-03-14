package cn.quuee.chatServer.web.service;

import cn.quuee.chatServer.web.dao.ChatLogDOMapper;
import cn.quuee.chatServer.web.model.ChatLogDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ChatLogServiceImpl extends ServiceImpl<ChatLogDOMapper, ChatLogDO> implements ChatLogService {
}
