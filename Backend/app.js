const mysql = require('mysql2');
const express = require('express');

const app = express();
const PORT = process.env.PORT || 3000;

// Create a connection pool to the MySQL database
const pool = mysql.createPool({
  host: 'localhost',
  user: '',
  password: '',
  database: 'sensordata'
});

// Create a GET endpoint to retrieve data from the database
app.get('/api/data', (req, res) => {
  // Get a connection from the pool
  pool.getConnection((err, connection) => {
    if (err) {
      console.error('Error connecting to database: ', err);
      res.status(500).send('Error connecting to database');
      return;
    }

    // Execute a SQL query to retrieve data from the "sensor_data" table
    connection.query('SELECT * FROM sensor_data', (err, results) => {
      // Release the connection back to the pool
      connection.release();

      if (err) {
        console.error('Error executing SQL query: ', err);
        res.status(500).send('Error executing SQL query');
        return;
      }

      // Send the results as a JSON response
      res.json(results);
    });
  });
});

// Start the server
app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}`);
});