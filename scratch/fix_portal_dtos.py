import re
import os

markdown_file = "BackendMigrationPlan.md"
with open(markdown_file, "r") as f:
    content = f.read()

base_src = "NexusCore/nexus-portal-api/src/main/java/com/nexusengine/core/portal"

lines = content.split('\n')
in_part52 = False
current_context = ""
current_layer = ""
moves = []

for line in lines:
    if "### 5.2 Target Structure" in line:
        in_part52 = True
        continue
    if in_part52 and "###" in line:
        break
        
    if in_part52:
        m_context = re.search(r'^├── ([a-z]+)/', line)
        if m_context:
            current_context = m_context.group(1)
            continue
            
        m_layer = re.search(r'^│   [├└]── ([a-z]+)/', line)
        if m_layer:
            current_layer = m_layer.group(1)
            continue
            
        if current_layer == "dto":
            m_file = re.search(r'^│   (?:│   |    )[├└]── ([A-Za-z0-9_]+)\.java\s+← was ([A-Za-z0-9_]+)', line)
            if m_file:
                moves.append((m_file.group(2), m_file.group(1), current_context))
                
            m_kept = re.search(r'^│   (?:│   |    )[├└]── ([A-Za-z0-9_]+)\.java\s+← kept', line)
            if m_kept:
                moves.append((m_kept.group(1), m_kept.group(1), current_context))

# Add some that were mapped incorrectly or not at all but exist in domain/
moves.append(("ConfirmOrderResultCalcAmount", "ConfirmOrderResultCalcAmount", "ordering"))
moves.append(("CartProduct", "CartProduct", "ordering"))
moves.append(("OmsOrderReturnApplyParam", "ReturnApplyParam", "ordering"))
moves.append(("UmsMemberLoginParam", "MemberLoginParam", "customer"))
moves.append(("HomeContentResult", "HomeContentResult", "home"))
moves.append(("HomeFlashPromotion", "HomeFlashPromotion", "home"))
moves.append(("FlashPromotionProduct", "FlashPromotionProduct", "promotion"))
moves.append(("CartPromotionItem", "CartPromotionItem", "promotion"))
moves.append(("SmsCouponHistoryDetail", "CouponHistoryDetail", "promotion"))
moves.append(("MemberDetails", "MemberDetails", "customer"))
moves.append(("OrderParam", "OrderParam", "ordering"))
moves.append(("QueueEnum", "QueueEnum", "ordering"))

# Also strategy files!
moves_strategy = [
    ("CouponHistoryDetail", "CouponHistoryDetail", "promotion", "strategy"),
    ("CouponHistoryDetail", "CouponHistoryDetail", "promotion", "strategy"),
]

for old_name, new_name, context in moves:
    old_path = os.path.join(base_src, "domain", f"{old_name}.java")
    new_path = os.path.join(base_src, context, "dto", f"{new_name}.java")
    
    if os.path.exists(old_path):
        os.makedirs(os.path.dirname(new_path), exist_ok=True)
        os.rename(old_path, new_path)
        print(f"Moved {old_path} -> {new_path}")
        
        with open(new_path, "r") as f:
            c = f.read()
            
        new_pkg = f"com.nexusengine.core.portal.{context}.dto"
        c = re.sub(r'package\s+com\.nexusengine\.core\.portal\.domain;', f'package {new_pkg};', c)
        
        if old_name != new_name:
            c = re.sub(r'\b' + old_name + r'\b', new_name, c)
            
        with open(new_path, "w") as f:
            f.write(c)
