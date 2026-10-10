sed -i 's/m.url.startsWith/m.mediaUrl.startsWith/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/: m.url/: m.mediaUrl/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/+ m.url/+ m.mediaUrl/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/url: res.data.url/mediaUrl: res.data.url/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/displayOrder: isReply/sortOrder: isReply/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/  url: string;/  mediaUrl: string;/g' NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i 's/  displayOrder: number;/  sortOrder: number;/g' NexusFrontendWeb/src/components/ReviewSection.tsx
