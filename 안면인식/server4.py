import socket
import time

# 서버 정보
server_ip = '172.20.10.4'
server_port = 12345

while True:
    # 소켓 생성
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # 서버 연결
    client_socket.connect((server_ip, server_port))

    # 타임아웃 값 설정
    client_socket.settimeout(10)


    client_socket.sendall("1".encode("utf-8"))

    client_socket.close()

    time.sleep(5)