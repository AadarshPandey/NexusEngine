package com.nexusengine.core.portal.customer.service;

import com.nexusengine.core.domain.customer.entity.MemberAddress;


import java.util.List;


/**
 * Represents the MemberAddressService component.
 * Provides core functionality and operations for MemberAddressService.
 */
public interface MemberAddressService {
        /**
     * Executes the operation.
     * @param address the address
     * @return the result of the operation
     */
    int add(MemberAddress address);

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
    int update(Long id, MemberAddress address);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<MemberAddress> list();

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    MemberAddress getItem(Long id);
}
