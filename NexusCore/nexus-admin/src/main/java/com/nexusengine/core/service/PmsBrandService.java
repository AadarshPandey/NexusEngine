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
     * Auto-generated documentation
     */
    List<PmsBrand> listAllBrand();

    /**
     * Auto-generated documentation
     */
    int createBrand(PmsBrandParam pmsBrandParam);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int updateBrand(Long id, PmsBrandParam pmsBrandParam);

    /**
     * Auto-generated documentation
     */
    int deleteBrand(Long id);

    /**
     * Auto-generated documentation
     */
    int deleteBrand(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    List<PmsBrand> listBrand(String keyword, Integer showStatus, int pageNum, int pageSize);

    /**
     * Auto-generated documentation
     */
    PmsBrand getBrand(Long id);

    /**
     * Auto-generated documentation
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * Auto-generated documentation
     */
    int updateFactoryStatus(List<Long> ids, Integer factoryStatus);
}
