import os
import re

def remove_from_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    original_content = content

    # Backend Java
    if filepath.endswith('SmsCoupon.java'):
        content = re.sub(r'private Integer platform;\n?', '', content)
        content = re.sub(r'private Integer publishCount;\n?', '', content)
        content = re.sub(r'private Integer useCount;\n?', '', content)
        content = re.sub(r'private LocalDateTime enableTime;\n?', '', content)
        content = re.sub(r'minPoint', 'minOrderAmount', content)
        content = re.sub(r'min_point', 'min_order_amount', content)

    if filepath.endswith('UmsMemberCouponServiceImpl.java') or filepath.endswith('TestFixtures.java'):
        content = re.sub(r'minPoint', 'minOrderAmount', content)

    if filepath.endswith('SmsCouponRepository.java'):
        # Just in case useCount is mentioned
        content = re.sub(r'c.useCount = c.useCount \+ 1,?', '', content)

    # Frontend React/TS
    if filepath.endswith('coupon.d.ts') or filepath.endswith('product.ts') or filepath.endswith('order.ts'):
        content = re.sub(r'platform\??: number;?\n?', '', content)
        content = re.sub(r'publishCount\??: number;?\n?', '', content)
        content = re.sub(r'useCount\??: number;?\n?', '', content)
        content = re.sub(r'enableTime\??: string;?\n?', '', content)
        content = re.sub(r'minPoint', 'minOrderAmount', content)

    if filepath.endswith('CouponList.tsx') or filepath.endswith('CouponUpdate.tsx') or filepath.endswith('CouponAdd.tsx'):
        # Remove lines that contain these words, or try to replace the blocks.
        # It's better to just regex out the block or specific lines
        pass # We will handle frontend manually or carefully

    if content != original_content:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('.'):
    if 'target' in root or 'node_modules' in root:
        continue
    for file in files:
        if file.endswith(('.java', '.ts', '.tsx')):
            remove_from_file(os.path.join(root, file))
