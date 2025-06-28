import React, { useEffect, useState } from 'react';
import { getMetrics } from '../services/api';

export default function MetricsDashboard() {
  const [metrics, setMetrics] = useState(null);

  useEffect(() => {
    const fetchMetrics = async () => {
      try {
        const res = await getMetrics();
        setMetrics(res.data);
      } catch (err) {
        console.error("Failed to fetch metrics", err);
      }
    };

    fetchMetrics();
    const interval = setInterval(fetchMetrics, 5000);
    return () => clearInterval(interval);
  }, []);

  if (!metrics) return <p className="text-center mt-8">Loading metrics...</p>;

  const { disk, memory, cpu, network, uptime } = metrics;

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h2 className="text-3xl font-bold mb-6 text-gray-700">Server Health Metrics</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-white shadow rounded-lg p-4">
          <h3 className="font-semibold mb-2">Disk</h3>
          <p>Total: {disk.total_gb} GB</p>
          <p>Used: {disk.used_gb} GB</p>
          <p>Free: {disk.free_gb} GB</p>
        </div>
        <div className="bg-white shadow rounded-lg p-4">
          <h3 className="font-semibold mb-2">Memory</h3>
          <p>Total: {memory.total_mb} MB</p>
          <p>Used: {memory.used_mb} MB</p>
          <p>Free: {memory.free_mb} MB</p>
          <p>Usage: {memory.usage_percent}%</p>
        </div>
        <div className="bg-white shadow rounded-lg p-4">
          <h3 className="font-semibold mb-2">CPU</h3>
          <p>Usage: {cpu.usage_percent}%</p>
          <ul className="list-disc ml-5">
            {cpu.per_core.map((load, idx) => (
              <li key={idx}>Core {idx + 1}: {load}%</li>
            ))}
          </ul>
        </div>
        <div className="bg-white shadow rounded-lg p-4">
          <h3 className="font-semibold mb-2">Network</h3>
          <p>Private IP: {network.ip}</p>
          <p>Public IP: {network.public_ip}</p>
        </div>
        <div className="bg-white shadow rounded-lg p-4 md:col-span-2">
          <h3 className="font-semibold mb-2">⏱Uptime</h3>
          <p>App Uptime: {uptime.app_seconds} seconds</p>
          <p>System Uptime: {uptime.system_seconds} seconds</p>
        </div>
      </div>
    </div>
  );
}
