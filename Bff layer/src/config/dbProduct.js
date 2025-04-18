const { Pool } = require("pg");

const productDB = new Pool({
  user: "postgres",
  host: "localhost",
  database: "productservice",
  password: "12345",
  port: 5432,
});

module.exports = productDB;
