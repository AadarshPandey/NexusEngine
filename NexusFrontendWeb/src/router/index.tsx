import { createBrowserRouter } from 'react-router';
import Layout from '../layout';
import Home from '../views/Home';
import Login from '../views/Login';
import NotFound from '../views/NotFound';
import ProductDetail from '../views/ProductDetail';
import Cart from '../views/Cart';
import Checkout from '../views/Checkout';
import Profile from '../views/Profile';

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
        path: 'product/:id',
        element: <ProductDetail />,
      },
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
]);
