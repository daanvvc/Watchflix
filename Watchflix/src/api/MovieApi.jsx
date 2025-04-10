import axios from 'axios';

const URL = "http://localhost:8085/movie"

const MovieApi = {
    getMovie: (movieId) => axios.get(URL + `/${movieId}`),
    getMovies: (number) => axios.get(URL+ `/nr/${number}`),
}

export default MovieApi;