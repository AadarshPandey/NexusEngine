package com.nexusengine.core.admin.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;

import com.nexusengine.core.admin.ordering.dto.OrderStatusUpdateParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestQueryParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestResult;
import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.util.List;


@Service("adminReturnRequestServiceImpl")
@RequiredArgsConstructor
public class ReturnRequestServiceImpl implements ReturnRequestService {
    private final ReturnRequestRepository returnApplyRepository;
    private final com.nexusengine.core.domain.catalog.repository.BrandRepository brandRepository;
    private final com.nexusengine.core.admin.security.AdminTenantContext adminTenantContext;
    private final com.nexusengine.core.domain.payment.repository.PaymentTransactionRepository paymentTransactionRepository;
    private final com.nexusengine.core.domain.ordering.repository.MerchantAddressRepository merchantAddressRepository;

    private void assertReturnBrandAccess(ReturnRequest apply) {
        adminTenantContext.currentBrandId().ifPresent(brandId -> {
            String brandName = brandRepository.findById(brandId)
                    .map(com.nexusengine.core.domain.catalog.entity.Brand::getName)
                    .orElse("");
            if (apply.getProductBrand() == null || !brandName.equalsIgnoreCase(apply.getProductBrand())) {
                throw new com.nexusengine.core.common.exception.ApiException(com.nexusengine.core.common.api.ResultCode.FORBIDDEN);
            }
        });
    }

    @Override
    public List<ReturnRequest> list(ReturnRequestQueryParam queryParam, Integer pageSize, Integer pageNum) {
        return returnApplyRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (queryParam != null) {
                if (queryParam.id() != null) {
                    predicates.add(cb.equal(root.get("id"), queryParam.id()));
                }
                if (queryParam.status() != null) {
                    predicates.add(cb.equal(root.get("status"), queryParam.status()));
                }
                if (org.springframework.util.StringUtils.hasText(queryParam.receiverKeyword())) {
                    predicates.add(cb.or(
                            cb.like(root.get("returnName"), "%" + queryParam.receiverKeyword() + "%"),
                            cb.like(root.get("returnPhone"), "%" + queryParam.receiverKeyword() + "%")
                    ));
                }
            }
            adminTenantContext.currentBrandId().ifPresent(brandId -> {
                String brandName = brandRepository.findById(brandId)
                        .map(com.nexusengine.core.domain.catalog.entity.Brand::getName)
                        .orElse("");
                predicates.add(cb.equal(root.get("productBrand"), brandName));
            });
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createTime"));
    }

    @Override
    public int delete(List<Long> ids) {
        for (Long id : ids) {
            returnApplyRepository.findById(id).ifPresent(this::assertReturnBrandAccess);
        }
        returnApplyRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateStatus(Long id, OrderStatusUpdateParam statusParam) {
        ReturnRequest returnApply = returnApplyRepository.findById(id).orElse(null);
        if (returnApply == null) return 0;
        assertReturnBrandAccess(returnApply);
        Integer status = statusParam.status();
        if (status.equals(1)) {
            returnApply.setStatus(1);
            if (statusParam.returnAmount() != null) {
                returnApply.setReturnAmount(statusParam.returnAmount());
            }
            returnApply.setCompanyAddressId(statusParam.companyAddressId());
            returnApply.setHandleTime(java.time.OffsetDateTime.now());
            returnApply.setHandleMan(statusParam.handleMan() != null ? statusParam.handleMan() : "admin");
            returnApply.setHandleNote(statusParam.handleNote());
        } else if (status.equals(2)) {
            returnApply.setStatus(2);
            returnApply.setReceiveTime(java.time.OffsetDateTime.now());
            returnApply.setReceiveMan(statusParam.receiveMan() != null ? statusParam.receiveMan() : "admin");
            returnApply.setReceiveNote(statusParam.receiveNote());

            // Record payment refund in payment.payment_transaction
            if (returnApply.getOrderId() != null && returnApply.getReturnAmount() != null) {
                com.nexusengine.core.domain.payment.entity.PaymentTransaction refundTxn = new com.nexusengine.core.domain.payment.entity.PaymentTransaction();
                refundTxn.setOrderId(returnApply.getOrderId());
                refundTxn.setTransactionId("REF-" + System.currentTimeMillis() + "-" + returnApply.getId());
                refundTxn.setTransactionType("REFUND");
                refundTxn.setAmount(returnApply.getReturnAmount());
                refundTxn.setStatus("SUCCESS");
                refundTxn.setCreateTime(java.time.OffsetDateTime.now());
                refundTxn.setWebhookPayload("{\"source\":\"admin_return_approval\",\"returnRequestId\":" + returnApply.getId() + "}");
                paymentTransactionRepository.save(refundTxn);
            }
        } else if (status.equals(3)) {
            returnApply.setStatus(3);
            returnApply.setHandleTime(java.time.OffsetDateTime.now());
            returnApply.setHandleMan(statusParam.handleMan() != null ? statusParam.handleMan() : "admin");
            returnApply.setHandleNote(statusParam.handleNote());
        } else {
            return 0;
        }
        returnApplyRepository.save(returnApply);
        return 1;
    }

    @Override
    public ReturnRequestResult getItem(Long id) {
        ReturnRequest apply = returnApplyRepository.findById(id).orElse(null);
        if (apply == null) return null;
        assertReturnBrandAccess(apply);
        com.nexusengine.core.domain.ordering.entity.MerchantAddress companyAddress = null;
        if (apply.getCompanyAddressId() != null) {
            companyAddress = merchantAddressRepository.findById(apply.getCompanyAddressId()).orElse(null);
        }
        return new ReturnRequestResult(apply, companyAddress);
    }
}
