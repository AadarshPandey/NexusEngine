import { createBrowserRouter } from 'react-router';
import Layout from '../layout';
import Home from '../views/Home';
import Login from '../views/Login';
import Register from '../views/Register';
import NotFound from '../views/NotFound';
import ProductDetail from '../views/ProductDetail';
import Cart from '../views/Cart';
import Checkout from '../views/Checkout';
import Profile from '../views/Profile';
import ProtectedRoute from '../components/ProtectedRoute';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    errorElement: <NotFound />,
    children: [
      {
        index: true,
        element: <Home />,
      },
      {
        path: 'login',
        element: <Login />,
      },
      {
        path: 'register',
        element: <Register />,
      },
      {
        path: 'product/:id',
        element: <ProductDetail />,
      },
      {
        element: <ProtectedRoute />,
        children: [
          {
            path: 'cart',
            element: <Cart />,
          },
          {
            path: 'checkout',
            element: <Checkout />,
          },
          {
            path: 'profile',
            element: <Profile />,
          },
        ],
      },
    ],
  },
]);
