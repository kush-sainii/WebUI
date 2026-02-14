import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const sendCommand = (command) =>
  axios.post(`${API_BASE_URL}/api/command`, { command });

export const getMetrics = () =>
  axios.get(`${API_BASE_URL}/api/metrics`);
