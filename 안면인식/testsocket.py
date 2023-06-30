import socket

# Create a socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect to the server
s.connect(("192.168.123.111", 3456))

# Send data to the server
s.sendall("Hello, world!".encode())

# Receive data from the server
data = s.recv(1024)

# Print the data received from the server
print(data.decode())

# Close the connection
s.close()