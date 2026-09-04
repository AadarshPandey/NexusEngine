import { createTheme, alpha } from '@mui/material/styles';

// Premium e-commerce admin color palette
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
    mode: 'light',
    ...palette,
    background: {
      default: '#F8FAFC',
      paper: '#FFFFFF',
    },
    text: {
      primary: '#1E293B',
      secondary: '#64748B',
    },
    divider: '#E2E8F0',
  },
  typography: {
    fontFamily: '"Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
    h1: { fontWeight: 700, fontSize: '2rem', lineHeight: 1.2 },
    h2: { fontWeight: 700, fontSize: '1.5rem', lineHeight: 1.3 },
    h3: { fontWeight: 600, fontSize: '1.25rem', lineHeight: 1.4 },
    h4: { fontWeight: 600, fontSize: '1.125rem', lineHeight: 1.4 },
    h5: { fontWeight: 600, fontSize: '1rem', lineHeight: 1.5 },
    h6: { fontWeight: 600, fontSize: '0.875rem', lineHeight: 1.5 },
    subtitle1: { fontWeight: 500, fontSize: '0.875rem', color: '#64748B' },
    body1: { fontSize: '0.875rem', lineHeight: 1.6 },
    body2: { fontSize: '0.8125rem', lineHeight: 1.6 },
    button: { fontWeight: 600, textTransform: 'none' as const },
  },
  shape: {
    borderRadius: 12,
  },
  shadows: [
    'none',
    '0 1px 2px 0 rgb(0 0 0 / 0.05)',
    '0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1)',
    '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
    '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
    '0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1)',
    '0 25px 50px -12px rgb(0 0 0 / 0.25)',
    ...Array(18).fill('none'),
  ] as any,
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
        },
        containedPrimary: {
          background: 'linear-gradient(135deg, #6366F1 0%, #818CF8 100%)',
          '&:hover': {
            background: 'linear-gradient(135deg, #4F46E5 0%, #6366F1 100%)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 16,
          boxShadow: '0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1)',
          border: '1px solid #F1F5F9',
          '&:hover': {
            boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
          },
          transition: 'box-shadow 0.2s ease-in-out',
        },
      },
    },
    MuiTableHead: {
      styleOverrides: {
        root: {
          '& .MuiTableCell-head': {
            backgroundColor: '#F8FAFC',
            fontWeight: 600,
            color: '#475569',
            fontSize: '0.8125rem',
            borderBottom: '2px solid #E2E8F0',
          },
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          fontSize: '0.8125rem',
          padding: '12px 16px',
          borderBottom: '1px solid #F1F5F9',
        },
      },
    },
    MuiTableRow: {
      styleOverrides: {
        root: {
          '&:hover': {
            backgroundColor: alpha('#6366F1', 0.04),
          },
          transition: 'background-color 0.15s ease',
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          fontWeight: 500,
          fontSize: '0.75rem',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 10,
            '& fieldset': {
              borderColor: '#E2E8F0',
            },
            '&:hover fieldset': {
              borderColor: '#6366F1',
            },
          },
        },
      },
    },
    MuiDrawer: {
      styleOverrides: {
        paper: {
          borderRight: 'none',
          boxShadow: '2px 0 8px rgba(0, 0, 0, 0.06)',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#FFFFFF',
          color: '#1E293B',
          boxShadow: '0 1px 3px 0 rgb(0 0 0 / 0.05)',
        },
      },
    },
    MuiDialog: {
      styleOverrides: {
        paper: {
          borderRadius: 16,
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
  },
});

export default theme;
