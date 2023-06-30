import face_recognition
import sqlite3
import shutil
import os
import socket
import threading
import time

# 실종자 등록 모듈
def register_missing_person(photo_path, list_data_path):
    # 이미지 파일 복사
    photo_destination = "C:/Users/mpban/OneDrive/바탕 화면/안면인식 서버/실종자등록데이터/image.jpg"
    shutil.copy(photo_path, photo_destination)

    # 텍스트 파일 읽기
    with open(list_data_path, "r", encoding="utf-8") as file:
        info_list = file.readlines()

    # 얼굴 인코딩
    known_image = face_recognition.load_image_file(photo_destination)
    face_features = face_recognition.face_encodings(known_image)[0]
    encoding_string = " ".join([str(x) for x in face_features])

    # 인코딩값과 실종자 정보를 데이터베이스에 저장
    with sqlite3.connect("info.db") as conn:
        c = conn.cursor()
        c.execute("INSERT INTO information(encoding, name, sex, age, area, date, phone) VALUES(?, ?, ?, ?, ?, ?, ?)",
                  (encoding_string, info_list[0].strip(), info_list[1].strip(), info_list[2].strip(),
                   info_list[3].strip(), info_list[4].strip(), info_list[5].strip()))
        conn.commit()

# 클라이언트 처리 함수
def handle_client(conn_s, addr):
    print("클라이언트 연결됨: ", addr)
    conn_s.send(bytes("안녕하세요. 클라이언트!", "utf-8"))
    conn_s.close()

# 메인 함수
def main():
    # 서버 소켓 생성(패밀리: ipv4, 타입:tcp)
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # 소켓을 특정 주소 및 포트에 바인딩
    host = "localhost"
    port = 12345
    s.bind((host, port))

    print('클라이언트 연결 대기중...')

    # 들어오는 연결 듣기
    s.listen(5)

    # 이전에 읽은 사진과 리스트를 추적하기 위한 변수
    previous_photo_mod_time = None

    photo_path = "D:/boot/gradu/data/photo.jpg"
    list_data_path = "D:/boot/gradu/data/list_data.txt"

    while True:
        # 클라이언트 연결 대기
        conn_s, addr = s.accept()
        print("Hello client! : ", addr)
        threading.Thread(target=handle_client, args=(conn_s, addr)).start()
        client_sockets.append(conn_s)

        # 사진 파일 확인
        if photo_path:
            if os.path.exists(photo_path):
                image_mod_time = os.path.getmtime(photo_path)
                if previous_photo_mod_time != image_mod_time:
                    os.makedirs("실종자등록데이터", exist_ok=True)  # 디렉토리 생성
                    shutil.copy(photo_path, "실종자등록데이터/image.jpg")  # 사진 복사
                    shutil.copy(photo_path, "실종자등록/image.jpg")  # 사진 복사
                    source_path = "D:/boot/gradu/data/photo.jpg"
                    destination_path = "C:/Users/mpban/OneDrive/바탕 화면/안면인식 서버/실종자등록데이터/image.jpg"
                    shutil.copy(source_path, destination_path)

                    previous_photo_mod_time = image_mod_time

                    os.makedirs("실종자등록데이터", exist_ok=True)  # 디렉토리 생성
                    shutil.copy(list_data_path, "실종자등록데이터/info_list.txt")  # 리스트 복사
                    register_missing_person(photo_path, list_data_path)
                    print('실종자 등록 완료')

        time.sleep(1)  # 1초마다 확인
# 서버 소켓 닫기
# 서버 소켓 닫기
    s.close()
client_sockets = []

if __name__ == "__main__":
  main()