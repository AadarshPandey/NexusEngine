import re

file_path = "NexusFrontendWeb/src/views/Profile.tsx"
with open(file_path, "r") as f:
    content = f.read()

# Replace the text field for Avatar URL with a button and preview
old_ui = """<TextField label="Avatar URL" fullWidth value={editProfileData.icon} onChange={e => setEditProfileData({...editProfileData, icon: e.target.value})} />"""

new_ui = """
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <Avatar src={editProfileData.icon} sx={{ width: 64, height: 64 }} />
              <Button 
                variant="outlined" 
                onClick={() => setEditProfileData({...editProfileData, icon: `https://api.dicebear.com/7.x/avataaars/svg?seed=${Math.random().toString(36).substring(7)}`})}
              >
                Change Image
              </Button>
            </Box>
"""

content = content.replace(old_ui, new_ui)

with open(file_path, "w") as f:
    f.write(content)
