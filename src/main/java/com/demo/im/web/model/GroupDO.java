package com.demo.im.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("im_group")
public class GroupDO {

    @TableId(value = "group_id",type = IdType.AUTO)
    private Long groupId;

    @TableField("group_name")
    private String groupName;

    @TableField("group_avatar_url")
    private String groupAvatarUrl;

    @TableField(exist = false)
    private List<UserDO> members;



}
