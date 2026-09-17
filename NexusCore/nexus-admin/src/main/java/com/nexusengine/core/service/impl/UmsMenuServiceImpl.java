package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.UmsMenuNode;
import com.nexusengine.core.repository.UmsMenuRepository;
import com.nexusengine.core.model.*;
import com.nexusengine.core.service.UmsMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@lombok.RequiredArgsConstructor
public class UmsMenuServiceImpl implements UmsMenuService {
    private final UmsMenuRepository menuRepository;

    @Override
    public int create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        menuRepository.save(umsMenu);
        return 1;
    }

    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            umsMenu.setLevel(0);
        } else {
            UmsMenu parentMenu = menuRepository.findById(umsMenu.getParentId()).orElse(null);
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public int update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        updateLevel(umsMenu);
        menuRepository.save(umsMenu);
        return 1;
    }

    @Override
    public UmsMenu getItem(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public int delete(Long id) {
        menuRepository.deleteById(id);
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        int page = pageNum > 0 ? pageNum - 1 : 0;
        return menuRepository.findByParentId(parentId, PageRequest.of(page > 0 ? page - 1 : 0, pageSize, Sort.by(Sort.Direction.DESC, "sort")));
    }

    @Override
    public List<UmsMenuNode> treeList() {
        List<UmsMenu> menuList = /* findAll() is acceptable for small reference tables */
        menuRepository.findAll();
        List<UmsMenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = menuRepository.findById(id).orElse(new UmsMenu());
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        menuRepository.save(umsMenu);
        return 1;
    }

    private UmsMenuNode covertMenuNode(UmsMenu menu, List<UmsMenu> menuList) {
        UmsMenuNode node = new UmsMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<UmsMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
