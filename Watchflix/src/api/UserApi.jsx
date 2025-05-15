import axios from 'axios';

const URL = import.meta.env.VITE_HOST + "/user"

const UserApi = {
    getUser: (userId) => axios.get(URL + `/${userId}`),
    addUser: (user) => axios.post(URL + "/user", user),    
}

export default UserApi;