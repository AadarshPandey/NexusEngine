import React, { useEffect, useState } from 'react';
import { Box, Typography, Button, TextField, Rating, List, ListItem, Divider, Avatar, IconButton } from '@mui/material';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import request from '../utils/request';

interface MediaParam {
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
  mediaList?: MediaParam[];
  replies?: Review[];
}

export const ReviewSection: React.FC<{ productId: number }> = ({ productId }) => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [newReviewText, setNewReviewText] = useState('');
  const [rating, setRating] = useState<number | null>(5);
  const [mediaList, setMediaList] = useState<MediaParam[]>([]);
  
  useEffect(() => {
    loadReviews();
  }, [productId]);

  const loadReviews = async () => {
    try {
      const res = await request.get<any, { data: { list: Review[] } }>(`/review/product/${productId}`);
      setReviews(res.data.list);
    } catch (e) {
      console.error(e);
    }
  };

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      const formData = new FormData();
      formData.append('file', file);
      try {
        const res = await request.post<any, { data: { url: string } }>('/review/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        setMediaList([...mediaList, { mediaType: file.type.startsWith('video/') ? 'VIDEO' : 'IMAGE', mediaUrl: res.data.url, sortOrder: mediaList.length }]);
      } catch (err) {
        alert('Upload failed');
      }
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
        mediaList
      });
      setNewReviewText('');
      setRating(5);
      setMediaList([]);
      loadReviews();
    } catch (e) {
      alert('Failed to post review. Please login.');
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
            <input hidden accept="image/*,video/*" type="file" onChange={handleUpload} />
            <PhotoCamera />
          </IconButton>
          {mediaList.map((m, i) => (
            <Typography key={i} variant="caption">Uploaded Media {i + 1}</Typography>
          ))}
        </Box>
      </Box>

      <List>
        {reviews.map(review => (
          <React.Fragment key={review.id}>
            <ListItem alignItems="flex-start" sx={{ flexDirection: 'column' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 1 }}>
                <Avatar sx={{ width: 32, height: 32 }}>U</Avatar>
                <Rating value={review.rating} readOnly size="small" />
                <Typography variant="caption" color="text.secondary">
                  {new Date(review.createdTime).toLocaleDateString()}
                </Typography>
              </Box>
              <Typography variant="body1">{review.content}</Typography>
              {review.mediaList && review.mediaList.map((m, i) => (
                <Box key={i} sx={{ mt: 1 }}>
                  {m.mediaType === 'IMAGE' ? (
                    <img src={(import.meta.env.VITE_API_BASE_URL || '/api') + m.mediaUrl} alt="review" style={{ maxWidth: 200 }} />
                  ) : (
                    <video src={(import.meta.env.VITE_API_BASE_URL || '/api') + m.mediaUrl} controls style={{ maxWidth: 300 }} />
                  )}
                </Box>
              ))}
            </ListItem>
            <Divider variant="inset" component="li" />
          </React.Fragment>
        ))}
        {reviews.length === 0 && <Typography color="text.secondary">No reviews yet.</Typography>}
      </List>
    </Box>
  );
};
