package com.demo.im.web.controller;

import com.demo.im.common.Result;
import com.demo.im.web.model.UserDO;
import com.demo.im.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("contact")
public class ContactController {

    @Autowired
    private UserService userService;

    // 添加联系人

    // 删除联系人

    // 联系人列表
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public Result<List<UserDO>> list(@RequestParam Long userId){

        List<UserDO> userDOS = userService.contactList(userId);
        return Result.ok(userDOS);

    }

    // 编辑联系人信息

}
