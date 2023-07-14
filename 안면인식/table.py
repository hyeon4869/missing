import sqlite3

con = sqlite3.connect('info.db')
cursor = con.cursor()
cursor.execute("SELECT * FROM information")
print(cursor.fetchall())