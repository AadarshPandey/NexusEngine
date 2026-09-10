package com.nexusengine.core.service;

import com.nexusengine.core.dto.UmsMenuNode;
import com.nexusengine.core.model.UmsMenu;

import java.util.List;

/**
 * Represents the UmsMenuService component.
 * Provides core functionality and operations for UmsMenuService.
 */
public interface UmsMenuService {
        /**
     * Executes the operation.
     * @param umsMenu the umsMenu
     * @return the result of the operation
     */
    int create(UmsMenu umsMenu);

        /**
     * Executes the operation.
     * @param id the id
     * @param umsMenu the umsMenu
     * @return the result of the operation
     */
    int update(Long id, UmsMenu umsMenu);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    UmsMenu getItem(Long id);

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
    org.springframework.data.domain.Page<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<UmsMenuNode> treeList();

        /**
     * Executes the operation.
     * @param id the id
     * @param hidden the hidden
     * @return the result of the operation
     */
    int updateHidden(Long id, Integer hidden);
}
