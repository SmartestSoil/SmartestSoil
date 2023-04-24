import numpy as np
from datetime import datetime, timezone
import time
import mysql.connector
import busio
import digitalio
import board
import adafruit_mcp3xxx.mcp3008 as MCP
from adafruit_mcp3xxx.analog_in import AnalogIn

# Define the mean and standard deviation of the normal distribution
mean = 27  # mean temperature in Celsius
stddev = 5  # standard deviation in Celsius

min_temp = 17
max_temp = 25

# Generate a random temperature value using the normal distribution
temperature_range = np.clip(random.normalvariate(mean, stddev), min_temp, max_temp)
# Calibration values
moisture_min = 0   # minimum moisture value
moisture_max = 100  # maximum moisture value
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


 # Define the initial moisture level for the second sensor
moisture_value_s2 = random.uniform (60, 86)
    
# minimum and maximum moisture values
min_moisture_s2 = 0
max_moisture_s2 = 100
    
# minimum and maximum moisture decrease rates
min_moisture_decrease = 0.15
max_moisture_decrease = 0.30
generate_new_moisture = False 
    
# Create a loop that runs every 30 minute
while True:
    # Generate a random temperature value using the normal distribution
    temperature_range = np.clip(random.normalvariate(mean, stddev), min_temp, max_temp)

    # read the raw ADC value
    adc_value = chan.value

    # convert the ADC value to a moisture percentage
    # moisture_value = (adc_value - adc_min) * (moisture_max - moisture_min) / (adc_max - adc_min) + moisture_min
    moisture_value_s1 = ((adc_max - adc_value) - adc_min) * (moisture_max - moisture_min) / (adc_max - adc_min) + moisture_min
    
      # Decrease the moisture level of the second sensor by a random moisture decrease rate
    if moisture_value_s2 > min_moisture_s2:
        moisture_decrease = random.uniform(min_moisture_decrease, max_moisture_decrease)
        moisture_value_s2 -= moisture_decrease

    # Generate a new random moisture value if the moisture level falls below the minimum threshold
    if moisture_value_s2 < min_moisture_s2:
        moisture_value_s2 = random.uniform(60, 100)
        generate_new_moisture = False  # Reset the flag
    
    # Create a dictionary to store the sensor data
    sensor_data = {
        "timestamp": datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S'),
        "sensor_id": "soil_sensor_001",
        "soil_moisture": round(moisture_value_s1, 2),
        "soil_temperature": round(float(temperature_range), 2)
    }

    # Insert the data into the MySQL database
    insert_data = '''INSERT INTO sensor_data (timestamp, sensor_id, soil_moisture, soil_temperature)
                      VALUES (%s, %s, %s, %s)'''
    data_values = (sensor_data['timestamp'], sensor_data['sensor_id'],
                   sensor_data['soil_moisture'], sensor_data['soil_temperature'])
    cursor.execute(insert_data, data_values)
    cnx.commit()

# Create a dictionary to store the data for the second sensor
    sensor_data_2 = {
        "timestamp": datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S'),
        "sensor_id": "soil_sensor_002",
        "soil_moisture": round(moisture_value_s2, 2),
        "soil_temperature": round(float(temperature_range), 2)
    }

    # Insert the data for the second sensor into the MySQL database
    insert_data_2 = '''INSERT INTO sensor_data (timestamp, sensor_id, soil_moisture, soil_temperature)
                      VALUES (%s, %s, %s, %s)'''
    data_values_2 = (sensor_data_2['timestamp'], sensor_data_2['sensor_id'],
                   sensor_data_2['soil_moisture'], sensor_data_2['soil_temperature'])
    cursor.execute(insert_data_2, data_values_2)
    cnx.commit()

    # Generate small random offsets for soil_moisture and soil_temperature
    moisture_offset = random.uniform(-5, 5)
    temperature_offset = random.uniform(-0.5, 0.5)

    # Create a dictionary to store the data for the third sensor, with slightly modified values
    sensor_data_3 = {
        "timestamp": datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S'),
        "sensor_id": "soil_sensor_003",
        "soil_moisture": round(moisture_value_s2 + moisture_offset, 2),
        "soil_temperature": round(float(temperature_range) + temperature_offset, 2)
    }

    # Insert the data for the third sensor into the MySQL database
    insert_data_3 = '''INSERT INTO sensor_data (timestamp, sensor_id, soil_moisture, soil_temperature)
                      VALUES (%s, %s, %s, %s)'''
    data_values_3 = (sensor_data_3['timestamp'], sensor_data_3['sensor_id'],
                     sensor_data_3['soil_moisture'], sensor_data_3['soil_temperature'])
    cursor.execute(insert_data_3, data_values_3)
    cnx.commit()


    # Delete the oldest row if the count is greater than or equal to 129,600
    count_query = '''SELECT COUNT(*) FROM sensor_data'''
    cursor.execute(count_query)
    count = cursor.fetchone()[0]
    if count >= 129600:
        delete_query = '''DELETE FROM sensor_data ORDER BY id LIMIT 1'''
        cursor.execute(delete_query)
        cnx.commit()

    # Wait for 30 min before generating the next data point
    time.sleep(1800)  # 30 min in seconds

# Close