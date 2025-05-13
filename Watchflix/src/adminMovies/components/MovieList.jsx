import React, { useState, useEffect } from 'react';
import MovieApi from '../../api/MovieApi';
import { NavLink } from 'react-router-dom';
import { isArray } from 'tls';

const MovieList = () => {
  const [movies, setMovies] = useState([])

  useEffect(() => {
    MovieApi.getAllMovies()
    .then(result => isArray(result.data) ? setMovies(result.data) : setMovies([]))
    .catch(error => {console.log(error); setMovies([])})
  }, [])

  return (
    <ul>
        {movies.map(movie => {
            return (
              <li key={movie.name}>
                  {movie.name}: {movie.uploadStatus}
              </li>
              )
        })}    
    </ul> 
  );
};

export default MovieList;