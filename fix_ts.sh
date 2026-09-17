# 1. BannerCarousel.tsx
sed -i 's/fontWeight: "bold"/sx={{ fontWeight: "bold" }}/g' ./NexusFrontendWeb/src/components/BannerCarousel.tsx

# 2. ReviewSection.tsx unused vars
sed -i '/const \[loading, setLoading\] = useState(false);/d' ./NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i '/const \[uploading, setUploading\] = useState(false);/d' ./NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i '/setLoading/d' ./NexusFrontendWeb/src/components/ReviewSection.tsx
sed -i '/setUploading/d' ./NexusFrontendWeb/src/components/ReviewSection.tsx

# 3. cartSlice.test.ts import error
sed -i 's/..\/..\/api\/cart/..\/api\/order/g' ./NexusFrontendWeb/src/tests/cartSlice.test.ts

# 4. Checkout.tsx unknown data
sed -i 's/orderRes.data/((orderRes as any).data)/g' ./NexusFrontendWeb/src/views/Checkout.tsx

# 5. Home.tsx button import
sed -i 's/, Button//g' ./NexusFrontendWeb/src/views/Home.tsx

# 6. ProductDetail.tsx Stack flexWrap
sed -i 's/flexWrap="wrap"/sx={{ flexWrap: "wrap" }}/g' ./NexusFrontendWeb/src/views/ProductDetail.tsx

