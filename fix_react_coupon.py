import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    original = content
    
    if 'CouponList.tsx' in filepath:
        # Remove formatPlatform function completely
        content = re.sub(r'const formatPlatform = \(platform: number\) => {.*?};\n', '', content, flags=re.DOTALL)
        # Remove TableCell Platform
        content = re.sub(r'<TableCell>Platform</TableCell>\n?', '', content)
        content = re.sub(r'<TableCell>\{formatPlatform\(row\.platform\)\}</TableCell>\n?', '', content)
        content = content.replace('minPoint', 'minOrderAmount')

    if 'CouponAdd.tsx' in filepath or 'CouponUpdate.tsx' in filepath:
        content = re.sub(r'platform:\s*0,\n?', '', content)
        content = re.sub(r'publishCount:\s*100,\n?', '', content)
        content = re.sub(r'enableTime:\s*.*?,\n?', '', content)

        # Regex to remove the whole JSX blocks for Platform, Publish Count, Enable Time
        # E.g. <FormControl fullWidth margin="normal"> <InputLabel>Platform</InputLabel> ... </FormControl>
        content = re.sub(r'<FormControl fullWidth margin="normal">\s*<InputLabel>Platform</InputLabel>.*?</FormControl>', '', content, flags=re.DOTALL)
        content = re.sub(r'<TextField[^>]*label="Publish Count".*?/>', '', content, flags=re.DOTALL)
        content = re.sub(r'<DateTimePicker[^>]*label="Enable Time".*?/>', '', content, flags=re.DOTALL)
        
        # In case some fields are inside a Box with display flex
        # If there's an empty Box or Grid, it might be fine, but let's be careful.
        
        content = content.replace('minPoint', 'minOrderAmount')

    if content != original:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Fixed {filepath}")

process_file('./NexusAdminPanel/src/pages/sms/CouponList.tsx')
process_file('./NexusAdminPanel/src/pages/sms/CouponAdd.tsx')
process_file('./NexusAdminPanel/src/pages/sms/CouponUpdate.tsx')
