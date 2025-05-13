import axios from 'axios';

const URL = import.meta.env.VITE_HOST + "/movieFile"
console.log(URL)

const MovieFileApi = {
    getMovieFile: (movieId) => axios.get(URL + `/${movieId}`),
    uploadMovieFile: (movieFile) => axios.post(URL + "/upload", movieFile, { headers: { 'Content-Type': 'multipart/form-data' }}),    
}

export default MovieFileApi;