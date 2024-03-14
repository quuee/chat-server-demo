package com.demo.im.web.controller;

import com.demo.im.common.Result;
import com.demo.im.web.model.ConversationDO;
import com.demo.im.web.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @RequestMapping(value = "loadConversations",method = RequestMethod.GET)
    public Result loadConversations(Long userId){

        return Result.ok();
    }

    public Result loadConversation(Long userId,Long contactId){
        return Result.ok();
    }


    public Result saveConversations(List<ConversationDO> conversationDOList){
        return Result.ok();
    }
}
