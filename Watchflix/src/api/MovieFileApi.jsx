import axios from 'axios';
import TokenManager from './TokenManager';

const URL = import.meta.env.VITE_HOST + "/movieFile"

const MovieFileApi = {
    getMovieFile: (movieId) => axios.get(URL + `/${movieId}`, {headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}`}}),
    uploadMovieFile: (movieFile) => axios.post(URL + "/upload", movieFile, { 
        headers: { 'Content-Type': 'multipart/form-data', Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),    
}

export default MovieFileApi;