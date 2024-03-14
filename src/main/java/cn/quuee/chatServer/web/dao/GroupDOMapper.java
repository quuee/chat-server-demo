package cn.quuee.chatServer.web.dao;

import cn.quuee.chatServer.web.model.GroupDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupDOMapper extends BaseMapper<GroupDO> {

    GroupDO selectGroupAndMembers(@Param("groupId") Long groupId);

    List<GroupDO> selectGroupsByUser(@Param("userId")Long userId);
}
