package com.chc.peaceworld.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chc.peaceworld.security.entity.Resource;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author lite
 * @since 2019-06-18
 */
public interface IResourceService extends IService<Resource> {

    List<Resource> selectByAuthorityIdAndType(Long userId);

    List<Resource> selectByAuthorityIdAndTypeAndMenuId(Long id, Long resourceId);

    Set<Resource> changeStyle(Set<Resource> menuSet);
}
