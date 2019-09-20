package com.chc.peaceworld.security.repository;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chc.peaceworld.security.entity.Authority;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by stephan on 20.03.16.
 */
@Repository
public interface AuthorityMapper extends BaseMapper<Authority> {

    @Select("select a.* FROM sys_authority a INNER JOIN sys_user_authority u ON a.id=u.authority_id WHERE u.user_id=#{userId} ")
    List<Authority> selectAuthorityList(Long userId);

    @Insert("insert into sys_user_authority (user_id,authority_id) values (#{userId}, #{authorityId})")
    void save(@Param("userId") Long userId, @Param(("authorityId")) Long authorityId);
}
