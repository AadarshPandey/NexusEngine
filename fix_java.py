import re

def fix(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    original = content
    content = content.replace('getMinPoint()', 'getMinOrderAmount()')
    content = content.replace('setMinPoint', 'setMinOrderAmount')
    
    # In UmsMemberCouponServiceImpl: if (now.isBefore(coupon.getEnableTime()))
    # Since enable_time is gone, we should use startTime
    content = content.replace('getEnableTime()', 'getStartTime()')

    # In SmsCouponServiceImpl:
    # smsCoupon.setCount(smsCoupon.getPublishCount());
    # smsCoupon.setUseCount(0);
    # Since publishCount is gone, the admin should just supply count directly, or maybe they were copying it. Let's comment out or change it.
    if 'SmsCouponServiceImpl.java' in filepath:
        content = re.sub(r'smsCoupon\.setCount\(smsCoupon\.getPublishCount\(\)\);\n?', '', content)
        content = re.sub(r'smsCoupon\.setUseCount\(0\);\n?', '', content)

    if content != original:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Fixed {filepath}")

fix('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/UmsMemberCouponServiceImpl.java')
fix('./NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/SmsCouponServiceImpl.java')
fix('./NexusCore/nexus-portal/src/test/java/com/nexusengine/core/portal/TestFixtures.java')
