import './App.css'
import HomePage from './home/HomePage'
import LoginPage from './login/LoginPage';
import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import { useEffect, useState } from 'react';
import supabaseClient from './auth/supabaseClient';
import Navbar from './navbar/Navbar';
import WatchPage from './watch/WatchPage';
import UploadPage from './upload/UploadPage';
import AdminMoviesPage from './adminMovies/AdminMoviesPage';

function App() {
  const navigate = useNavigate();
  const [session, setSession] = useState(null)
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    supabaseClient.auth.getSession().then(({ data: { session } }) => {
      setSession(session)
      setIsLoggedIn(session !== null)
      setLoading(false);
    });
  }, []);

  useEffect(() => {
    setIsLoggedIn(session !== null)
  }, [session]);

  async function logout() {
    await supabaseClient.auth.signOut()
    setSession(null)
    navigate('/');
  }

  return (
    loading ?
      <div>Loading...</div>
    :
      <div className="App">
        {isLoggedIn ? (<Navbar logout={logout} email={session?.user.email}/>) : "" }
        <Routes>
          <Route path="/" element={isLoggedIn ? <HomePage /> : <LoginPage setSession={setSession} supabaseClient={supabaseClient} />}/>  
          <Route path="/watch/:id" element={isLoggedIn ? <WatchPage /> : <Navigate to="/"/>} />
          <Route path="/upload" element={isLoggedIn ? <UploadPage /> : <Navigate to="/"/>}/>  
          <Route path="/AdminMovies" element={isLoggedIn ? <AdminMoviesPage /> : <Navigate to="/"/>}/>  
        </Routes>
      </div>
  )
}

export default App