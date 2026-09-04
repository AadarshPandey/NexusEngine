import React from 'react';
import { Navigate } from 'react-router';
import { useAppSelector, useAppDispatch } from '@/store/hooks';
import { generateRoutes } from '@/store/slices/permissionSlice';
import { fetchUserInfo } from '@/store/slices/userSlice';

interface AuthGuardProps {
  children: React.ReactNode;
}

const AuthGuard: React.FC<AuthGuardProps> = ({ children }) => {
  const token = useAppSelector((state) => state.user.userInfo.token);
  const menus = useAppSelector((state) => state.user.userInfo.menus);
  const username = useAppSelector((state) => state.user.userInfo.username);
  const routers = useAppSelector((state) => state.permission.routers);
  const dispatch = useAppDispatch();

  React.useEffect(() => {
    if (token && routers.length === 0) {
      if (!menus || menus.length === 0) {
        // If menus are empty (maybe outdated cache), fetch fresh user info then generate
        dispatch(fetchUserInfo()).unwrap().then((data) => {
          dispatch(generateRoutes({ menus: data.menus, username: data.username }));
        }).catch(() => {
          dispatch(generateRoutes({ menus: [], username }));
        });
      } else {
        dispatch(generateRoutes({ menus, username }));
      }
    }
  }, [token, menus, username, routers.length, dispatch]);

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

export default AuthGuard;
