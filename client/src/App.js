import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import MetricsPage from './pages/MetricsPage';
import './global.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/metrics" element={<MetricsPage />} />
      </Routes>
    </Router>
  );
}

export default App;
