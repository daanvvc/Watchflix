import axios from 'axios';

const URL = import.meta.env.VITE_HOST + "/movieFile"
console.log(URL)

const MovieFileApi = {
    getMovieFile: (movieId) => axios.get(URL + `/${movieId}`),
}

export default MovieFileApi;