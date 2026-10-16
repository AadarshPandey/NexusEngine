package com.nexusengine.core.admin.iam.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;

import com.nexusengine.core.domain.iam.repository.ConsoleMenuRepository;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.nexusengine.core.admin.iam.dto.MenuNode;


@Service
@RequiredArgsConstructor
public class ConsoleMenuServiceImpl implements ConsoleMenuService {
    private final ConsoleMenuRepository menuRepository;

    @Override
    public int create(ConsoleMenu consoleMenu) {
        consoleMenu.setCreateTime(java.time.OffsetDateTime.now());
        updateLevel(consoleMenu);
        menuRepository.save(consoleMenu);
        return 1;
    }

    private void updateLevel(ConsoleMenu consoleMenu) {
        if (consoleMenu.getParentId() == 0) {
            consoleMenu.setLevel(0);
        } else {
            ConsoleMenu parentMenu = menuRepository.findById(consoleMenu.getParentId()).orElse(null);
            if (parentMenu != null) {
                consoleMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                consoleMenu.setLevel(0);
            }
        }
    }

    @Override
    public int update(Long id, ConsoleMenu consoleMenu) {
        consoleMenu.setId(id);
        updateLevel(consoleMenu);
        menuRepository.save(consoleMenu);
        return 1;
    }

    @Override
    public ConsoleMenu getItem(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public int delete(Long id) {
        menuRepository.deleteById(id);
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<ConsoleMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        int page = pageNum > 0 ? pageNum - 1 : 0;
        return menuRepository.findByParentId(parentId, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "sort")));
    }

    @Override
    public List<MenuNode> treeList() {
        List<ConsoleMenu> menuList = /* findAll() is acceptable for small reference tables */
        menuRepository.findAll();
        List<MenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        ConsoleMenu consoleMenu = menuRepository.findById(id).orElse(new ConsoleMenu());
        consoleMenu.setId(id);
        consoleMenu.setHidden(hidden != null && hidden == 1);
        menuRepository.save(consoleMenu);
        return 1;
    }

    private MenuNode covertMenuNode(ConsoleMenu menu, List<ConsoleMenu> menuList) {
        List<MenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        return new MenuNode(menu, children);
    }
}
