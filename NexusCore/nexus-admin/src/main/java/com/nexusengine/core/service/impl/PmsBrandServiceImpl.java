package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.dto.PmsBrandParam;
import com.nexusengine.core.repository.PmsBrandRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.service.PmsBrandService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PmsBrandServiceImpl implements PmsBrandService {
    @Autowired
    private PmsBrandRepository brandRepository;
    @Autowired
    private PmsProductRepository productRepository;

    @Override
    public List<PmsBrand> listAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public int createBrand(PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        if (StrUtil.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        brandRepository.save(pmsBrand);
        return 1;
    }

    @Override
    public int updateBrand(Long id, PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);
        if (StrUtil.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        brandRepository.save(pmsBrand);
        return 1;
    }

    @Override
    public int deleteBrand(Long id) {
        brandRepository.deleteById(id);
        return 1;
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        brandRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public List<PmsBrand> listBrand(String keyword, Integer showStatus, int pageNum, int pageSize) {
        return brandRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        List<PmsBrand> brands = brandRepository.findAllById(ids);
        for (PmsBrand brand : brands) {
            brand.setShowStatus(showStatus);
            brandRepository.save(brand);
        }
        return brands.size();
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        List<PmsBrand> brands = brandRepository.findAllById(ids);
        for (PmsBrand brand : brands) {
            brand.setFactoryStatus(factoryStatus);
            brandRepository.save(brand);
        }
        return brands.size();
    }
}
