import re
import os

def replace_in_file(filepath, old, new):
    if not os.path.exists(filepath): return
    with open(filepath, 'r') as f:
        content = f.read()
    content = content.replace(old, new)
    with open(filepath, 'w') as f:
        f.write(content)

replace_in_file("./NexusFrontendWeb/src/views/ProductDetail.tsx", "mediaList", "galleryList")
replace_in_file("./NexusFrontendWeb/src/components/ReviewSection.tsx", "mediaList", "attachmentList")
replace_in_file("./NexusFrontendWeb/src/components/ReviewSection.tsx", "MediaParam", "AttachmentParam")
replace_in_file("./NexusFrontendWeb/src/components/ReviewSection.tsx", "replyMediaList", "replyAttachmentList")
replace_in_file("./NexusFrontendWeb/src/components/ReviewSection.tsx", "setMediaList", "setAttachmentList")
replace_in_file("./NexusFrontendWeb/src/api/home.ts", "mediaList", "galleryList")
