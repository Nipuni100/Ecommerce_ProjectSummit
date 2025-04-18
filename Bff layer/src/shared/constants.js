exports.SPRING_BOOT_PRODUCTS_URL = 'http://localhost:8080/api/v1/products';
exports.CART_URL = 'http://localhost:8081/api/v1/carts';
exports.SPRING_BOOT_ORDER_URL = 'http://localhost:8082/api/v1/orders';


//Cognito user pool default configuration 
export const USER_POOL = {
    TOKEN_TYPE: 'access',
    TOKEN_EXPIRATION_TIME_IN_MS: '3600000'
}

// Errors 
export const ERROR = 'error';
export const ERROR_INITIALIZATION = 'Function Initialization failed';
export const ERROR_ACCESS_ID_TOKEN_NOT_PROVIDED = 'Access and/or Id token is not provided';
export const ERROR_ACCESS_TOKEN_INVALID = 'Access token is invalid';
export const ERROR_UNABLE_TO_FETCH_USER_INFO = 'Unable to fetch user information';
export const ERROR_ALL_SOME_USER_ATTRIBUTES_MISSING = 'All/some user attributes are missing';
export const ERROR_IN_RESPONSE = 'Response error';
export const ERROR_IN_COMMUNICATION = 'Communication error. No response received from MS.';
export const ERROR_WHILE_SETTING_UP_REQUEST = 'Error while setting up a request';
export const ERROR_NOT_RECEIVED_REDIRECTION_URL = 'Redirection URL not received';

export function createErrorObj (errorMessage) {
    return {
        [ERROR]: errorMessage
    }
}

// User attributes
export const USER_ATTRIBUTES = {
    ID: 'nickname',
    FIRST_NAME: 'given_name',
    LAST_NAME: 'family_name',
    EMAIL: 'email',
    OPCO_IDS: 'locale',
    ROLE: 'profile'
}

// Key, value
export const KEY = 'Name';
export const VALUE = 'Value';

export const SUCCESS = 'SUCCESS';
export const FAILURE = 'FAILURE';

// Paths
export const LOCALHOST_ORIGIN_HOST_URL = 'http://localhost:3000';
export const HOME_PATH_TO_REDIRECT = '/app/home';
export const LOGOUT_PATH_TO_REDIRECT = '/login';
export const PATH_DDS = '/dds';
export const LOCAL_STAGE_TO_DISCARD = '/local';

// Request headers
export const CORRELATIONID_HEADER = 'x-correlation-id';
export const ACCESS_TOKEN = 'x-amzn-oidc-accesstoken';
export const ID_TOKEN = 'x-amzn-oidc-data';
export const CONTENT_ENCODING = 'content-encoding';
