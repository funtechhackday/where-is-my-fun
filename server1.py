#!flask/bin/python
from flask import Flask, jsonify
import mysql.connector
from mysql.connector import Error
app = Flask(__name__)
def connect():
    """ Connect to MySQL database """
    try:
        conn = mysql.connector.connect(host='192.168.0.101',
                                       database='wheremyfun',
                                       user='sibsutis',
                                       password='18091995')
        if conn.is_connected():
            print('Connected to MySQL database')
    except Error as e:
        print(e)
    return conn


@app.route('/login/user/login/<log>/password/<passw>', methods=['GET'])
def loginUser(log, passw):
    connection = connect()
    query = "SELECT * FROM users WHERE login=%s and password=%s"
    args = (log, passw)
    try:
      cursor = connection.cursor()
      cursor.execute(query, args)
      row = cursor.fetchone()
      if row != None:
	(login, password, fio, photo, cards) = row
	state = 'ok'
	return jsonify({'state': state, 'login': log, 'fio': fio, 'photo': photo, 'cards': cards})
      else:
	state = 'error'
	return jsonify({'state': state})
    except Error as error:
      print(error)
      state = 'error'
    finally:
      cursor.close()
      connection.close()
      
@app.route('/register/user/login/<log>/password/<passw>/title/<title>/description/<desc>/lat/<lat>/lon/<lon>', methods=['GET'])
def registerUser(log, passw, fio, photo, cards):
    connection = connect()
    query = "INSERT INTO users(login,password,fio,photo,cards) " \
            "VALUES(%s,%s,%s,%s,%s,%s)"
    args = (log, passw, fio, photo, cards)
    try:
      cursor = connection.cursor()
      cursor.execute(query, args)
      connection.commit()
      state = 'ok'
      return jsonify({'state': state})
    except Error as error:
      print(error)
      state = 'error'
      return jsonify({'state': state})
    finally:
      cursor.close()
      connection.close()

@app.route('/login/company/login/<log>/password/<passw>', methods=['GET'])
def loginComp(log, passw):
    connection = connect()
    query = "SELECT * FROM companies WHERE lat<%s and lon=%s"
    args = (lat, lon)
    try:
      cursor = connection.cursor()
      cursor.execute(query, args)
      row = cursor.fetchone()
      if row != None:
	(login, password, title, desc, lat, lon) = row
	state = 'ok'
	return jsonify({'state': state, 'login': log, 'title': title, 'desc': desc, 'lat': lat, 'lon': lon})
      else:
	state = 'error'
	return jsonify({'state': state})
    except Error as error:
      print(error)
      state = 'error'
    finally:
      cursor.close()
      connection.close()
      
@app.route('/register/company/login/<log>/password/<passw>/title/<title>/description/<desc>/lat/<lat>/lon/<lon>', methods=['GET'])
def registerComp(log, passw, title, desc, lat, lon):
    connection = connect()
    query = "INSERT INTO companies(login,password,title,description,lat,lon) " \
            "VALUES(%s,%s,%s,%s,%s,%s)"
    args = (log, passw, title, desc, lat, lon)
    try:
      cursor = connection.cursor()
      cursor.execute(query, args)
      connection.commit()
      state = 'ok'
      return jsonify({'state': state})
    except Error as error:
      print(error)
      state = 'error'
      return jsonify({'state': state})
    finally:
      cursor.close()
      connection.close()

@app.route('/<lat>/<lon>', methods=['GET'])
def searchComp(lat, lon):
    connection = connect()
    query = "SELECT * FROM companies WHERE abs(lat-%s) < 0.3 and abs(lon-%s) < 0.3"
    args = (lat, lon)
    listRows = []
    try:
        cursor = connection.cursor()
        cursor.execute(query, args)
        rows = cursor.fetchall()
        print(rows)
        for row in rows:
            print(row)
            (login, password, title, desc, lat, lon, addr) = row
            listRows = listRows + [{'title':title, 'desc':desc, 'lat':lat, 'lon':lon, 'addr':addr}]
            print(listRows)
        if cursor.rowcount != 0:
            status = 'ok'
            return jsonify({'status': status, 'listRows':listRows})
        else:
            status = 'error'
            return jsonify({'status': status})
    except Error as error:
      print(error)
      status = 'error'
    finally:
      cursor.close()
      connection.close()

@app.route('/bookit/user/<log>', methods=['GET'])
def searchCompBook(log):
    connection = connect()
    query = "SELECT * FROM bookit WHERE loginUser = %s AND %s"
    args = (log, 1)
    print(args)
    listRows = []
    try:
        cursor = connection.cursor()
        cursor.execute(query, args)
        rows = cursor.fetchall()        
        print(rows)
        for row in rows:
            print(row)
            (reservId, loginComp, loginUser, status, describtion, reservation) = row
            listRows = listRows + [{'reservId':reservId,'loginComp':loginComp, 'loginUser':loginUser, 'status':status, 'describtion':describtion, 'reservation':reservation}]
            print(listRows)
        if cursor.rowcount != 0:
            status = 'ok'
            return jsonify({'status': status, 'listRows':listRows})
        else:
            status = 'error'
            return jsonify({'status': status})
    except Error as error:
      print(error)
      status = 'error'
    finally:
      cursor.close()
      connection.close()
      
@app.route('/bookit/comp/<log>', methods=['GET'])
def searchUserBook(log):
    connection = connect()
    query = "SELECT * FROM bookit WHERE loginComp = %s AND %s"
    args = (log, 1)
    print(args)
    listRows = []
    try:
        cursor = connection.cursor()
        cursor.execute(query, args)
        rows = cursor.fetchall()        
        print(rows)
        for row in rows:
            print(row)
            (reservId, loginComp, loginUser, status, describtion, reservation) = row
            listRows = listRows + [{'reservId':reservId,'loginComp':loginComp, 'loginUser':loginUser, 'status':status, 'describtion':describtion, 'reservation':reservation}]
            print(listRows)
        if cursor.rowcount != 0:
            status = 'ok'
            return jsonify({'status': status, 'listRows':listRows})
        else:
            status = 'error'
            return jsonify({'status': status})
    except Error as error:
      print(error)
      status = 'error'
    finally:
      cursor.close()
      connection.close()

@app.route('/bookit/add/<logC>/<logU>/<st>/<desc>/<res>', methods=['GET'])
def addBook(logC, logU, st, desc, res):
    connection = connect()
    query = "INSERT INTO `bookit`(`reservId`, `loginComp`, `loginUser`, `status`, `describtion`, `reservation`)"\
	     "VALUES (%s,%s,%s,%s,%s,%s)"
    args = ('ll', logC, logU, st, desc, res)
    print(args)
    try:
      cursor = connection.cursor()
      cursor.execute(query, args)
      connection.commit()
      status = 'ok'
      if cursor.rowcount != 0:
	  status = 'ok'
	  return jsonify({'status': status})
      else:
	  status = 'error'
	  return jsonify({'status': status})
    except Error as error:
      print(error)
      status = 'error'
    finally:
      cursor.close()
      connection.close()

@app.route('/sales', methods=['GET'])
def getSales():
    connection = connect()
    #query = "SELECT *  FROM companies"
    query = "SELECT title, sales.description, lat, lon FROM companies, sales WHERE sales.login = companies.login"
    listRows = []
    try:
        cursor = connection.cursor()
        cursor.execute(query)
        rows = cursor.fetchall()
        print(rows)
        for row in rows:
            print(row)
            (title, desc, lt, ln) = row
            listRows = listRows + [{'titleComp':title,'describtion':desc,'lat':lt,'lon':ln}]
            print(listRows)
        if cursor.rowcount != 0:
            status = 'ok'
            return jsonify({'status': status, 'listRows':listRows})
        else:
            status = 'error'
            return jsonify({'status': status})
    except Error as error:
      print(error)
      status = 'error'
    finally:
      cursor.close()
      connection.close()

@app.route('/sales/companies/<log>/<desc>', methods=['GET'])
def setSales(log,desc):
    connection = connect()
    query = "INSERT INTO sales(id,login,description) " \
            "VALUES(%s,%s,%s)"
    args = ("ll" ,log, desc)
    try:
      cursor = connection.cursor()
      cursor.execute(query, args)
      connection.commit()
      state = 'ok'
      return jsonify({'state': state})
    except Error as error:
      print(error)
      state = 'error'
      return jsonify({'state': state})
    finally:
      cursor.close()
      connection.close()

if __name__ == '__main__':
    app.run(debug = True, host = '0.0.0.0')


