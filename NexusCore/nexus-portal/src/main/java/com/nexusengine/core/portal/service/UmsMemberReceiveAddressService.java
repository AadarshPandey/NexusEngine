package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * Represents the UmsMemberReceiveAddressService component.
 * Provides core functionality and operations for UmsMemberReceiveAddressService.
 */
public interface UmsMemberReceiveAddressService {
        /**
     * Executes the operation.
     * @param address the address
     * @return the result of the operation
     */
    int add(UmsMemberReceiveAddress address);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @param address the address
     * @return the result of the operation
     */
    int update(Long id, UmsMemberReceiveAddress address);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<UmsMemberReceiveAddress> list();

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    UmsMemberReceiveAddress getItem(Long id);
}
