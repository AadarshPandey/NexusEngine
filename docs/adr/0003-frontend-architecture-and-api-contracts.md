# ADR 0003: Frontend Architecture, Canonical Domain Contracts, & Media Handling

- **Status:** Accepted
- **Date:** 2026-10-11
- **Deciders:** NexusEngine Engineering Team

---

## 1. Context and Problem Statement

The user interfaces (`NexusAdminPanel` and `NexusFrontendWeb`) accumulated significant technical debt and architectural drift:

1. **Legacy Prefix Jargon:** API client modules, routes, and state management reflected legacy prefixes (`/pms/*`, `/oms/*`, `/sms/*`, `/ums/*`) inherited from the mall template.
2. **Dual-World Naming & Fallback Chains:** Frontend types carried duplicate property aliases (`pic` alongside `primaryImageUrl`, `sale` alongside `unitsSold`, `stock` alongside `onHandQty`, `productSn` alongside `productCode`). Components contained brittle fallback ladders (`item.primaryImageUrl || item.pic || item.imageUrl`).
3. **Integer-as-Boolean Anti-Pattern:** Flags were represented as integers (`0` and `1`), forcing UI components to perform numeric evaluations (`if (status === 1)` or `item.publishStatus === 1`), obscuring boolean logic.
4. **Cart Snapshot Anti-Pattern:** The shopping cart replicated mutable catalog fields (`productPic`, `productName`, `productBrand`, `productSn`, `productSkuCode`, `productAttr`) directly inside cart state. If an administrator updated prices or descriptions, the user's cart retained stale, inaccurate snapshots.
5. **Storage SDK Inconsistency:** Code referenced legacy MinIO and Aliyun OSS endpoints (`/admin/oss/upload`, `minio-data`, `OssPolicy`) while infrastructure used AWS S3 / LocalStack. Furthermore, migration plans specified `nexus-bucket` while production infrastructure provisioned `nexus-media`.

---

## 2. Decision Drivers

- **Canonical Naming End-to-End:** Enforce exactly one name per concept across DB column, Java entity, REST DTO, TypeScript type, and React component props (zero aliases, zero fallback chains).
- **Type Safety & Native Primitives:** Use native TypeScript `boolean` types and ISO-8601 UTC strings.
- **Single Source of Truth:** Shopping cart must always reflect real-time catalog pricing and availability.
- **Industry Standard S3 Storage:** Standardize on AWS S3 / LocalStack with canonical bucket name `nexus-media`.

---

## 3. Decision Outcome

### 3.1 Bounded-Context Frontend Organization

Both applications (`NexusAdminPanel` and `NexusFrontendWeb`) structure API services, hooks, and types by bounded contexts:

```
src/
├── api/
│   ├── catalog/             <-- Products, SKUs, Categories, Brands
│   ├── ordering/            <-- Cart, Checkout, Orders, Returns
│   ├── customer/            <-- Profile, Addresses, Auth
│   ├── iam/                 <-- Admin RBAC, Users, Roles
│   ├── promotion/           <-- Coupons, Flash Sales, Banners
│   ├── engagement/          <-- Reviews, Ratings
│   ├── payment/             <-- Razorpay payment flows & webhooks
│   └── ai/                  <-- Assistant chat, search suggestions
├── types/                   <-- Canonical TypeScript interfaces (*.d.ts)
└── utils/
    ├── http.ts              <-- Axios instance with standard error handling
    └── media.ts             <-- S3 URL resolution helper
```

### 3.2 Canonical Contract Standards

| Concept | Legacy (Anti-Pattern) | Canonical Modern Standard |
|---|---|---|
| **Product Image** | `pic`, `imgUrl`, `picture` | `primaryImageUrl: string` |
| **Product Identifier** | `productSn`, `sn` | `productCode: string` |
| **Variant Identifier** | `skuCode`, `skuSn` | `skuCode: string` |
| **Inventory Count** | `stock` (ambiguous) | `onHandQty: number`, `availableQty: number` |
| **Sales Count** | `sale` (misleading) | `unitsSold: number` |
| **Monetary Amounts** | `totalAmount`, `payAmount` | `totalAmount: number`, `paidAmount: number` |
| **Booleans** | `publishStatus: 0 \| 1` | `isPublished: boolean` |
| **Timestamps** | `createTime`, `createDate` | `createdAt: string` (ISO-8601 UTC) |

### 3.3 Live Catalog Shopping Cart (Eliminating Snapshots)

To eradicate the Cart Snapshot anti-pattern:
1. `ordering.cart_item` state stores only identity and desired quantity: `{ id, skuId, quantity }`.
2. When the cart page or drawer loads, the client calls `GET /api/store/cart`, which returns items enriched dynamically with live SKU details from the `catalog` context (live unit price, sale price, stock availability, title, variant attributes).
3. If an item is out of stock or changed price, the UI clearly displays real-time state with appropriate warnings rather than executing checkout with stale data.

### 3.4 Storage & Media Handling

1. **Bucket:** Standardized to **`nexus-media`** across all environments (LocalStack S3 in development/testing, AWS S3 in production).
2. **Public Prefix:** Public catalog assets are stored under `public/catalog/`, `public/marketing/`, and `public/users/`.
3. **Upload Workflow:**
   - Admin uploads request a presigned S3 upload URL via `POST /api/admin/media/presigned-url`.
   - The frontend uploads the asset directly to S3 via standard HTTP `PUT`.
   - The returned canonical URL is saved to the entity.

---

## 4. Consequences

### Positive
- Elimination of confusing `a || b || c` fallback properties across React components.
- Strict TypeScript compiler guarantees catching missing or misspelled fields.
- Guaranteed up-to-date cart prices and stock verification before checkout initiation.
- Clean S3 integration aligned with standard AWS infrastructure patterns.

### Trade-Offs
- Cart fetching requires a backend join/enrichment step on read, which is mitigated via high-speed caching for catalog lookups.
