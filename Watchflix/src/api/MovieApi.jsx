import axios from 'axios';
import TokenManager from './TokenManager';

const URL = import.meta.env.VITE_HOST + "/movie"

const MovieApi = {
    getMovie: (movieId) => axios.get(URL + `/${movieId}`, {headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}`}}),
    getMovies: (number) => axios.get(URL+ `/nr/${number}`, {headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}`}}),
    getAllMovies: () => axios.get(URL+ `/`)
}

export default MovieApi;