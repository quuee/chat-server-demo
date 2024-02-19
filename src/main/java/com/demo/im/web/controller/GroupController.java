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

    public Result create(){

        return null;
    }

    @RequestMapping(value = "list",method = RequestMethod.GET)
    public Result list(Long userId){
        List<GroupDO> groupsByUser = groupService.findGroupsByUser(userId);
        return Result.ok(groupsByUser);
    }

    @RequestMapping(value = "members",method = RequestMethod.GET)
    public Result groupAndMembers(Long groupId){
        GroupDO groupAndMembers = groupService.findGroupAndMembers(groupId);
        return Result.ok(groupAndMembers);
    }

    public Result delete(){
        return null;
    }


}
