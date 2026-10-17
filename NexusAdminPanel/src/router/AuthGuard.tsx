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
  const roles = useAppSelector((state) => state.user.userInfo.roles);
  const username = useAppSelector((state) => state.user.userInfo.username);
  const routers = useAppSelector((state) => state.permission.routers);
  const dispatch = useAppDispatch();

  React.useEffect(() => {
    if (token && routers.length === 0) {
      if (!roles || roles.length === 0) {
        dispatch(fetchUserInfo()).unwrap().then((data) => {
          dispatch(generateRoutes({ roles: data.roles || [], username: data.username }));
        }).catch(() => {
          dispatch(generateRoutes({ roles: [], username }));
        });
      } else {
        dispatch(generateRoutes({ roles, username }));
      }
    }
  }, [token, roles, username, routers.length, dispatch]);

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

export default AuthGuard;
