FROM node:22-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ENV VITE_API_BASE_URL=http://localhost:8080/api/store
RUN npm run build
EXPOSE 5174
CMD ["npx", "vite", "preview", "--host", "0.0.0.0", "--port", "5174"]

