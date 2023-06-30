import sqlite3

# 데이터베이스 연결
conn = sqlite3.connect('info.db')
cursor = conn.cursor()

# 삭제할 레코드의 조건을 지정
table_name = 'information'
condition = 'name = "조현호"'

# SQL DELETE 문 실행
delete_query = f"DELETE FROM {table_name} WHERE {condition}"
cursor.execute(delete_query)

# 변경 사항 저장 및 연결 종료
conn.commit()
conn.close()