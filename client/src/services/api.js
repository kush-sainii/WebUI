import axios from 'axios';

export const sendCommand = (command) =>
  axios.post('/api/command', { command });

export const getMetrics = () =>
  axios.get('/api/metrics');
