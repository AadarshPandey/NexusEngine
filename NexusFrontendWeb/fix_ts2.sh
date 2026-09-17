# 1. BannerCarousel.tsx
sed -i 's/fontWeight="bold"/sx={{ fontWeight: "bold" }}/g' ./src/components/BannerCarousel.tsx

# 2. cartSlice.test.ts
sed -i "s/import('..\/api\/order')/import('..\/api\/cart')/g" ./src/tests/cartSlice.test.ts
sed -i "s/..\/..\/api\/cart/..\/api\/cart/g" ./src/tests/cartSlice.test.ts

