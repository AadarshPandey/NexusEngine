package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;

import com.nexusengine.core.admin.iam.dto.MenuNode;

import java.util.List;


/**
 * Represents the ConsoleMenuService component.
 * Provides core functionality and operations for ConsoleMenuService.
 */
public interface ConsoleMenuService {
        /**
     * Executes the operation.
     * @param consoleMenu the consoleMenu
     * @return the result of the operation
     */
    int create(ConsoleMenu consoleMenu);

        /**
     * Executes the operation.
     * @param id the id
     * @param consoleMenu the consoleMenu
     * @return the result of the operation
     */
    int update(Long id, ConsoleMenu consoleMenu);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    ConsoleMenu getItem(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);

        /**
     * Executes the operation.
     * @param parentId the parentId
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<ConsoleMenu> list(Long parentId, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<MenuNode> treeList();

        /**
     * Executes the operation.
     * @param id the id
     * @param hidden the hidden
     * @return the result of the operation
     */
    int updateHidden(Long id, Integer hidden);
}
