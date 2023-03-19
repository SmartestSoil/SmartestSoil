import json
import random
from datetime import datetime, timezone
import time
import mysql.connector

# Set the range for the random values
moisture_range = (10, 50)
temperature_range = (0, 35)

# Connect to the MySQL database
cnx = mysql.connector.connect(user='', password='',
                              host='', database='')
cursor = cnx.cursor()

# Create the table to store the sensor data
create_table = '''CREATE TABLE IF NOT EXISTS sensor_data (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    timestamp DATETIME,
                    sensor_id VARCHAR(20),
                    soil_moisture FLOAT,
                    soil_temperature FLOAT
                )'''
cursor.execute(create_table)

# Create a loop that runs every 1 minute
while True:
    # Create a dictionary to store the sensor data
    sensor_data = {
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "sensor_id": "soil_sensor_001",
        "soil_moisture": round(random.uniform(*moisture_range), 2),
        "soil_temperature": round(random.uniform(*temperature_range), 2)
    }

    # Insert the data into the MySQL database
    insert_data = '''INSERT INTO sensor_data (timestamp, sensor_id, soil_moisture, soil_temperature)
                      VALUES (%s, %s, %s, %s)'''
    data_values = (sensor_data['timestamp'], sensor_data['sensor_id'],
                   sensor_data['soil_moisture'], sensor_data['soil_temperature'])
    cursor.execute(insert_data, data_values)
    cnx.commit()

    # Delete the oldest row if the count is greater than or equal to 129,600
    count_query = '''SELECT COUNT(*) FROM sensor_data'''
    cursor.execute(count_query)
    count = cursor.fetchone()[0]
    if count >= 129600:
        delete_query = '''DELETE FROM sensor_data ORDER BY id LIMIT 1'''
        cursor.execute(delete_query)
        cnx.commit()

    # Wait for 1 min before generating the next data point
    time.sleep(60)  # 1 min in seconds

# Close the MySQL database connection when done
cursor.close()
cnx.close()