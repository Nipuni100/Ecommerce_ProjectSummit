import express from 'express';
import HttpStatusCodes from 'http-status-codes';

import logger from '../util/logger';
import authUtil from '../util/authUtil';
import httpUtil from '../util/httpUtil';
import userUtil from '../util/userUtil';
import {
    SUCCESS,
    ERROR_NOT_RECEIVED_REDIRECTION_URL,
    createErrorObj,
    LOCALHOST_ORIGIN_HOST_URL,
    HOME_PATH_TO_REDIRECT,
    LOGOUT_PATH_TO_REDIRECT,
    LOCAL_STAGE_TO_DISCARD,
    ACCESS_TOKEN,
    ID_TOKEN,
    CONTENT_ENCODING
} from '../shared/constants';
import User from '../model/user';

// Route for all the /dds API requests 
const router = express.Router();

router.get('/login', function (req, res) {

    if (req.query.host || (!req.query.host && process.env.originURL)) {
        if (req.query.host === 'localhost') {
            res.redirect(`${LOCALHOST_ORIGIN_HOST_URL}${HOME_PATH_TO_REDIRECT}`);
        } else {
            res.redirect(`${process.env.originURL}${HOME_PATH_TO_REDIRECT}`);
        }
    } else {
        res.status(HttpStatusCodes.BAD_REQUEST).json(createErrorObj(ERROR_NOT_RECEIVED_REDIRECTION_URL));
    }

});

router.get('/logout', function (req, res) {

    let redirectUri = '';

    if (req.query.host || (!req.query.host && process.env.originURL)) {
        if(req.query.host === 'localhost') {
            redirectUri = `${LOCALHOST_ORIGIN_HOST_URL}${LOGOUT_PATH_TO_REDIRECT}`;
        } else {
            redirectUri = `${process.env.originURL}${LOGOUT_PATH_TO_REDIRECT}`;
        }

        res.clearCookie('AWSELBAuthSessionCookie-1');
        res.clearCookie('AWSELBAuthSessionCookie-0');
    
        logger.debug (`Logout URL: https://${process.env.userPoolDomain}/logout?logout_uri=${redirectUri}&client_id=${process.env.userPoolClientId}`);
    
        res.redirect(`https://${process.env.userPoolDomain}/logout?logout_uri=${redirectUri}&client_id=${process.env.userPoolClientId}`);

    } else {
        res.status(HttpStatusCodes.BAD_REQUEST).json(createErrorObj(ERROR_NOT_RECEIVED_REDIRECTION_URL));
    }

});

router.get('/user', async function (req, res) {

    let user = new User();

    req = await authUtil.fetchUserClaims (req, res);
    user = userUtil.decodeUserClaims(req, user);
    logger.debug (user);

    res.send (user);
    
});

router.get('/*', function (req, res) {
    sendHttpRequest (req, res);
});

router.post('/*', function (req, res) {
    sendHttpRequest (req, res);
});

router.patch('/*', function (req, res) {
    sendHttpRequest (req, res);
});

router.put('/*', function (req, res) {
    sendHttpRequest (req, res);
});

router.delete('/*', function (req, res) {
    sendHttpRequest (req, res);
});

async function sendHttpRequest (req, res) {

    req.originalUrl = req.originalUrl.replace(LOCAL_STAGE_TO_DISCARD, '');
    const decodedUrl=decodeURIComponent((req.originalUrl))
    const response = await httpUtil.sendRequest (decodedUrl, req.method, req.headers[ACCESS_TOKEN], 
        req.headers[ID_TOKEN], req.body, req.headers[CONTENT_ENCODING]);

    logger.debug ('response status: ' + response.status);
    logger.debug ('response statusCode: ' + response.statusCode);
    
    if (response.status === SUCCESS) {
        return res.json(response.data);
    } else {
        const statusCode = (response.statusCode ? response.statusCode : 500);
        return res.status(statusCode).json(response.data);
    }

}

export default router;