import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Fetch welcome message from Spring Boot
    fetch('http://localhost:8080/api/welcome')
        .then(response => response.text())
        .then(data => {
          setMessage(data);
          setLoading(false);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          setMessage('Welcome to your React App!');
          setLoading(false);
        });
  }, []);

  if (loading) {
    return (
        <div className="App">
          <header className="App-header">
            <p>Loading...</p>
          </header>
        </div>
    );
  }

  return (
      <div className="App">
        <header className="App-header">
          <h1>Welcome</h1>
          <p>{message}</p>
          <p>Your Spring Boot + React application is running!</p>
        </header>
      </div>
  );
}

export default App;