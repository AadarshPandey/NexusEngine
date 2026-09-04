import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Switch, Paper, Button, Box } from '@mui/material';
import { getFlashSessionListAPI } from '@/apis/flashSession';
import type { SmsFlashPromotionSession } from '@/types/flash';

const FlashSessionList: React.FC = () => {
  const [sessions, setSessions] = useState<SmsFlashPromotionSession[]>([]);

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const res = await getFlashSessionListAPI();
      setSessions(res.data || []);
    } catch (e) {
      console.error(e);
    }
  };

  const formatTime = (timeString: string) => {
    if (!timeString) return 'N/A';
    // Often time is returned like '08:00:00'
    const parts = timeString.split(':');
    return `${parts[0]}:${parts[1]}`;
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
                <TableCell>{session.name}</TableCell>
                <TableCell>{formatTime(session.startTime)}</TableCell>
                <TableCell>{formatTime(session.endTime)}</TableCell>
                <TableCell>
                  <Switch size="small" checked={session.status === 1} color="primary" />
                </TableCell>
                <TableCell>
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
