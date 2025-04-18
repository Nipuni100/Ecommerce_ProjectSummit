const { getReasonPhrase, StatusCodes } = require('http-status-codes');

class ApiError extends Error {
    constructor(statusCode = StatusCodes.INTERNAL_SERVER_ERROR, message = getReasonPhrase(statusCode)) {
        super(message);
        this.statusCode = statusCode;
        Error.captureStackTrace(this, this.constructor);
    }
}

module.exports = ApiError;
