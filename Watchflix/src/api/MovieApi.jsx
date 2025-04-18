import axios from 'axios';

const URL = import.meta.env.VITE_HOST + "/movie"

const MovieApi = {
    getMovie: (movieId) => axios.get(URL + `/${movieId}`),
    getMovies: (number) => axios.get(URL+ `/nr/${number}`),
}

export default MovieApi;