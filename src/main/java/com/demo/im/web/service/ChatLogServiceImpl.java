package com.demo.im.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.im.web.dao.ChatLogDOMapper;
import com.demo.im.web.model.ChatLogDO;
import org.springframework.stereotype.Service;

@Service
public class ChatLogServiceImpl extends ServiceImpl<ChatLogDOMapper, ChatLogDO> implements ChatLogService {
}
