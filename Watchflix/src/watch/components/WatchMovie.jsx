import React, { useEffect, useState } from 'react';
import MovieFileApi from '../../api/MovieFileApi';

const WatchMovie = (props) => {
    const [videoUrl, setVideoUrl] = useState(null);
    const [error, setError] = useState([])

    useEffect(() => {
        MovieFileApi.getMovieFile(props.movieId)
        .then(result => setVideoUrl(result.config.url))
    .catch(error => {
      if (error.message === "Network Error" || error.status === 503) {
        setError("Watchflix isn't working right now")
      }
      console.log(error);
      setVideoUrl(undefined)})      
    }, [])

    return (
        <>
            <p>{error}</p>
            {videoUrl !== null && videoUrl !== undefined ?
                <video controls width="100%" height="auto">
                    <source src={videoUrl} type="video/mp4" />
                </video> 
            :
                videoUrl === null &&
                <>
                    The movie is loading...
                </>
            }
        </>
  );
};

export default WatchMovie;