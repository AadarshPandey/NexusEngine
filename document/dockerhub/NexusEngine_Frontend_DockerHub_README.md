# NexusEngine - Frontend Applications

![React](https://img.shields.io/badge/React-19-61dafb.svg?logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5-3178c6.svg?logo=typescript)
![Vite](https://img.shields.io/badge/Vite-5-646cff.svg?logo=vite)
![MUI](https://img.shields.io/badge/MUI-5-007FFF.svg?logo=mui)

This repository contains the containerized front-end **React 19** applications for **NexusEngine**, a high-performance, enterprise-grade e-commerce platform.

## 🔗 Official Repository
The full source code, extensive architecture documentation, and issue tracker can be found on GitHub:  
**[https://github.com/AadarshPandey/NexusEngine](https://github.com/AadarshPandey/NexusEngine)**

---

## 🌟 Included Applications

This Docker repository actually encompasses two distinct Single Page Applications (SPAs) built with React 19, depending on which image tag you run:

### 1. Customer Storefront (`nexus-engine-frontend-store`)
A highly responsive, premium-designed e-commerce storefront for customers. 
- **Features:** A complete shopping cart system, dynamic product catalogs, AI-powered semantic search, category filtering, and a secure mock checkout flow powered by Razorpay.
- **Design:** Built using modern aesthetics including glassmorphism, dynamic micro-animations, and vibrant gradients.

### 2. Administrator ERP Dashboard (`nexus-engine-frontend-admin`)
A comprehensive back-office ERP interface for store administrators.
- **Features:** High-performance dynamic data tables mapping directly to the PostgreSQL database.
- **Modules:** Complete management of Users (RBAC), Product Catalog, Orders, Flash Sale Sessions, Return Applications, and Coupon distribution.

## 🛠️ Technology Stack

Both applications share a modern, unified tech stack:
- **Framework:** React 19 + TypeScript
- **Tooling:** Vite (Next-generation build tool)
- **UI Library:** Material UI (MUI) v5
- **State Management:** Redux Toolkit
- **Routing:** React Router v7
- **HTTP Client:** Axios with JWT Interceptors
- **Data Visualization:** Recharts
- **Container Server:** Nginx Alpine (Custom configured to proxy `/api` calls directly to the Spring Boot backend)

## 🚀 Quick Start (Docker Compose)

The easiest way to run the applications is via Docker Compose alongside the required backend services.

Download the `docker-compose.yml` from the [GitHub Repository](https://github.com/AadarshPandey/NexusEngine) and simply run:

```bash
docker compose up -d
```

### 🌍 Accessing the Applications
- **Admin Dashboard**: `http://localhost:5173` (Login: `admin` / `macro123`)
- **Customer Storefront**: `http://localhost:5174` (Login: `customer1` / `macro123`)

## 🤝 Open Source Acknowledgements
This project was highly inspired by the incredible open-source architecture of the `macrozheng/mall` project, heavily adapted and modernized for this specific deployment.
