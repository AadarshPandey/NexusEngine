import re

filepath = './NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java'
with open(filepath, 'r') as f:
    content = f.read()

# Change the method signature and implementation
old_method = """    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) return;
        List<SmsCouponHistory> histories = couponHistoryRepository.findByMemberIdAndUseStatus(memberId, useStatus == 0 ? 1 : 0);
        List<SmsCouponHistory> filtered = histories.stream()
                .filter(h -> h.getCouponId().equals(couponId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filtered)) {
            SmsCouponHistory couponHistory = filtered.get(0);
            couponHistory.setUseTime(java.time.LocalDateTime.now());
            couponHistory.setUseStatus(useStatus);
            couponHistoryRepository.save(couponHistory);
        }
    }"""

new_method = """    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus, OmsOrder order) {
        if (couponId == null) return;
        List<SmsCouponHistory> histories = couponHistoryRepository.findByMemberIdAndUseStatus(memberId, useStatus == 0 ? 1 : 0);
        List<SmsCouponHistory> filtered = histories.stream()
                .filter(h -> h.getCouponId().equals(couponId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filtered)) {
            SmsCouponHistory couponHistory = filtered.get(0);
            couponHistory.setUseTime(useStatus == 1 ? java.time.LocalDateTime.now() : null);
            couponHistory.setUseStatus(useStatus);
            if (useStatus == 1 && order != null) {
                couponHistory.setOrderId(order.getId());
                couponHistory.setOrderSn(order.getOrderSn());
            } else if (useStatus == 0) {
                couponHistory.setOrderId(null);
                couponHistory.setOrderSn(null);
            }
            couponHistoryRepository.save(couponHistory);
        }
    }"""

content = content.replace(old_method, new_method)

# Fix callers
# line 228: updateCouponStatus(orderParam.couponId(), currentMember.getId(), 1);
content = re.sub(r'updateCouponStatus\(orderParam\.couponId\(\), currentMember\.getId\(\), 1\);',
                 r'updateCouponStatus(orderParam.couponId(), currentMember.getId(), 1, order);', content)

# line 308: updateCouponStatus(timeOutOrder.omsOrder().getCouponId(), timeOutOrder.omsOrder().getMemberId(), 0);
content = re.sub(r'updateCouponStatus\(timeOutOrder\.omsOrder\(\)\.getCouponId\(\), timeOutOrder\.omsOrder\(\)\.getMemberId\(\), 0\);',
                 r'updateCouponStatus(timeOutOrder.omsOrder().getCouponId(), timeOutOrder.omsOrder().getMemberId(), 0, null);', content)

# line 344: updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
content = re.sub(r'updateCouponStatus\(cancelOrder\.getCouponId\(\), cancelOrder\.getMemberId\(\), 0\);',
                 r'updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0, null);', content)

with open(filepath, 'w') as f:
    f.write(content)

print("Fixed checkout coupon history logic")
