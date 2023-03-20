import json
import random
from datetime import datetime, timezone
import time
import mysql.connector


# Define the mean and standard deviation of the normal distribution
mean = 25 # mean temperature in Celsius
stddev = 5 # standard deviation in Celsius

# Generate a random temperature value using the normal distribution
temperature_range = random.normalvariate(mean, stddev)

# Define the alpha and beta parameters of the beta distribution
alpha = 2
beta = 5

# Generate a random moisture value using the beta distribution
#moisture_range = random.betavariate(alpha, beta) * 100
moisture_min = 20.0
moisture_max = 100.0
moisture_value = random.betavariate(alpha, beta) * (moisture_max - moisture_min) + moisture_min


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
        "soil_moisture": round(moisture_value, 2),
        "soil_temperature": round(temperature_range, 2)
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