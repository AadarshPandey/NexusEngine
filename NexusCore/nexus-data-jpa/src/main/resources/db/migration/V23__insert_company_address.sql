-- V23: Insert default return warehouse address
-- Required for processing refunds in the admin panel

INSERT INTO oms_company_address (id, address_name, send_status, receive_status, name, phone, province, city, region, detail_address) 
VALUES (1, 'Main Warehouse', 1, 1, 'Receiving Dept', '1-800-555-0199', 'CA', 'San Jose', 'Santa Clara', '100 Tech Drive, Dock 4')
ON CONFLICT DO NOTHING;
