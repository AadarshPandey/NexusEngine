import React, { useState, useEffect, useRef } from 'react';
import { 
  Fab, Paper, Box, Typography, TextField, IconButton, 
  List, ListItem, CircularProgress
} from '@mui/material';
import ChatIcon from '@mui/icons-material/Chat';
import CloseIcon from '@mui/icons-material/Close';
import SendIcon from '@mui/icons-material/Send';
import axios from 'axios';

// Interfaces
interface ChatMessage {
  id?: number;
  role: 'USER' | 'ASSISTANT';
  body: string;
}

const generateSessionId = () => {
  const existing = localStorage.getItem('nexus_chat_session_id');
  if (existing) return existing;
  const newId = 'session_' + Math.random().toString(36).substring(2, 15);
  localStorage.setItem('nexus_chat_session_id', newId);
  return newId;
};

const ChatWidget: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [slotId, setSessionId] = useState('');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const sId = generateSessionId();
    setSessionId(sId);
    // Load history when component mounts
    axios.get(`/api/portal/chat/history?slotId=${sId}`)
      .then(res => {
        if (res.data.code === 200 && res.data.data) {
           const history = res.data.data.map((item: ChatMessage) => ({
             role: item.role,
             body: item.body
           }));
           if(history.length === 0) {
              setMessages([{ role: 'ASSISTANT', body: 'Hi there! I am Nexus AI. How can I help you today?' }]);
           } else {
              setMessages(history);
           }
        }
      })
      .catch(err => {
        console.error("Failed to load chat history", err);
        setMessages([{ role: 'ASSISTANT', body: 'Hi! How can I help you?' }]);
      });
  }, []);

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [messages, isOpen]);

  const handleSend = async () => {
    if (!input.trim()) return;
    
    const userMessage = input.trim();
    setInput('');
    setMessages(prev => [...prev, { role: 'USER', body: userMessage }]);
    setIsLoading(true);

    try {
      const response = await axios.post('/api/portal/chat', {
        question: userMessage,
        slotId: slotId
      });
      
      if (response.data.code === 200) {
        setMessages(prev => [...prev, { role: 'ASSISTANT', body: response.data.data.answer }]);
      }
    } catch (error) {
      setMessages(prev => [...prev, { role: 'ASSISTANT', body: 'Sorry, I am having trouble connecting right now.' }]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Fab 
        color="primary" 
        aria-label="chat" 
        onClick={() => setIsOpen(!isOpen)}
        sx={{ position: 'fixed', bottom: 24, right: 24, zIndex: 1000 }}
      >
        {isOpen ? <CloseIcon /> : <ChatIcon />}
      </Fab>

      {isOpen && (
        <Paper 
          elevation={6}
          sx={{
            position: 'fixed',
            bottom: 90,
            right: 24,
            width: 350,
            height: 500,
            display: 'flex',
            flexDirection: 'column',
            zIndex: 1000,
            borderRadius: 2,
            overflow: 'hidden',
            bgcolor: 'background.paper'
          }}
        >
          {/* Header */}
          <Box sx={{ bgcolor: 'primary.main', color: 'background.default', p: 2, display: 'flex', alignItems: 'center' }}>
            <ChatIcon sx={{ mr: 1 }} />
            <Typography variant="h6" sx={{ fontWeight: 'bold' }}>Nexus AI</Typography>
          </Box>

          {/* Chat Messages Area */}
          <Box sx={{ flex: 1, overflowY: 'auto', p: 2, bgcolor: 'background.default' }}>
            <List>
              {messages.map((msg, index) => {
                const isUser = msg.role === 'USER';
                return (
                  <ListItem 
                    key={index} 
                    sx={{ 
                      display: 'flex', 
                      justifyContent: isUser ? 'flex-end' : 'flex-start',
                      mb: 1,
                      px: 0
                    }}
                  >
                    <Box 
                      sx={{ 
                        maxWidth: '85%',
                        p: 1.5,
                        borderRadius: 2,
                        bgcolor: isUser ? 'primary.main' : 'background.paper',
                        color: isUser ? '#000' : 'text.primary',
                        border: '1px solid',
                        borderColor: isUser ? 'primary.dark' : 'secondary.main',
                        boxShadow: 1
                      }}
                    >
                      <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap', wordBreak: 'break-word' }}>
                        {msg.body}
                      </Typography>
                    </Box>
                  </ListItem>
                );
              })}
              {isLoading && (
                <ListItem sx={{ display: 'flex', justifyContent: 'flex-start', px: 0 }}>
                  <Box sx={{ p: 1.5, borderRadius: 2, bgcolor: 'background.paper', border: '1px solid', borderColor: 'secondary.main', boxShadow: 1 }}>
                    <CircularProgress size={20} />
                  </Box>
                </ListItem>
              )}
              <div ref={messagesEndRef} />
            </List>
          </Box>

          {/* Input Area */}
          <Box sx={{ p: 1, borderTop: 1, borderColor: 'divider', display: 'flex', bgcolor: 'background.paper' }}>
            <TextField
              fullWidth
              size="small"
              placeholder="Type your question..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyPress={(e) => {
                if (e.key === 'Enter') handleSend();
              }}
              disabled={isLoading}
              sx={{ '& .MuiOutlinedInput-root': { borderRadius: 5 } }}
            />
            <IconButton color="primary" onClick={handleSend} disabled={isLoading || !input.trim()} sx={{ ml: 1 }}>
              <SendIcon />
            </IconButton>
          </Box>
        </Paper>
      )}
    </>
  );
};

export default ChatWidget;
