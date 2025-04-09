import React, { useEffect, useState } from 'react';
import MovieFileApi from '../../api/MovieFileApi';

const WatchMovie = (props) => {
    const [videoUrl, setVideoUrl] = useState(null);

    useEffect(() => {
        MovieFileApi.getMovieFile(props.movieId)
        .then(result => setVideoUrl(result.config.url))
        .catch(error => {console.log(error); setVideoUrl(null)})
      }, [])    

    return (
        videoUrl !== null ?
        <video controls width="100%" height="auto">
            <source src={videoUrl} type="video/mp4" />
        </video> 
        :
        <>
            The movie is loading...
        </>
  );
};

export default WatchMovie;