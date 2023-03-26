import busio
import digitalio
import board
import time
import adafruit_mcp3xxx.mcp3008 as MCP
from adafruit_mcp3xxx.analog_in import AnalogIn

# Calibration values
moisture_min = 0   # minimum moisture value
moisture_max = 100 # maximum moisture value
adc_min = 0        # minimum ADC value
adc_max = 65535    # maximum ADC valu
# create the spi bus
spi = busio.SPI(clock=board.SCK, MISO=board.MISO, MOSI=board.MOSI)

# create the cs (chip select)
cs = digitalio.DigitalInOut(board.CE0)

# create the mcp object
mcp = MCP.MCP3008(spi, cs)

# create an analog input channel on pin 0
chan = AnalogIn(mcp, MCP.P0)

while True:
     # read the raw ADC value
    adc_value = chan.value

    # convert the ADC value to a moisture percentage
    moisture_value = (adc_value - adc_min) * (moisture_max - moisture_min) / (adc_max - adc_min) + moisture_min

    # print the moisture percentage
    print('Moisture Percentage: {:.2f}%'.format(moisture_value))

    time.sleep(1)

