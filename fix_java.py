import os

def clean_file(filepath, strings_to_remove):
    with open(filepath, 'r') as f: lines = f.readlines()
    new_lines = []
    
    skip_next = False
    in_block_to_delete = False
    braces_depth = 0
    
    for line in lines:
        if any(s in line for s in strings_to_remove):
            continue
            
        # specifically for order.setReceiverProvince etc in OmsOrderServiceImpl
        if 'setReceiverProvince(' in line or 'setReceiverCity(' in line or 'setReceiverRegion(' in line:
            continue
            
        new_lines.append(line)

    with open(filepath, 'w') as f:
        f.write(''.join(new_lines))

strings_to_remove_portal = [
    '.setPaymentId(', '.setEarnedPoints(', '.setPointsDiscountAmount(', 
    '.setPromotionInfo(', '.setMemberUsername(', '.setSourceType(', 
    '.setReceiverProvince(', '.setReceiverCity(', '.setReceiverRegion(', 
    '.setAutoConfirmDay(', 'orderParam.usePoints()', 'calcEarnedPoints(',
    'calcPointsAmount(', '.getPointsDiscountAmount(', '.getEarnedPoints(',
    'member.getPoints()', 'memberService.updatePoints(', 'getUsedPoints(', '.setUsedPoints('
]

clean_file('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/OmsPortalOrderServiceImpl.java', strings_to_remove_portal)
clean_file('./NexusCore/nexus-admin/src/main/java/com/nexusengine/core/service/impl/OmsOrderServiceImpl.java', ['.setReceiverProvince(', '.setReceiverCity(', '.setReceiverRegion(', 'admin.getVendorId()', 'findByVendorIdOrderByCreateTimeDesc'])

print("Cleaned!")
