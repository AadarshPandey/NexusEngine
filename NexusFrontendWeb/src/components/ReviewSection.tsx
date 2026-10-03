import React, { useEffect, useState } from 'react';
import { Box, Typography, Button, TextField, Rating, List, ListItem, Divider, Avatar, IconButton, Collapse } from '@mui/material';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import ThumbUpOutlinedIcon from '@mui/icons-material/ThumbUpOutlined';
import ThumbDownOutlinedIcon from '@mui/icons-material/ThumbDownOutlined';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import ThumbDownIcon from '@mui/icons-material/ThumbDown';
import request from '../utils/request';

interface AttachmentParam {
  mediaType: string;
  mediaUrl: string;
  sortOrder: number;
}

interface Review {
  id: number;
  productId: number;
  memberId: number;
  rating: number;
  content: string;
  createdTime: string;
  likeCount?: number;
  dislikeCount?: number;
  userLiked?: boolean;
  userDisliked?: boolean;
  attachmentList?: AttachmentParam[];
  replies?: Review[];
}

export const ReviewSection: React.FC<{ productId: number }> = ({ productId }) => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [newReviewText, setNewReviewText] = useState('');
  const [rating, setRating] = useState<number | null>(5);
  const [attachmentList, setAttachmentList] = useState<AttachmentParam[]>([]);
  
  const [replyingTo, setReplyingTo] = useState<number | null>(null);
  const [replyText, setReplyText] = useState('');
  const [replyAttachmentList, setReplyMediaList] = useState<AttachmentParam[]>([]);

  useEffect(() => {
    loadReviews();
  }, [productId]);

  const loadReviews = async () => {
    try {
      const res = await request.get<unknown, { data: { list: Review[] } }>(`/review/product/${productId}`);
      const fetchedReviews = res.data.list || [];
      
      const reviewsWithReplies = await Promise.all(
        fetchedReviews.map(async (review) => {
          try {
            const repliesRes = await request.get<unknown, { data: Review[] }>(`/review/${review.id}/replies`);
            return { ...review, replies: repliesRes.data || [] };
          } catch (err) {
            return { ...review, replies: [] };
          }
        })
      );
      
      setReviews(reviewsWithReplies);
    } catch (error) {
      console.error('Failed to load reviews', error);
    }
  };

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>, isReply: boolean = false) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      const formData = new FormData();
      formData.append('file', file);
      
      try {
        const res = await request.post<unknown, { data: { url: string } }>('/review/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        const newMedia = { mediaType: file.type.startsWith('video/') ? 'VIDEO' : 'IMAGE', mediaUrl: res.data.url, sortOrder: isReply ? replyAttachmentList.length : attachmentList.length };
        if (isReply) {
          setReplyMediaList([...replyAttachmentList, newMedia]);
        } else {
          setAttachmentList([...attachmentList, newMedia]);
        }
      } catch (err) {
        alert('Upload failed');
      }
    }
  };

  const handleLike = async (id: number) => {
    try {
      await request.post(`/review/${id}/like`);
      loadReviews(); // Reload to get updated likes
    } catch (e) {
      alert('Failed to like review.');
    }
  };

  const handleDislike = async (id: number) => {
    try {
      await request.post(`/review/${id}/dislike`);
      loadReviews();
    } catch (e) {
      alert('Failed to dislike review.');
    }
  };

  const handleSubmit = async () => {
    if (!newReviewText.trim()) return;
    try {
      await request.post('/review/create', {
        productId,
        parentId: null,
        rating,
        content: newReviewText,
        attachmentList
      });
      setNewReviewText('');
      setRating(5);
      setAttachmentList([]);
      loadReviews();
    } catch (e) {
      alert('Failed to post review. Please login.');
    }
  };

  const handleReplySubmit = async (parentId: number) => {
    if (!replyText.trim()) return;
    try {
      await request.post('/review/create', {
        productId,
        parentId,
        rating: 5,
        content: replyText,
        attachmentList: replyAttachmentList
      });
      setReplyText('');
      setReplyingTo(null);
      setReplyMediaList([]);
      loadReviews();
    } catch (e) {
      alert('Failed to post reply. Please login.');
    }
  };

  return (
    <Box sx={{ mt: 4 }}>
      <Typography variant="h5" gutterBottom>Customer Reviews</Typography>
      
      <Box sx={{ mb: 4, p: 2, border: '1px solid #eee', borderRadius: 2 }}>
        <Typography variant="subtitle1">Write a Review</Typography>
        <Rating value={rating} onChange={(_, v) => setRating(v)} sx={{ mb: 2 }} />
        <TextField
          fullWidth
          multiline
          rows={3}
          value={newReviewText}
          onChange={e => setNewReviewText(e.target.value)}
          placeholder="What did you like or dislike?"
        />
        <Box sx={{ mt: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button variant="contained" onClick={handleSubmit}>Submit Review</Button>
          <IconButton color="primary" component="label">
            <input hidden accept="image/*,video/*" type="file" onChange={(e) => handleUpload(e, false)} />
            <PhotoCamera />
          </IconButton>
          {attachmentList.map((_m, i) => (
            <Typography key={i} variant="caption">Media {i + 1}</Typography>
          ))}
        </Box>
      </Box>

      <List>
        {reviews.map(review => (
          <React.Fragment key={review.id}>
            <ListItem alignItems="flex-start" sx={{ flexDirection: 'column' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 1, width: '100%' }}>
                <Avatar sx={{ width: 32, height: 32 }}>U</Avatar>
                <Rating value={review.rating} readOnly size="small" />
                <Typography variant="caption" color="text.secondary">
                  {new Date(review.createdTime).toLocaleDateString()}
                </Typography>
                <Box sx={{ flexGrow: 1 }} />
                
                <IconButton size="small" onClick={() => handleLike(review.id)} color={review.userLiked ? 'primary' : 'default'}>
                  {review.userLiked ? <ThumbUpIcon fontSize="small" /> : <ThumbUpOutlinedIcon fontSize="small" />}
                </IconButton>
                <Typography variant="caption">{review.likeCount || 0}</Typography>

                <IconButton size="small" onClick={() => handleDislike(review.id)} color={review.userDisliked ? 'primary' : 'default'}>
                  {review.userDisliked ? <ThumbDownIcon fontSize="small" /> : <ThumbDownOutlinedIcon fontSize="small" />}
                </IconButton>
                <Typography variant="caption">{review.dislikeCount || 0}</Typography>
                
                <Button size="small" sx={{ ml: 1 }} onClick={() => { setReplyingTo(replyingTo === review.id ? null : review.id); setReplyMediaList([]); }}>
                  {replyingTo === review.id ? 'Cancel' : 'Reply'}
                </Button>
              </Box>
              <Typography variant="body1">{review.content}</Typography>
              <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mt: 1 }}>
                {review.attachmentList && review.attachmentList.map((m, i) => (
                  <Box key={i}>
                    {m.mediaType === 'IMAGE' ? (
                      <img src={m.mediaUrl.startsWith('http') ? m.mediaUrl : (import.meta.env.VITE_API_BASE_URL || '/api') + m.mediaUrl} alt="review" style={{ maxWidth: 200 }} />
                    ) : (
                      <video src={m.mediaUrl.startsWith('http') ? m.mediaUrl : (import.meta.env.VITE_API_BASE_URL || '/api') + m.mediaUrl} controls style={{ maxWidth: 300 }} />
                    )}
                  </Box>
                ))}
              </Box>

              <Collapse in={replyingTo === review.id} timeout="auto" unmountOnExit sx={{ width: '100%', mt: 2 }}>
                <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
                  <TextField 
                    fullWidth 
                    size="small" 
                    placeholder="Write a reply..." 
                    value={replyText}
                    onChange={e => setReplyText(e.target.value)}
                  />
                  <IconButton color="primary" component="label" size="small">
                    <input hidden accept="image/*,video/*" type="file" onChange={(e) => handleUpload(e, true)} />
                    <PhotoCamera fontSize="small" />
                  </IconButton>
                  <Button variant="contained" onClick={() => handleReplySubmit(review.id)}>Reply</Button>
                </Box>
                {replyAttachmentList.map((_m, i) => (
                  <Typography key={i} variant="caption" sx={{ display: 'block', mt: 0.5 }}>Reply Media {i + 1} Attached</Typography>
                ))}
              </Collapse>

              {review.replies && review.replies.length > 0 && (
                <List sx={{ width: '100%', ml: 4, mt: 1, bgcolor: 'action.hover', borderRadius: 1 }}>
                  {review.replies.map(reply => (
                    <ListItem key={reply.id} sx={{ flexDirection: 'column', alignItems: 'flex-start' }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5, width: '100%' }}>
                        <Avatar sx={{ width: 24, height: 24 }}>R</Avatar>
                        <Typography variant="caption" color="text.secondary">
                          {new Date(reply.createdTime).toLocaleDateString()}
                        </Typography>
                        <Box sx={{ flexGrow: 1 }} />
                        <IconButton size="small" onClick={() => handleLike(reply.id)} color={reply.userLiked ? 'primary' : 'default'}>
                          {reply.userLiked ? <ThumbUpIcon fontSize="small" sx={{ fontSize: 16 }} /> : <ThumbUpOutlinedIcon fontSize="small" sx={{ fontSize: 16 }} />}
                        </IconButton>
                        <Typography variant="caption">{reply.likeCount || 0}</Typography>

                        <IconButton size="small" onClick={() => handleDislike(reply.id)} color={reply.userDisliked ? 'primary' : 'default'}>
                          {reply.userDisliked ? <ThumbDownIcon fontSize="small" sx={{ fontSize: 16 }} /> : <ThumbDownOutlinedIcon fontSize="small" sx={{ fontSize: 16 }} />}
                        </IconButton>
                        <Typography variant="caption">{reply.dislikeCount || 0}</Typography>
                        
                        <Button size="small" sx={{ ml: 1 }} onClick={() => { setReplyingTo(replyingTo === reply.id ? null : reply.id); setReplyMediaList([]); }}>
                          {replyingTo === reply.id ? 'Cancel' : 'Reply'}
                        </Button>
                      </Box>
                      <Typography variant="body2">{reply.content}</Typography>
                      <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mt: 1 }}>
                        {reply.attachmentList && reply.attachmentList.map((m, i) => (
                          <Box key={i}>
                            {m.mediaType === 'IMAGE' ? (
                              <img src={m.mediaUrl.startsWith('http') ? m.mediaUrl : (import.meta.env.VITE_API_BASE_URL || '/api') + m.mediaUrl} alt="reply" style={{ maxWidth: 150 }} />
                            ) : (
                              <video src={m.mediaUrl.startsWith('http') ? m.mediaUrl : (import.meta.env.VITE_API_BASE_URL || '/api') + m.mediaUrl} controls style={{ maxWidth: 200 }} />
                            )}
                          </Box>
                        ))}
                      </Box>
                      
                      <Collapse in={replyingTo === reply.id} timeout="auto" unmountOnExit sx={{ width: '100%', mt: 2 }}>
                        <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
                          <TextField 
                            fullWidth 
                            size="small" 
                            placeholder="Write a reply..." 
                            value={replyText}
                            onChange={e => setReplyText(e.target.value)}
                          />
                          <IconButton color="primary" component="label" size="small">
                            <input hidden accept="image/*,video/*" type="file" onChange={(e) => handleUpload(e, true)} />
                            <PhotoCamera fontSize="small" />
                          </IconButton>
                          <Button variant="contained" onClick={() => handleReplySubmit(reply.id)}>Reply</Button>
                        </Box>
                        {replyAttachmentList.map((_m, i) => (
                          <Typography key={i} variant="caption" sx={{ display: 'block', mt: 0.5 }}>Reply Media {i + 1} Attached</Typography>
                        ))}
                      </Collapse>
                    </ListItem>
                  ))}
                </List>
              )}
            </ListItem>
            <Divider variant="inset" component="li" />
          </React.Fragment>
        ))}
        {reviews.length === 0 && <Typography color="text.secondary">No reviews yet.</Typography>}
      </List>
    </Box>
  );
};
