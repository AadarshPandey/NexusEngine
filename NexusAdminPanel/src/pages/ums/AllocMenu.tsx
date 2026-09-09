import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Snackbar,
  Alert,
  Checkbox,
  List,
  ListItem,
  ListItemText,
  Collapse,
  IconButton,
  Divider,
} from '@mui/material';
import { ExpandMore, ExpandLess } from '@mui/icons-material';
import { getMenuTreeListAPI } from '@/apis/menu';
import { roleListMenuByRoleIdAPI, roleAllocMenuAPI } from '@/apis/role';
import type { UmsMenuNode } from '@/types/menu';

const AllocMenu: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const roleId = Number(searchParams.get('roleId'));

  const [menuTree, setMenuTree] = useState<UmsMenuNode[]>([]);
  const [selectedMenuIds, setSelectedMenuIds] = useState<number[]>([]);
  const [expandedMenus, setExpandedMenus] = useState<number[]>([]);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  useEffect(() => {
    if (!roleId) {
      navigate('/ums/role');
      return;
    }
    
    // Fetch all menus in tree format
    getMenuTreeListAPI().then(res => {
      setMenuTree(res.data);
      // Auto expand root menus
      setExpandedMenus(res.data.map(m => m.id!));
    }).catch(console.error);

    // Fetch already allocated menus
    roleListMenuByRoleIdAPI(roleId).then(res => {
      setSelectedMenuIds(res.data.map(m => m.id!));
    }).catch(console.error);
  }, [roleId, navigate]);

  const toggleExpand = (id: number) => {
    setExpandedMenus(prev => 
      prev.includes(id) ? prev.filter(m => m !== id) : [...prev, id]
    );
  };

  const handleToggleMenu = (menu: UmsMenuNode) => {
    const isSelected = selectedMenuIds.includes(menu.id!);
    let newSelected = [...selectedMenuIds];

    if (isSelected) {
      // Unselect this menu
      newSelected = newSelected.filter(id => id !== menu.id);
      // Unselect all its children
      if (menu.children) {
        const childIds = menu.children.map(c => c.id!);
        newSelected = newSelected.filter(id => !childIds.includes(id));
      }
    } else {
      // Select this menu
      newSelected.push(menu.id!);
      // Select all its children
      if (menu.children) {
        newSelected.push(...menu.children.map(c => c.id!));
      }
      // Select its parent if not selected (this requires parentId which is on UmsMenu, if available)
      if (menu.parentId !== 0 && !newSelected.includes(menu.parentId!)) {
        newSelected.push(menu.parentId!);
      }
    }
    
    // De-duplicate
    setSelectedMenuIds(Array.from(new Set(newSelected)));
  };

  const handleToggleChild = (childId: number, parentId: number) => {
    const isSelected = selectedMenuIds.includes(childId);
    let newSelected = [...selectedMenuIds];

    if (isSelected) {
      newSelected = newSelected.filter(id => id !== childId);
    } else {
      newSelected.push(childId);
      // Auto select parent if a child is selected
      if (!newSelected.includes(parentId)) {
        newSelected.push(parentId);
      }
    }

    setSelectedMenuIds(Array.from(new Set(newSelected)));
  };

  const handleSave = async () => {
    try {
      await roleAllocMenuAPI({
        roleId,
        menuIds: selectedMenuIds.join(',')
      });
      setSnackbar({ open: true, message: 'Menus allocated successfully', severity: 'success' });
      setTimeout(() => navigate('/ums/role'), 1000);
    } catch {
      setSnackbar({ open: true, message: 'Failed to allocate menus', severity: 'error' });
    }
  };

  const handleSelectAll = () => {
    const allIds: number[] = [];
    menuTree.forEach(m => {
      allIds.push(m.id!);
      if (m.children) allIds.push(...m.children.map(c => c.id!));
    });
    setSelectedMenuIds(allIds);
  };

  const handleClearAll = () => {
    setSelectedMenuIds([]);
  };

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', mt: 4 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
            <Typography variant="h6">Assign UI Menus to Role</Typography>
            <Box>
              <Button size="small" onClick={handleSelectAll} sx={{ mr: 1 }}>Select All</Button>
              <Button size="small" color="error" onClick={handleClearAll}>Clear All</Button>
            </Box>
          </Box>
          
          <Divider sx={{ mb: 2 }} />

          <List disablePadding>
            {menuTree.map((menu) => (
              <React.Fragment key={menu.id}>
                <ListItem sx={{ bgcolor: 'action.hover', borderRadius: 1, mb: 1 }}>
                  <Checkbox
                    checked={selectedMenuIds.includes(menu.id!)}
                    onChange={() => handleToggleMenu(menu)}
                    color="primary"
                  />
                  <ListItemText 
                    primary={menu.title} 
                    secondary={menu.name} 
                    primaryTypographyProps={{ fontWeight: 600 }}
                  />
                  {menu.children && menu.children.length > 0 && (
                    <IconButton onClick={() => toggleExpand(menu.id!)}>
                      {expandedMenus.includes(menu.id!) ? <ExpandLess /> : <ExpandMore />}
                    </IconButton>
                  )}
                </ListItem>
                
                {menu.children && menu.children.length > 0 && (
                  <Collapse in={expandedMenus.includes(menu.id!)} timeout="auto" unmountOnExit>
                    <List component="div" disablePadding sx={{ pl: 4, mb: 2 }}>
                      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, pt: 1 }}>
                        {menu.children.map((child) => (
                          <Box key={child.id} sx={{ display: 'flex', alignItems: 'center', minWidth: 200 }}>
                            <Checkbox
                              size="small"
                              checked={selectedMenuIds.includes(child.id!)}
                              onChange={() => handleToggleChild(child.id!, menu.id!)}
                            />
                            <Typography variant="body2">{child.title}</Typography>
                          </Box>
                        ))}
                      </Box>
                    </List>
                  </Collapse>
                )}
              </React.Fragment>
            ))}
            {menuTree.length === 0 && <Typography color="text.secondary">No menus available.</Typography>}
          </List>

          <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 4 }}>
            <Button variant="outlined" onClick={() => navigate('/ums/role')}>Cancel</Button>
            <Button variant="contained" onClick={handleSave}>Save Allocations</Button>
          </Box>
        </CardContent>
      </Card>

      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar((s) => ({ ...s, open: false }))} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert severity={snackbar.severity} onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>{snackbar.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default AllocMenu;
