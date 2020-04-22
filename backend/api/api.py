from connect import Database
from flask import Flask, jsonify, request
from flask_restful import Resource, Api

app = Flask(__name__)
api = Api(app)

db = Database()

class Data(Resource):
    def get(self):
        return db.get_all_data()

    def post(self):
        json_data = request.get_json()
        db.add_row(json_data)
        return jsonify({"status": "success"})

api.add_resource(Data, '/')

def run():
    app.run(host='0.0.0.0', port=15544, debug=True)


if __name__=='__main__':
    run()
