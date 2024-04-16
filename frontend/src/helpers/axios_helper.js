import axios from 'axios';
import {jwtDecode} from 'jwt-decode';


export const getAuthToken = () => {
    return window.localStorage.getItem('auth_token');
};

export const getUserInfo = () => {
    const auth_token = getAuthToken();
    if (auth_token === null) {
        return {id: null, sub: null, email: null, role: null, exp: null};
    }
    console.log(jwtDecode(getAuthToken()))
    return jwtDecode(getAuthToken());
}

export const setAuthHeader = (token) => {
    if (token !== null) {
      window.localStorage.setItem("auth_token", token);
    } else {
      window.localStorage.removeItem("auth_token");
    }
};

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.post['Content-Type'] = 'application/json; charset=UTF-8';

export const request = (method, url, data) => {

    let headers = {};
    if (getAuthToken() !== null && getAuthToken() !== "null") {
        headers = {'Authorization': `Bearer ${getAuthToken()}`};
    }

    return axios({
        method: method,
        url: url,
        headers: headers,
        data: data});
};