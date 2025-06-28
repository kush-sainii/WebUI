import React from 'react';
import { Link } from 'react-router-dom';

export default function Header() {
  return (
    <header className="bg-gray-800 p-4 text-white shadow">
      <h1 className="text-2xl font-bold text-center mb-2">Remote Server Dashboard</h1>
      <nav className="flex justify-center gap-4">
        <Link to="/" className="hover:underline">Command</Link>
        <Link to="/metrics" className="hover:underline">Metrics</Link>
      </nav>
    </header>
  );
}
