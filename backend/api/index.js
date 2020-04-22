const express = require('express');
const mysql = require('mysql');

const connection = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USERNAME,
    password: process.env.DB_PW
});

const app = express();
const port = 15544;

app.use(express.json())

app.post('/', (req, res) => {
    connection.connect();
    const {selection_type, duration, distance_from_center, circle_radius, space_between} = req.body;
    connection.query('INSERT INTO selection.TRIAL (selection_type, duration, distance_from_center, circle_radius, space_between) VALUES (?, ?, ?, ?, ?);', [selection_type, duration, distance_from_center, circle_radius, space_between], function(error, results, fields) {
        if (error) res.status(500).send(error);

        res.status(200).send('success');
    });
    connection.end();
});

app.get('/', (req, res) => {
    connection.connect();
    connection.query('SELECT * FROM selection.TRIAL', (error, results, fields) => {
        if (error) res.status(500).send(error);

        res.status(200).send(results);
    })
    connection.end();
});

app.listen(port, '0.0.0.0');
