import MovieFileApi from '../api/MovieFileApi';
import mockMovie from '../assets/mockMovie2.mp4'; 

function UploadPage() {
    console.log(mockMovie)

    const handleUpload = async () => {
        const response = await fetch(mockMovie);
        const blob = await response.blob();

        const file = new File([blob], 'video.mp4', { type: 'video/mp4' });
        const formData = new FormData();
        formData.append('video', file);

        MovieFileApi.uploadMovieFile(formData)
    }

    return (
        <button onClick={() => handleUpload()}>
            upload
        </button>
    );
}

export default UploadPage