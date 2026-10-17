import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Switch, Paper, Button, Box } from '@mui/material';
import { getFlashSessionListAPI } from '@/apis/promotion';
import type { SmsFlashPromotionSession } from '@/types/promotion';
import { useNavigate } from 'react-router';

const FlashSessionList: React.FC = () => {
  const navigate = useNavigate();
  const [sessions, setSessions] = useState<SmsFlashPromotionSession[]>([]);

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const res = await getFlashSessionListAPI();
      setSessions(Array.isArray(res) ? res : []);
    } catch (e) {
      console.error(e);
    }
  };

  const formatTime = (timeString: string) => {
    if (!timeString) return 'N/A';
    const parts = String(timeString).split(':');
    return parts.length >= 2 ? `${parts[0]}:${parts[1]}` : String(timeString);
  };

  return (
    <Card sx={{ p: 3, m: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5">Flash Sessions Configuration</Typography>
        <Button variant="contained" color="primary">Add Session</Button>
      </Box>
      <TableContainer component={Paper} elevation={0} variant="outlined">
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'grey.50' }}>
              <TableCell>Session ID</TableCell>
              <TableCell>Session Name</TableCell>
              <TableCell>Start Time</TableCell>
              <TableCell>End Time</TableCell>
              <TableCell>Enable Status</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {sessions.map((session) => (
              <TableRow key={session.id} hover>
                <TableCell>{session.id}</TableCell>
                <TableCell>{session.name || session.label}</TableCell>
                <TableCell>{formatTime(session.startTime || session.startsAt || '')}</TableCell>
                <TableCell>{formatTime(session.endTime || session.endsAt || '')}</TableCell>
                <TableCell>
                  <Switch size="small" checked={Boolean(session.status ?? session.isActive)} color="primary" />
                </TableCell>
                <TableCell>
                  <Button size="small" variant="contained" color="secondary" onClick={() => navigate(`/promotion/flash-sales/1/items?flashSaleId=1&slotId=${session.id}`)}>Product Config</Button>
                  <Button size="small" variant="text" color="primary">Edit</Button>
                  <Button size="small" variant="text" color="error">Delete</Button>
                </TableCell>
              </TableRow>
            ))}
            {sessions.length === 0 && (
              <TableRow>
                <TableCell colSpan={6} align="center" sx={{ py: 5 }}>No flash sessions found.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Card>
  );
};

export default FlashSessionList;
