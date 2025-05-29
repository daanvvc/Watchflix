import axios from 'axios';
import TokenManager from './TokenManager';

const URL = import.meta.env.VITE_HOST + "/user"

const UserApi = {
    getUser: (userId) => axios.get(URL + `/${userId}`, {headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}`}}),
    addUser: (user) => axios.post(URL + "/user", user, {headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}`}}),
    getRightOfAccess: (userId) => axios.get(URL + `/access/${userId}`, 
        {headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}`}, responseType: 'blob' }),
    deleteUser: (userId) => axios.delete(URL + `/${userId}`, {headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}`}}),
}

export default UserApi;