# NexusAdminPanel

> Admin panel for the NexusCore e-commerce platform, built with React + Material UI + Redux Toolkit.

## Tech Stack

| Technology | Description | Official Site |
| --- | --- | --- |
| React 19 | Front-end framework | https://react.dev/ |
| React Router 7 | Routing framework | https://reactrouter.com/ |
| Redux Toolkit | Global state management | https://redux-toolkit.js.org/ |
| Material UI 7 | Desktop UI component library | https://mui.com/ |
| Axios | HTTP client | https://github.com/axios/axios |
| Recharts | Charting library | https://recharts.org/ |
| Js-cookie | Cookie management tool | https://github.com/js-cookie/js-cookie |
| react-top-loading-bar | Progress bar widget | https://github.com/klendi/react-top-loading-bar |
| TinyMCE React | Rich text editor | https://github.com/tinymce/tinymce-vue |
| Vite 7 | Build tool and dev server | https://vite.dev/ |

## Project Layout

```
src/
├── apis/           -- Axios HTTP API client modules (29 modules)
├── assets/         -- Static image resources
├── components/     -- Shared reusable components
├── layouts/        -- Admin layout shell (sidebar, navbar)
├── pages/          -- All page components
│   ├── pms/        -- Product management (13 pages)
│   ├── oms/        -- Order management (7 pages)
│   ├── sms/        -- Marketing management (14 pages)
│   └── ums/        -- Access control (9 pages)
├── router/         -- React Router config and auth guard
├── store/          -- Redux Toolkit store and slices
├── styles/         -- Global CSS styles
├── theme/          -- MUI custom theme
├── types/          -- TypeScript type definitions
└── utils/          -- Utility functions (HTTP, cookie, datetime)
```

## Setup

1. Ensure Node.js v20+ is installed;
2. The backend (NexusCore) must be running for API access — see the [NexusCore README](../README.md);
3. Update `.env.development` with the backend API URL if needed:
   ```
   VITE_BASE_SERVER_URL = http://localhost:8080
   ```

## Running

```bash
# Install dependencies
npm install

# Start dev server
npm run dev

# Build for production
npm run build
```

Access: http://localhost:5173

## License

[Apache License 2.0](LICENSE)

Copyright (c) 2018-2026 macrozheng
