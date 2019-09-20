package com.chc.peaceworld.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chc.peaceworld.security.entity.Resource;
import com.chc.peaceworld.security.repository.ResourceMapper;
import com.chc.peaceworld.security.service.IResourceService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author lite
 * @since 2019-06-18
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Override
    public List<Resource> selectByAuthorityIdAndType(Long authorityId) {
        return baseMapper.selectByAuthorityIdAndType(authorityId);
    }

    @Override
    public List<Resource> selectByAuthorityIdAndTypeAndMenuId(Long authorityId, Long resourceId) {
        return baseMapper.selectByAuthorityIdAndTypeAndMenuId(authorityId, resourceId);
    }

    @Override
    public Set<Resource> changeStyle(Set<Resource> menuSet) {
        Set<Resource> firstLevel = new LinkedHashSet<>();
        // 提取一级菜单
        for (Resource resource : menuSet) {
            if (resource.getParentId() == 0) {
                firstLevel.add(resource);
            }
        }
        menuSet.removeAll(firstLevel);

        // 配置下属
        for (Resource resource : firstLevel) {
            setChildren(resource, menuSet);
        }
        return firstLevel;
    }

    private void setChildren(Resource resource, Set<Resource> menuSet) {
        if (menuSet.isEmpty()) {
            return;
        }

        Set<Resource> childSet = new LinkedHashSet<>();
        for (Resource menu : menuSet) {
            if (menu.getParentId().equals(resource.getId())) {
                childSet.add(menu);
            }
        }
        resource.setChildren(childSet);
        menuSet.removeAll(childSet);

        // 递归
        for (Resource childResource : childSet) {
            setChildren(childResource, menuSet);
        }
    }
}
