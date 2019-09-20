package com.chc.peaceworld.security.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chc.peaceworld.security.entity.Resource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author lite
 * @since 2019-06-18
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    @Select("select r.* from sys_resource r inner join sys_authority_resource ar on r.id=ar.resource_id where ar.authority_id=#{authorityId}  and r.type='menu'")
    List<Resource> selectByAuthorityIdAndType(Long authorityId);

    @Select("select r.* from sys_resource r inner join sys_authority_resource ar on r.id=ar.resource_id " +
            "where ar.authority_id=#{authorityId} and parent_id=#{resourceId} and r.type='button'")
    List<Resource> selectByAuthorityIdAndTypeAndMenuId(@Param("authorityId") Long authorityId, @Param("resourceId") Long resourceId);
}
