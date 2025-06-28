import React, { useState } from 'react';
import { sendCommand } from '../services/api';

export default function CommandForm() {
  const [command, setCommand] = useState('');
  const [output, setOutput] = useState([]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await sendCommand(command);
      setOutput(res.data);
    } catch (err) {
      setOutput(["Error: " + (err.response?.data || err.message)]);
    }
  };

  return (
    <div className="max-w-xl mx-auto bg-white shadow-lg rounded-lg p-6 mt-8">
      <h2 className="text-2xl font-bold mb-4 text-gray-700">Execute Command</h2>
      <form onSubmit={handleSubmit} className="flex gap-2">
        <input
          type="text"
          value={command}
          onChange={(e) => setCommand(e.target.value)}
          placeholder="e.g., mkdir helloworld"
          className="flex-1 p-2 border rounded-lg border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 rounded-lg hover:bg-blue-600"
        >
          Run
        </button>
      </form>
      <pre className="mt-4 bg-gray-100 p-3 rounded-lg text-sm text-gray-700 overflow-auto">
        {output.join('\n')}
      </pre>
    </div>
  );
}
