import { useRef, useState } from 'react';
import MovieFileApi from '../api/MovieFileApi';

function UploadPage() {
    const movieFileInputRef = useRef();
    const [movieFile, setMovieFile] = useState(null);
    const [movieName, setMovieName] = useState('');
    const [outputText, setOutputText] = useState('');

    const handleMovieFile = (event) => {
        const file = event.target.files[0];
        setMovieFile(file);
    };

    const handleUpload = async () => {
        if (movieFile === null) {
            setOutputText("You must upload a file!")
            return;
        }

        if (!movieName.trim()) {
            setOutputText("You must add the movie name")
            return;
        }

        // Make a formdata
        const formData = new FormData();
        formData.append('video', movieFile);
        const movieInformation = {
            name: movieName
        }
        formData.append('movieInformation', JSON.stringify(movieInformation));

        // Upload the movie file
        MovieFileApi.uploadMovieFile(formData)
            .then(() => setOutputText("Movie file upload is being handled!"))
            .catch(() => setOutputText("Something went wrong!"))
    }

    return (
        <>
            {/* Movie file */}
            <button onClick={()=>movieFileInputRef.current.click()}>
                Upload movie file
            </button>
            <input
                type="file"
                accept="video/mp4"
                style={{ display: 'none' }}
                ref={movieFileInputRef}
                onChange={handleMovieFile}
            />
            {movieFile && (
                <>
                    <strong>Movie file:</strong> {movieFile.name}
                </>
            )}
            <br/>

            {/* Movie name */}
            Movie Name: 
            <input
                type="text"
                value={movieName}
                onChange={(e) => setMovieName(e.target.value)}
                placeholder="Enter movie name"
            />
            <br/>

            {/* Upload the movie */}
            <button onClick={handleUpload}>
                Add movie to Watchflix
            </button>

            <br/>
            {outputText}
        </>
    );
}

export default UploadPage