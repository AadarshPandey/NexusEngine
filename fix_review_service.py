import re

with open('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java', 'r') as f:
    content = f.read()

# Remove imports
content = re.sub(r'import com\.nexusengine\.core\.model\.PmsComment;\n', '', content)
content = re.sub(r'import com\.nexusengine\.core\.model\.PmsCommentReply;\n', '', content)
content = re.sub(r'import com\.nexusengine\.core\.repository\.PmsCommentRepository;\n', '', content)
content = re.sub(r'import com\.nexusengine\.core\.repository\.PmsCommentReplyRepository;\n', '', content)

# Remove repository injections
content = re.sub(r'\s*@Autowired\s*private PmsCommentRepository commentRepository;\n', '', content)
content = re.sub(r'\s*@Autowired\s*private PmsCommentReplyRepository commentReplyRepository;\n', '', content)

# Remove getCommentForReview method
content = re.sub(r'\s*private PmsComment getCommentForReview\(Long reviewId\) \{[\s\S]*?\}\n', '\n', content)

# Remove sync logic in like() and dislike()
# The like() method has things like:
# PmsComment comment = getCommentForReview(id);
# if (comment != null) { comment.setLikesCount(...) }
# if (comment != null) commentRepository.save(comment);

content = re.sub(r'\s*PmsComment comment = getCommentForReview\(id\);\n', '\n', content)
content = re.sub(r'\s*if \(comment != null\) \{\s*comment\.setLikesCount\([^)]+\);\s*\}\n', '\n', content)
content = re.sub(r'\s*if \(comment != null\) \{\s*comment\.setDislikeCount\([^)]+\);\s*\}\n', '\n', content)
content = re.sub(r'\s*if \(comment != null\) commentRepository\.save\(comment\);\n', '\n', content)

# Remove sync logic in addReview()
# It contains a massive block under: // Sync to PmsComment
# PmsComment comment = new PmsComment(); ... commentRepository.save(comment);
# And then under: // It's a reply! Save the child comment in PmsComment first

sync_block_1 = r'\s*// Sync to PmsComment\s*if \(param\.parentId\(\) == null\) \{[\s\S]*?\}\s*// It\'s a reply! Save the child comment in PmsComment first\s*else \{[\s\S]*?\}\n'
content = re.sub(sync_block_1, '\n', content)

with open('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/service/impl/PmsPortalReviewServiceImpl.java', 'w') as f:
    f.write(content)
