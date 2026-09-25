package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsBrandParam;
import com.nexusengine.core.model.PmsBrand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface PmsBrandService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<PmsBrand> listAllBrand();

        /**
     * Executes the operation.
     * @param pmsBrandParam the pmsBrandParam
     * @return the result of the operation
     */
    int createBrand(PmsBrandParam pmsBrandParam);

        /**
     * Executes the operation.
     * @param id the id
     * @param pmsBrandParam the pmsBrandParam
     * @return the result of the operation
     */
    @Transactional
    int updateBrand(Long id, PmsBrandParam pmsBrandParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int deleteBrand(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int deleteBrand(List<Long> ids);

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param showStatus the showStatus
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<PmsBrand> listBrand(String keyword, Integer showStatus, int pageNum, int pageSize);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    PmsBrand getBrand(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param showStatus the showStatus
     * @return the result of the operation
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);
    int updateSort(Long id, Integer sort);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param factoryStatus the factoryStatus
     * @return the result of the operation
     */
    int updateFactoryStatus(List<Long> ids, Integer factoryStatus);
}
