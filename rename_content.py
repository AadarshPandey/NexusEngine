import os
import re

replacements = {
    "PmsProductFullReduction": "PmsProductSpendDiscount",
    "pms_product_full_reduction": "pms_product_spend_discount",
    "productFullReductionList": "productSpendDiscountList",
    "productFullReduction": "productSpendDiscount",
    
    "PmsProductLadder": "PmsProductVolumeDiscount",
    "pms_product_ladder": "pms_product_volume_discount",
    "productLadderList": "productVolumeDiscountList",
    "productLadder": "productVolumeDiscount",
    
    "PmsProductOperateLog": "PmsProductAuditLog",
    "pms_product_operate_log": "pms_product_audit_log",
    "operateLog": "auditLog",
    "OperateLog": "AuditLog",
    
    "PmsProductVerifyRecord": "PmsProductApprovalHistory",
    "pms_product_verify_record": "pms_product_approval_history",
    "verifyRecord": "approvalHistory",
    "VerifyRecord": "ApprovalHistory",
    
    "PmsProductMedia": "PmsProductGallery",
    "pms_product_media": "pms_product_gallery",
    "PmsReviewMedia": "PmsReviewAttachment",
    "pms_review_media": "pms_review_attachments",
}

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    new_content = content
    for old, new in replacements.items():
        new_content = new_content.replace(old, new)
        
    if "PmsProduct" in new_content or "PmsReview" in new_content or "mediaList" in new_content:
        # Care needed for mediaList
        # Inside PmsProduct.java: mediaList -> galleryList
        # Inside PmsReview.java: mediaList -> attachmentList
        if "PmsProduct.java" in filepath:
            new_content = new_content.replace("mediaList", "galleryList")
        elif "PmsReview.java" in filepath:
            new_content = new_content.replace("mediaList", "attachmentList")
            
        # PmsProductServiceImpl etc
        if "PmsProduct" in filepath and "mediaList" in new_content:
            new_content = new_content.replace("mediaList", "galleryList")
        if "PmsPortalReview" in filepath and "mediaList" in new_content:
            new_content = new_content.replace("mediaList", "attachmentList")

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)

for root, dirs, files in os.walk("./NexusCore"):
    for file in files:
        if file.endswith(".java") or file.endswith(".xml") or file.endswith(".sql"):
            process_file(os.path.join(root, file))

for root, dirs, files in os.walk("./NexusAdminPanel"):
    for file in files:
        if file.endswith(".ts") or file.endswith(".tsx"):
            process_file(os.path.join(root, file))

for root, dirs, files in os.walk("./NexusFrontendWeb"):
    for file in files:
        if file.endswith(".ts") or file.endswith(".tsx"):
            process_file(os.path.join(root, file))

# And postgres data
process_file("./resource_setup/postgres_data.sql")

