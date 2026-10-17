/**
 * AI Context API Client
 * Aligned with ADR 0003 and modular monolith domain model
 */

import request from '../utils/request';

export interface ChatMessageDto {
  id?: number;
  role: 'USER' | 'ASSISTANT' | 'SYSTEM' | 'TOOL';
  content?: string;
  body?: string;
}

export const getChatHistory = (sessionId: string) => {
  return request.get<unknown, ChatMessageDto[]>('/ai/chat/history', {
    params: { slotId: sessionId },
  });
};

export const sendChatMessage = (sessionId: string, question: string) => {
  return request.post<unknown, { question: string; answer: string; sessionId: string }>('/ai/chat', {
    question,
    sessionId,
  });
};
