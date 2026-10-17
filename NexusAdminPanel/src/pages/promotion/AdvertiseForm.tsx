import React from 'react';
import { useParams, useSearchParams } from 'react-router';
import AdvertiseDetail from './components/AdvertiseDetail';

const AdvertiseForm: React.FC = () => {
  const { id: paramId } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const id = paramId || searchParams.get('id');

  return <AdvertiseDetail isEdit={Boolean(id)} />;
};

export default AdvertiseForm;
