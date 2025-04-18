const { Pool } = require("pg");

const cartDB = new Pool({
  user: "postgres",
  host: "localhost",
  database: "cart",
  password: "12345",
  port: 5432,
});

module.exports = cartDB;
