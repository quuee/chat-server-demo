package cn.quuee.chatServer.web.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("im_group_user")
public class GroupUserDO {

    @TableField("group_id")
    private Long groupId;

    @TableField("user_id")
    private Long userId;
}
