package com.demo.im.web.controller;


import com.demo.im.common.Result;
import com.demo.im.web.model.GroupDO;
import com.demo.im.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 发起群聊
     * @return
     */
    public Result create(){

        return null;
    }

    /**
     * 添加某人到群聊
     * @return
     */
    public Result addSomeone(){
        return null;
    }


    /**
     * 群聊列表
     * @param userId
     * @return
     */
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public Result list(Long userId){
        // 其实登录后有token ，这里简单实现
        List<GroupDO> groupsByUser = groupService.findGroupsByUser(userId);
        return Result.ok(groupsByUser);
    }

    /**
     * 群聊成员
     * @param groupId
     * @return
     */
    @RequestMapping(value = "members",method = RequestMethod.GET)
    public Result groupAndMembers(Long groupId){
        GroupDO groupAndMembers = groupService.findGroupAndMembers(groupId);
        return Result.ok(groupAndMembers);
    }

    /**
     * 删除某人
     */
    public Result deleteSomeone(){
        return null;
    }

    /**
     * 退出群聊
     * 群主无法退出，只有解散群聊
     * @return
     */
    public Result quit(){
        return null;
    }


    /**
     * 删除群聊 及 群成员
     * @return
     */
    public Result delete(){
        return null;
    }


}
