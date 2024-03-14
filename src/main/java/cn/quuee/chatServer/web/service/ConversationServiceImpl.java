package cn.quuee.chatServer.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.quuee.chatServer.web.dao.ConversationDOMapper;
import cn.quuee.chatServer.web.model.ConversationDO;
import org.springframework.stereotype.Service;

@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationDOMapper, ConversationDO> implements ConversationService {
}
