import mysql.connector
from mysql.connector import errorcode

class Database:
    def __init__(self):
        self.config = { 
            'user': 'justin', 
            'password': '5544', 
            'host': 'localhost', 
            'database': 'selection' 
        }

    def create(self):
        cnx = mysql.connector.connect(**self.config)
        query = ("create table if not exists TRIAL ("
                 "id int auto_increment primary key,"
                 "selection_type varchar(50),"
                 "matrix_style varchar(50),"
                 "duration bigint,"
                 "distance_from_center double,"
                 "circle_radius float,"
                 "space_between float);")

        cursor = cnx.cursor()
        cursor.execute(query)
        cnx.commit()

        cursor.close()
        cnx.close()
        
    
    def get_all_data(self):
        cnx = mysql.connector.connect(**self.config)
        query = ("select * from TRIAL")
        cursor = cnx.cursor()
        cursor.execute(query)
        data = cursor.fetchall()
        cursor.close()
        cnx.close()
        return data

    def add_row(self, data):
        insert_query = """ insert into TRIAL (selection_type, matrix_style, duration, distance_from_center, circle_radius, space_between)
                            values (%s, %s, %s, %s, %s, %s)"""

        cnx = mysql.connector.connect(**self.config)
        cursor = cnx.cursor()

        record_tuple = (data['selection_type'], data['matrix_style'], data['duration'], data['distance_from_center'], data['circle_radius'], data['space_between'])

        cursor.execute(insert_query, record_tuple)
        cnx.commit()
        cursor.close()
        cnx.close()

    def delete_all_data(self):
        cnx = mysql.connector.connect(**self.config)
        cursor = cnx.cursor()

        cursor.execute("delete from TRIAL where id > 0")
        cnx.commit()

        cursor.close()
        cnx.close()
