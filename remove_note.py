import re
import os

def remove_from_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    original = content

    # Backend Java
    if filepath.endswith('SmsCoupon.java'):
        content = re.sub(r'@Schema\(title = "Note"\)\s*private String note;\n?', '', content)
        content = re.sub(r'private String note;\n?', '', content)

    # Frontend React/TS
    if filepath.endswith('coupon.d.ts') or filepath.endswith('product.ts') or filepath.endswith('order.ts'):
        content = re.sub(r'note\??: string;?\n?', '', content)

    if filepath.endswith('CouponList.tsx'):
        # Remove Note column headers and cells
        content = re.sub(r'<TableCell>Note</TableCell>\n?', '', content)
        content = re.sub(r'<TableCell>\{row\.note\}</TableCell>\n?', '', content)

    if filepath.endswith('CouponAdd.tsx') or filepath.endswith('CouponUpdate.tsx'):
        content = re.sub(r'note:\s*\'\',\n?', '', content)
        # Remove the Note TextField block
        content = re.sub(r'<TextField[^>]*label="Note".*?/>', '', content, flags=re.DOTALL)

    if content != original:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('.'):
    if 'target' in root or 'node_modules' in root:
        continue
    for file in files:
        if file.endswith(('.java', '.ts', '.tsx')):
            remove_from_file(os.path.join(root, file))
