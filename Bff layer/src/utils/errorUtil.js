const { StatusCodes } = require("http-status-codes");

const createError = (message, statusCode = StatusCodes.INTERNAL_SERVER_ERROR) => {
  const error = new Error(message);
  error.status = statusCode;
  return error;
};

class CustomError extends Error {
    constructor(message, status) {
      super(message);
      this.status = status || 500;  // Default to 500 if no status is provided
    }
  }
  
module.exports = CustomError;

module.exports = { createError };