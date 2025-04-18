import CognitoExpress from 'cognito-express';

import logger from '../util/logger';
import {USER_POOL} from '../shared/constants';

export default class CognitoAuthorizer {

    static async initializeCognitoExpress() {

        try {
            CognitoAuthorizer.cognitoExpress = new CognitoExpress({
                region: process.env.userPoolRegion,
                cognitoUserPoolId: process.env.userPoolRegion + '_' + process.env.userPoolId,
                tokenUse: USER_POOL.TOKEN_TYPE,
                tokenExpiration: USER_POOL.TOKEN_EXPIRATION_TIME_IN_MS
            });
    
            await CognitoAuthorizer.cognitoExpress.promise;
            logger.debug ("Successfully initialized CognitoExpress");
            return Promise.resolve();
        } catch (error) {
            logger.error (`Error initializing CognitoExpress - ${error}`);
            return Promise.reject(new Error(error));
        }
    }

    static getCognitoExpress() {
        return CognitoAuthorizer.cognitoExpress;
    }
}