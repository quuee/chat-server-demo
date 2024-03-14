package cn.quuee.chatServer.web.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * user_id contact_id 唯一索引
 */
@Data
@TableName("im_contact")
public class ContactDO {

    @TableField("user_id")
    private Long userId;

    //(userId)
    @TableField("contact_id")
    private Long contactId;
}
