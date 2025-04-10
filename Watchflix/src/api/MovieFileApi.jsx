import axios from 'axios';

const URL = "http://localhost:8085/movieFile"

const MovieFileApi = {
    getMovieFile: (movieId) => axios.get(URL + `/${movieId}`),
}

export default MovieFileApi;