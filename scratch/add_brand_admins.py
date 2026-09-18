import psycopg2
import re

conn = psycopg2.connect(dbname='nexuscore', user='postgres', host='127.0.0.1', port='5433', password='postgres')
cur = conn.cursor()

# Get all brands
cur.execute("SELECT id, name FROM pms_brand")
brands = cur.fetchall()

hash_pw = "$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO"

for b_id, b_name in brands:
    # create a username from brand name: lowercase, replace spaces with underscores, remove special chars
    clean_name = re.sub(r'[^a-zA-Z0-9_]', '', b_name.lower().replace(' ', '_'))
    username = f"{clean_name}_admin"
    
    # check if exists
    cur.execute("SELECT id FROM ums_admin WHERE username=%s", (username,))
    if not cur.fetchone():
        # insert
        cur.execute("""
            INSERT INTO ums_admin (username, password, email, nick_name, status, vendor_id, create_time)
            VALUES (%s, %s, %s, %s, 1, %s, NOW())
            RETURNING id
        """, (username, hash_pw, f"{clean_name}@example.com", f"{b_name} Admin", b_id))
        
        admin_id = cur.fetchone()[0]
        # Assign to some role? Admin is role 1 usually. Let's see if role exists.
        cur.execute("INSERT INTO ums_admin_role_relation (admin_id, role_id) VALUES (%s, %s)", (admin_id, 1))

conn.commit()
cur.close()
conn.close()
print("Added brand admins!")
