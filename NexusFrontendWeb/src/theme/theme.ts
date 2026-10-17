import { createTheme, alpha } from '@mui/material/styles';

// Premium e-commerce color palette matching NexusAdminPanel
const palette = {
  primary: {
    main: '#6366F1',     // Indigo
    light: '#818CF8',
    dark: '#4F46E5',
    contrastText: '#FFFFFF',
  },
  secondary: {
    main: '#EC4899',     // Pink
    light: '#F472B6',
    dark: '#DB2777',
    contrastText: '#FFFFFF',
  },
  success: {
    main: '#10B981',     // Emerald
    light: '#34D399',
    dark: '#059669',
  },
  warning: {
    main: '#F59E0B',     // Amber
    light: '#FBBF24',
    dark: '#D97706',
  },
  error: {
    main: '#EF4444',     // Red
    light: '#F87171',
    dark: '#DC2626',
  },
  info: {
    main: '#3B82F6',     // Blue
    light: '#60A5FA',
    dark: '#2563EB',
  },
};

const theme = createTheme({
  palette: {
    mode: 'dark',
    ...palette,
    background: {
      default: '#0f172a',
      paper: '#1e293b',
    },
    text: {
      primary: '#f8fafc',
      secondary: '#94a3b8',
    },
    divider: '#334155',
  },
  typography: {
    fontFamily: '"Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
    h1: { fontWeight: 700, fontSize: '2rem', lineHeight: 1.2 },
    h2: { fontWeight: 700, fontSize: '1.5rem', lineHeight: 1.3 },
    h3: { fontWeight: 600, fontSize: '1.25rem', lineHeight: 1.4 },
    h4: { fontWeight: 600, fontSize: '1.125rem', lineHeight: 1.4 },
    h5: { fontWeight: 600, fontSize: '1rem', lineHeight: 1.5 },
    h6: { fontWeight: 600, fontSize: '0.875rem', lineHeight: 1.5 },
    subtitle1: { fontWeight: 500, fontSize: '0.875rem', color: '#94a3b8' },
    body1: { fontSize: '0.875rem', lineHeight: 1.6 },
    body2: { fontSize: '0.8125rem', lineHeight: 1.6 },
    button: { fontWeight: 600, textTransform: 'none' as const },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 10,
          padding: '8px 20px',
          fontSize: '0.8125rem',
          boxShadow: 'none',
          '&:hover': {
            boxShadow: '0 4px 12px rgba(99, 102, 241, 0.3)',
          },
          '&.MuiButton-containedPrimary': {
            background: 'linear-gradient(135deg, #6366F1 0%, #818CF8 100%)',
            '&:hover': {
              background: 'linear-gradient(135deg, #4F46E5 0%, #6366F1 100%)',
            },
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 16,
          boxShadow: '0 1px 3px 0 rgb(0 0 0 / 0.5), 0 1px 2px -1px rgb(0 0 0 / 0.5)',
          border: '1px solid #334155',
          backgroundImage: 'none',
          '&:hover': {
            boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.5), 0 2px 4px -2px rgb(0 0 0 / 0.5)',
          },
          transition: 'box-shadow 0.2s ease-in-out',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#1e293b',
          color: '#f8fafc',
          boxShadow: '0 1px 3px 0 rgb(0 0 0 / 0.5)',
          borderBottom: '1px solid #334155',
          backgroundImage: 'none',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 10,
            '& fieldset': {
              borderColor: '#334155',
            },
            '&:hover fieldset': {
              borderColor: '#6366F1',
            },
          },
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          borderBottom: '1px solid #334155',
        },
      },
    },
    MuiTableRow: {
      styleOverrides: {
        root: {
          '&:hover': {
            backgroundColor: alpha('#6366F1', 0.08),
          },
          transition: 'background-color 0.15s ease',
        },
      },
    },
  },
});

export default theme;
