import React, { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Paper, Button, Box, Alert } from '@mui/material';
import { getFlashSessionListAPI } from '@/apis/flashSession';
import type { SmsFlashPromotionSession } from '@/types/flash';
import { useNavigate, useSearchParams } from 'react-router';

const SelectSession: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const flashSaleId = searchParams.get('flashSaleId');
  const [sessions, setSessions] = useState<SmsFlashPromotionSession[]>([]);
  const [errorMsg, setErrorMsg] = useState<string>('');

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const res = await getFlashSessionListAPI();
      console.log('API Response:', res);
      if (res && res.data) {
        if (Array.isArray(res.data)) {
           setSessions(res.data);
        } else if ((res.data as any).list && Array.isArray((res.data as any).list)) {
           setSessions((res.data as any).list);
        } else {
           setSessions([]);
           setErrorMsg('API returned data but it is not an array: ' + JSON.stringify(res.data));
        }
      } else {
        setSessions([]);
        setErrorMsg('API returned empty res.data: ' + JSON.stringify(res));
      }
    } catch (e: any) {
      console.error(e);
      setErrorMsg(e.message || String(e));
    }
  };

  const formatTime = (timeValue: any) => {
    if (!timeValue) return 'N/A';
    if (typeof timeValue === 'string') {
      const parts = timeValue.split(':');
      if (parts.length >= 2) return `${parts[0]}:${parts[1]}`;
      return timeValue;
    }
    if (Array.isArray(timeValue)) {
      return `${timeValue[0]}:${timeValue[1]}`;
    }
    return String(timeValue);
  };

  return (
    <Box sx={{ p: 3 }}>
      <Card>
        <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6">Select Session to Manage Products</Typography>
        </Box>
        {errorMsg && <Alert severity="error">{errorMsg}</Alert>}
        {sessions.length === 0 && !errorMsg && <Alert severity="info">No sessions loaded. Please verify API response.</Alert>}
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: 'background.default' }}>
                <TableCell>Session ID</TableCell>
                <TableCell>Session Name</TableCell>
                <TableCell>Start Time</TableCell>
                <TableCell>End Time</TableCell>
                <TableCell align="center">Action</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {sessions.map((session) => (
                <TableRow key={session.id}>
                  <TableCell>{session.id}</TableCell>
                  <TableCell>{session.name}</TableCell>
                  <TableCell>{formatTime(session.startsAt)}</TableCell>
                  <TableCell>{formatTime(session.endsAt)}</TableCell>
                  <TableCell align="center">
                    <Button 
                      variant="contained" 
                      size="small" 
                      onClick={() => navigate(`/sms/flashProductRelation?flashSaleId=${flashSaleId}&slotId=${session.id}`)}
                    >
                      Product List
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Card>
    </Box>
  );
};

export default SelectSession;
