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
  Divider,
} from '@mui/material';
import { resourceCategoryListAllAPI } from '@/apis/resourceCategory';
import { fetchAllResourceList } from '@/apis/resource';
import { roleListResourceById, roleAllocResourceAPI } from '@/apis/role';
import type { UmsResource, UmsResourceCategory } from '@/types/resource';

const AllocResource: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const roleId = Number(searchParams.get('roleId'));

  const [categories, setCategories] = useState<UmsResourceCategory[]>([]);
  const [resources, setResources] = useState<UmsResource[]>([]);
  const [selectedResourceIds, setSelectedResourceIds] = useState<number[]>([]);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' as 'success' | 'error' });

  useEffect(() => {
    if (!roleId) {
      navigate('/ums/role');
      return;
    }

    // Load categories, all resources, and allocated resources
    Promise.all([
      resourceCategoryListAllAPI(),
      fetchAllResourceList(),
      roleListResourceById(roleId)
    ]).then(([catRes, resRes, allocRes]) => {
      // @ts-ignore
      setCategories(catRes.data);
      // @ts-ignore
      setResources(resRes.data);
      
      // @ts-ignore
      const allocatedIds = allocRes.data.map((r: { id: number }) => r.id);
      setSelectedResourceIds(allocatedIds);
    }).catch(console.error);
  }, [roleId, navigate]);

  const handleToggleCategory = (categoryId: number) => {
    const categoryResources = resources.filter(r => r.categoryId === categoryId).map(r => r.id!);
    const allSelected = categoryResources.every(id => selectedResourceIds.includes(id));
    
    let newSelected = [...selectedResourceIds];
    if (allSelected) {
      // Unselect all in category
      newSelected = newSelected.filter(id => !categoryResources.includes(id));
    } else {
      // Select all in category
      newSelected.push(...categoryResources.filter(id => !newSelected.includes(id)));
    }
    setSelectedResourceIds(newSelected);
  };

  const handleToggleResource = (resourceId: number) => {
    setSelectedResourceIds(prev => 
      prev.includes(resourceId) ? prev.filter(id => id !== resourceId) : [...prev, resourceId]
    );
  };

  const handleSave = async () => {
    try {
      await roleAllocResourceAPI({
        roleId,
        resourceIds: selectedResourceIds.join(',')
      });
      setSnackbar({ open: true, message: 'Resources allocated successfully', severity: 'success' });
      setTimeout(() => navigate('/ums/role'), 1000);
    } catch {
      setSnackbar({ open: true, message: 'Failed to allocate resources', severity: 'error' });
    }
  };

  const handleSelectAll = () => {
    setSelectedResourceIds(resources.map(r => r.id!));
  };

  const handleClearAll = () => {
    setSelectedResourceIds([]);
  };

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto', mt: 4 }}>
      <Card>
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
            <Typography variant="h6">Assign Backend API Resources to Role</Typography>
            <Box>
              <Button size="small" onClick={handleSelectAll} sx={{ mr: 1 }}>Select All</Button>
              <Button size="small" color="error" onClick={handleClearAll}>Clear All</Button>
            </Box>
          </Box>
          
          <Divider sx={{ mb: 2 }} />

          <List disablePadding>
            {categories.map((category) => {
              const catResources = resources.filter(r => r.categoryId === category.id);
              if (catResources.length === 0) return null;
              
              const isAllSelected = catResources.length > 0 && catResources.every(r => selectedResourceIds.includes(r.id!));
              const isIndeterminate = !isAllSelected && catResources.some(r => selectedResourceIds.includes(r.id!));

              return (
                <Box key={category.id} sx={{ mb: 3 }}>
                  <ListItem sx={{ bgcolor: 'action.hover', borderRadius: 1, py: 0.5 }}>
                    <Checkbox
                      checked={isAllSelected}
                      indeterminate={isIndeterminate}
                      onChange={() => handleToggleCategory(category.id!)}
                      color="primary"
                    />
                    <ListItemText primary={category.name} primaryTypographyProps={{ fontWeight: 600 }} />
                  </ListItem>
                  
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, pt: 2, pl: 2 }}>
                    {catResources.map((resource) => (
                      <Box key={resource.id} sx={{ display: 'flex', alignItems: 'flex-start', minWidth: 250, maxWidth: 350 }}>
                        <Checkbox
                          size="small"
                          checked={selectedResourceIds.includes(resource.id!)}
                          onChange={() => handleToggleResource(resource.id!)}
                          sx={{ pt: 0.5 }}
                        />
                        <Box>
                          <Typography variant="body2" sx={{ fontWeight: 500 }}>{resource.name}</Typography>
                          <Typography variant="caption" color="text.secondary" sx={{ wordBreak: 'break-all' }}>
                            {resource.url}
                          </Typography>
                        </Box>
                      </Box>
                    ))}
                  </Box>
                </Box>
              );
            })}
            {categories.length === 0 && <Typography color="text.secondary">No resources available.</Typography>}
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

export default AllocResource;
