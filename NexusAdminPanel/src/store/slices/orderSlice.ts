import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { CustomerOrder, OmsOrder } from '@/types/ordering';

interface OrderState {
  deliverOrderList: CustomerOrder[];
  currentOrder: CustomerOrder | null;
}

const initialState: OrderState = {
  deliverOrderList: [],
  currentOrder: null,
};

const orderSlice = createSlice({
  name: 'order',
  initialState,
  reducers: {
    setDeliverOrderList(state, action: PayloadAction<CustomerOrder[]>) {
      state.deliverOrderList = action.payload;
    },
    setCurrentOrder(state, action: PayloadAction<CustomerOrder | null>) {
      state.currentOrder = action.payload;
    },
    clearCurrentOrder(state) {
      state.currentOrder = null;
    },
  },
});

export const { setDeliverOrderList, setCurrentOrder, clearCurrentOrder } = orderSlice.actions;
export default orderSlice.reducer;
