import WatchMovie from './components/WatchMovie'
import { useParams } from 'react-router-dom';

function WatchPage() {
    const { id } = useParams();

    return (
        <WatchMovie movieId={id} />
    );
}

export default WatchPage