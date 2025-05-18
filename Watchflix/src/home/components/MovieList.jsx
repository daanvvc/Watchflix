import React, { useState, useEffect } from 'react';
import MovieApi from '../../api/MovieApi';
import { NavLink } from 'react-router-dom';
import { isArray } from 'tls';

const MovieList = () => {
  const [movies, setMovies] = useState([])
  const [error, setError] = useState([])

  useEffect(() => {
    MovieApi.getMovies(10)
    .then(result => isArray(result.data) ? setMovies(result.data) : setMovies([]))
    .catch(error => {
      if (error.message = "Network Error" || error.status === 503) {
        setError("Watchflix isn't working right now")
      }
      console.log(error);
      setMovies([])})
  }, [])

  return (
    <>
      <p>{error}</p>
      <ul>
        {movies.map(movie => {
            return (
              <li key={movie.name}>
                <NavLink data-testid={movie.name} to={`/watch/${movie.id}`}>
                  {movie.name} 
                </NavLink>
              </li>)
        })}    
      </ul>
    </>
  );
};

export default MovieList;