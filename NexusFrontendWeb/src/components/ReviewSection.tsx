import React, { useEffect, useState } from 'react';
import {
  Box,
  Typography,
  Button,
  TextField,
  Rating,
  List,
  ListItem,
  Avatar,
  IconButton,
  Chip,
  Paper,
} from '@mui/material';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import ThumbUpOutlinedIcon from '@mui/icons-material/ThumbUpOutlined';
import ThumbDownOutlinedIcon from '@mui/icons-material/ThumbDownOutlined';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import ThumbDownIcon from '@mui/icons-material/ThumbDown';
import VerifiedIcon from '@mui/icons-material/Verified';
import {
  getReviewsByProductId,
  submitReview,
  likeReview,
  dislikeReview,
  uploadReviewAttachment,
  type ProductReview,
} from '../api/engagement';
import { resolveImageUrl } from '../utils/media';

interface AttachmentParam {
  mediaType: string;
  mediaUrl: string;
  sortOrder: number;
}

export const ReviewSection: React.FC<{ productId: number }> = ({ productId }) => {
  const [reviews, setReviews] = useState<ProductReview[]>([]);
  const [newReviewText, setNewReviewText] = useState('');
  const [rating, setRating] = useState<number | null>(5);
  const [attachmentList, setAttachmentList] = useState<AttachmentParam[]>([]);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    loadReviews();
  }, [productId]);

  const loadReviews = async () => {
    try {
      const res = await getReviewsByProductId(productId);
      setReviews(res.content || []);
    } catch (error) {
      console.error('Failed to load reviews', error);
    }
  };

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      try {
        const res = await uploadReviewAttachment(file);
        const newMedia: AttachmentParam = {
          mediaType: file.type.startsWith('video/') ? 'VIDEO' : 'IMAGE',
          mediaUrl: res.url,
          sortOrder: attachmentList.length,
        };
        setAttachmentList([...attachmentList, newMedia]);
      } catch {
        alert('Upload failed');
      }
    }
  };

  const handleLike = async (id: number) => {
    try {
      await likeReview(id);
      loadReviews();
    } catch {
      alert('Failed to like review. Please login.');
    }
  };

  const handleDislike = async (id: number) => {
    try {
      await dislikeReview(id);
      loadReviews();
    } catch {
      alert('Failed to dislike review. Please login.');
    }
  };

  const handleSubmit = async () => {
    if (!newReviewText.trim()) return;
    setSubmitting(true);
    try {
      await submitReview({
        productId,
        parentReviewId: null,
        rating: rating || 5,
        content: newReviewText,
        attachmentList,
      });
      setNewReviewText('');
      setRating(5);
      setAttachmentList([]);
      loadReviews();
      alert('Thank you! Your review has been submitted.');
    } catch {
      alert('Failed to post review. Please login as a verified buyer.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Box sx={{ mt: 5 }}>
      <Typography variant="h5" sx={{ fontWeight: 700, mb: 3 }}>
        Verified Customer Reviews ({reviews.length})
      </Typography>

      {/* Write a Review Box */}
      <Paper variant="outlined" sx={{ p: 3, mb: 4, borderRadius: 2 }}>
        <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 1 }}>
          Write a Review
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
          <Typography variant="body2" color="text.secondary">Your Rating:</Typography>
          <Rating value={rating} onChange={(_, v) => setRating(v)} />
        </Box>
        <TextField
          fullWidth
          multiline
          rows={3}
          value={newReviewText}
          onChange={(e) => setNewReviewText(e.target.value)}
          placeholder="Share your experience with this product..."
          sx={{ mb: 2 }}
        />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Button
              variant="outlined"
              component="label"
              size="small"
              startIcon={<PhotoCamera />}
            >
              Add Photo
              <input hidden accept="image/*,video/*" type="file" onChange={handleUpload} />
            </Button>
            {attachmentList.map((_m, i) => (
              <Chip
                key={i}
                label={`Photo ${i + 1}`}
                size="small"
                onDelete={() => setAttachmentList(attachmentList.filter((_, idx) => idx !== i))}
              />
            ))}
          </Box>
          <Button
            variant="contained"
            onClick={handleSubmit}
            disabled={submitting || !newReviewText.trim()}
          >
            Submit Review
          </Button>
        </Box>
      </Paper>

      {/* Reviews List */}
      <List sx={{ display: 'flex', flexDirection: 'column', gap: 2, p: 0 }}>
        {reviews.map((review) => {
          const authorName = review.authorDisplayName || review.memberNickName || 'Verified Buyer';
          const media = review.attachmentList || [];

          return (
            <Paper key={review.id} variant="outlined" sx={{ p: 2.5, borderRadius: 2 }}>
              <ListItem alignItems="flex-start" disableGutters sx={{ flexDirection: 'column', p: 0 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', width: '100%', mb: 1.5 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                    <Avatar
                      src={resolveImageUrl(review.authorAvatarUrl || review.memberIcon)}
                      sx={{ width: 36, height: 36 }}
                    >
                      {authorName[0]?.toUpperCase()}
                    </Avatar>
                    <Box>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <Typography sx={{ fontWeight: 600, fontSize: '0.95rem' }}>
                          {authorName}
                        </Typography>
                        <Chip
                          icon={<VerifiedIcon sx={{ fontSize: '14px !important' }} />}
                          label="Verified Purchase"
                          size="small"
                          color="success"
                          variant="outlined"
                          sx={{ height: 20, fontSize: '0.65rem' }}
                        />
                      </Box>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mt: 0.25 }}>
                        <Rating value={review.rating} readOnly size="small" />
                        <Typography variant="caption" color="text.secondary">
                          {new Date(review.createdAt || review.createTime || Date.now()).toLocaleDateString()}
                        </Typography>
                      </Box>
                    </Box>
                  </Box>

                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    <IconButton
                      size="small"
                      onClick={() => handleLike(review.id)}
                      color={review.userLiked ? 'primary' : 'default'}
                    >
                      {review.userLiked ? <ThumbUpIcon fontSize="small" /> : <ThumbUpOutlinedIcon fontSize="small" />}
                    </IconButton>
                    <Typography variant="caption">{review.likeCount || review.helpfulCount || 0}</Typography>

                    <IconButton
                      size="small"
                      onClick={() => handleDislike(review.id)}
                      color={review.userDisliked ? 'primary' : 'default'}
                    >
                      {review.userDisliked ? <ThumbDownIcon fontSize="small" /> : <ThumbDownOutlinedIcon fontSize="small" />}
                    </IconButton>
                    <Typography variant="caption">{review.dislikeCount || 0}</Typography>
                  </Box>
                </Box>

                <Typography variant="body1" sx={{ mt: 0.5, lineHeight: 1.6 }}>
                  {review.body || review.content}
                </Typography>

                {/* Review Photos (engagement.review_media) */}
                {media.length > 0 && (
                  <Box sx={{ display: 'flex', gap: 1.5, flexWrap: 'wrap', mt: 2 }}>
                    {media.map((m, i) => (
                      <Box
                        key={m.id || i}
                        sx={{
                          borderRadius: 2,
                          overflow: 'hidden',
                          border: '1px solid #E2E8F0',
                          width: 100,
                          height: 100,
                        }}
                      >
                        <img
                          src={resolveImageUrl(m.mediaUrl || m.url)}
                          alt="Review attachment"
                          style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                        />
                      </Box>
                    ))}
                  </Box>
                )}
              </ListItem>
            </Paper>
          );
        })}
        {reviews.length === 0 && (
          <Typography color="text.secondary" sx={{ textAlign: 'center', py: 4 }}>
            No reviews for this product yet. Be the first to share your thoughts!
          </Typography>
        )}
      </List>
    </Box>
  );
};
