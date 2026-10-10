sed -i 's/orderLineId/productId/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/body: string;/content: string;/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/body:/content:/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/review.body/review.content/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/reply.body/reply.content/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/createdAt: string;/createdTime: string;/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/review.createdAt/review.createdTime/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/reply.createdAt/reply.createdTime/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/helpfulCount/likeCount/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/notHelpfulCount/dislikeCount/g' NexusFrontendWeb/src/components/ReviewSection.tsx
