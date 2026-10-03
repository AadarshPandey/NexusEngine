import re

filepath = './resource_setup/postgres_data.sql'
with open(filepath, 'r') as f: content = f.read()

# 1. Update oms_order COPY statement
old_order_copy = 'COPY orders.oms_order (id, member_id, coupon_id, order_sn, create_time, member_username, total_amount, pay_amount, freight_amount, promotion_amount, points_discount_amount, coupon_amount, discount_amount, pay_type, source_type, status, order_type, delivery_company, delivery_sn, auto_confirm_day, earned_points, promotion_info, bill_type, bill_header, bill_content, bill_receiver_phone, bill_receiver_email, receiver_name, receiver_phone, receiver_post_code, receiver_province, receiver_city, receiver_region, receiver_detail_address, note, confirm_status, delete_status, used_points, payment_id, payment_time, delivery_time, receive_time, comment_time, modify_time, vendor_id) FROM stdin;'
new_order_copy = 'COPY orders.oms_order (id, member_id, coupon_id, order_sn, create_time, total_amount, pay_amount, freight_amount, promotion_amount, coupon_amount, discount_amount, pay_type, status, order_type, delivery_company, delivery_sn, receiver_name, receiver_phone, receiver_post_code, receiver_detail_address, note, confirm_status, delete_status, payment_time, delivery_time, receive_time, comment_time, modify_time) FROM stdin;'
content = content.replace(old_order_copy, new_order_copy)

# 2. Update oms_order_item COPY statement
old_item_copy = 'COPY orders.oms_order_item (id, order_id, order_sn, product_id, product_pic, product_name, product_brand, product_sn, product_price, product_quantity, product_sku_id, product_sku_code, product_category_id, promotion_name, promotion_amount, coupon_amount, points_discount_amount, real_amount, earned_points, product_attr) FROM stdin;'
new_item_copy = 'COPY orders.oms_order_item (id, order_id, order_sn, product_id, product_pic, product_name, product_brand, product_sn, product_price, product_quantity, product_sku_id, product_sku_code, product_category_id, promotion_name, promotion_amount, coupon_amount, real_amount, product_attr) FROM stdin;'
content = content.replace(old_item_copy, new_item_copy)


def process_order(m):
    header = m.group(1)
    data = m.group(2)
    footer = m.group(3)
    drop_indices = [44, 38, 37, 32, 31, 30, 26, 25, 24, 23, 22, 21, 20, 19, 14, 10, 5]
    new_data = []
    for line in data.split('\n'):
        if line.strip():
            parts = line.split('\t')
            if len(parts) >= 44:
                for idx in drop_indices:
                    if idx < len(parts):
                        parts.pop(idx)
            new_data.append('\t'.join(parts))
        else:
            new_data.append(line)
    return header + '\n'.join(new_data) + footer

content = re.sub(r'(COPY orders\.oms_order \([^)]+\) FROM stdin;\n)(.*?)(\\\.\n)', process_order, content, flags=re.DOTALL)

def process_item(m):
    header = m.group(1)
    data = m.group(2)
    footer = m.group(3)
    drop_indices = [18, 16]
    new_data = []
    for line in data.split('\n'):
        if line.strip():
            parts = line.split('\t')
            if len(parts) >= 19:
                for idx in drop_indices:
                    if idx < len(parts):
                        parts.pop(idx)
            new_data.append('\t'.join(parts))
        else:
            new_data.append(line)
    return header + '\n'.join(new_data) + footer

content = re.sub(r'(COPY orders\.oms_order_item \([^)]+\) FROM stdin;\n)(.*?)(\\\.\n)', process_item, content, flags=re.DOTALL)

with open(filepath, 'w') as f: f.write(content)
print("postgres_data TSV blocks cleaned!")
