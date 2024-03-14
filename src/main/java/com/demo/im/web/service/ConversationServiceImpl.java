package com.demo.im.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.im.web.dao.ConversationDOMapper;
import com.demo.im.web.model.ConversationDO;
import org.springframework.stereotype.Service;

@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationDOMapper, ConversationDO> implements ConversationService {
}
