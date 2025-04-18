import HttpStatusCodes from 'http-status-codes';

import {
    ERROR_ACCESS_ID_TOKEN_NOT_PROVIDED,
    ERROR_ACCESS_TOKEN_INVALID,
    createErrorObj,
    ACCESS_TOKEN,
    ID_TOKEN
} from '../shared/constants';
import logger from '../util/logger';
import CognitoAuthorizer from '../authorizer/cognitoAuthorizer';

export default function auth (req, res, next) {

        logger.debug (`Authenticating user`);
        logger.debug (req.headers);

        if (!req.headers[ACCESS_TOKEN] || !req.headers[ID_TOKEN]) {
            logger.error (ERROR_ACCESS_ID_TOKEN_NOT_PROVIDED);
            res.status(HttpStatusCodes.UNAUTHORIZED).json(createErrorObj(ERROR_ACCESS_ID_TOKEN_NOT_PROVIDED));
            return;
        }

        CognitoAuthorizer.getCognitoExpress().validate(req.headers[ACCESS_TOKEN], function(error, response) {
            if (error) {
                logger.error (`${ERROR_ACCESS_TOKEN_INVALID} - ${error}`);
                res.status(HttpStatusCodes.UNAUTHORIZED).json(createErrorObj(ERROR_ACCESS_TOKEN_INVALID));
            } else {
                logger.debug (`User successfully authenticated`);
                next();
            }
        });

  }