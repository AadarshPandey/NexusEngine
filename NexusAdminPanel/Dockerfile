FROM node:22-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ENV VITE_BASE_SERVER_URL=http://localhost:8080
RUN npm run build
EXPOSE 5173
CMD ["npx", "vite", "preview", "--host", "0.0.0.0", "--port", "5173"]

