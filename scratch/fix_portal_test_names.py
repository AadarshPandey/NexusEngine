import os
import re

os.chdir("NexusCore")
replacements = {
    "PmsPortalProductDetail": "ProductDetail",
    "PmsProductCategoryNode": "CategoryNode",
    "OmsOrderDetail": "OrderDetail",
    "OmsOrderReturnApplyParam": "ReturnApplyParam",
    "UmsMemberLoginParam": "MemberLoginParam",
    "SmsCouponHistoryDetail": "CouponHistoryDetail",
}

def process_module(module_path):
    for root, dirs, files in os.walk(module_path):
        for file in files:
            if not file.endswith(".java"): continue
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
                
            orig = content
            
            for old_name, new_name in replacements.items():
                if old_name in content:
                    content = re.sub(r'\b' + old_name + r'\b', new_name, content)
                    
            if content != orig:
                with open(filepath, "w") as f:
                    f.write(content)

process_module("nexus-portal-api/src/test/java")
