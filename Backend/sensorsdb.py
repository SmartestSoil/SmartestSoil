import json
import random
from datetime import datetime, timezone
import time
import mysql.connector
import busio
import digitalio
import board
import adafruit_mcp3xxx.mcp3008 as MCP
from adafruit_mcp3xxx.analog_in import AnalogIn

# Define the mean and standard deviation of the normal distribution
mean = 25 # mean temperature in Celsius
stddev = 5 # standard deviation in Celsius

# Generate a random temperature value using the normal distribution
temperature_range = random.normalvariate(mean, stddev)

# Calibration values
moisture_min = 0   # minimum moisture value
moisture_max = 100 # maximum moisture value
adc_min = 0        # minimum ADC value
adc_max = 65535    # maximum ADC value

# create the spi bus
spi = busio.SPI(clock=board.SCK, MISO=board.MISO, MOSI=board.MOSI)

# create the cs (chip select)
cs = digitalio.DigitalInOut(board.CE0)

# create the mcp object
mcp = MCP.MCP3008(spi, cs)

# create an analog input channel on pin 0
chan = AnalogIn(mcp, MCP.P0)

# Connect to the MySQL database
cnx = mysql.connector.connect(user='group-01', password='spring2023',
                              host='localhost', database='MDP2023')
cursor = cnx.cursor()

# Create the table to store the sensor data1
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
    # Generate a random temperature value using the normal distribution
    temperature_range = random.normalvariate(mean, stddev)

    # read the raw ADC value
    adc_value = chan.value

    # convert the ADC value to a moisture percentage
    #moisture_value = (adc_value - adc_min) * (moisture_max - moisture_min) / (adc_max - adc_min) + moisture_min
    moisture_value = ((adc_max - adc_value) - adc_min) * (moisture_max - moisture_min) / (adc_max - adc_min) + moisture_min

    # Create a dictionary to store the sensor data
    sensor_data = {
        "timestamp": datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S'),
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

# Close