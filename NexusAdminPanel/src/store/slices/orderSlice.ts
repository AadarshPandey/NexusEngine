import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { OmsOrder } from '@/types/order';

interface OrderState {
  deliverOrderList: OmsOrder[];
}

const initialState: OrderState = {
  deliverOrderList: [],
};

const orderSlice = createSlice({
  name: 'order',
  initialState,
  reducers: {
    setDeliverOrderList(state, action: PayloadAction<OmsOrder[]>) {
      state.deliverOrderList = action.payload;
    },
  },
});

export const { setDeliverOrderList } = orderSlice.actions;
export default orderSlice.reducer;
