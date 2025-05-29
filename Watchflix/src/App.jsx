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
import UserApi from './api/UserApi';
import UserPage from './user/UserPage';

function App() {
  const navigate = useNavigate();
  const [session, setSession] = useState(null)
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [isAdmin, setIsAdmin] = useState(null)
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null)

  useEffect(() => {
    supabaseClient.auth.getSession().then(({ data: { session } }) => {
      setSession(session)
      setIsLoggedIn(session !== null)
    });
  }, []); 

  useEffect(() => {
    setIsLoggedIn(session !== null)

    supabaseClient.auth.onAuthStateChange((event, session) => {
      if (event === 'SIGNED_IN' && user === null) {
          UserApi.getUser(session.user.id)
          .then(user => setUser(user.data))
          .catch(async error =>{ 
            if (error.status === 404) {
              const userFormData = new FormData();
              const userInformation = {
                id: session.user.id,
                email: session.user.email
              }
              userFormData.append('user', JSON.stringify(userInformation));

              UserApi.addUser(userFormData)
              .then(() => UserApi.getUser(session.user.id)
              .then(user => setUser(user.data)))
          }
        }) 
      }
  })
  }, [session]);

  useEffect(() => {
    let timeout;

    if (loading) {
      timeout = setTimeout(() => {
        if (!user?.role) {
          console.warn("User role not set after timeout, proceeding anyway");
          setLoading(false);
        }
      }, 500); // 0.5 seconds
    }

    if (user?.role) {
      setIsAdmin(user.role === "ADMIN")
      setLoading(false);
    }

    return () => clearTimeout(timeout);
  }, [user])

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
        {isLoggedIn ? (<Navbar logout={logout} username={user?.username} isAdmin={isAdmin}/>) : "" }
        <Routes>
          <Route path="/" element={isLoggedIn ? <HomePage /> : <LoginPage setSession={setSession} supabaseClient={supabaseClient} />}/>  
          <Route path="/watch/:id" element={isLoggedIn ? <WatchPage /> : <Navigate to="/"/>} />
          <Route path="/upload" element={isLoggedIn && isAdmin ? <UploadPage /> : <Navigate to="/"/>}/>  
          <Route path="/AdminMovies" element={isLoggedIn && isAdmin ? <AdminMoviesPage /> : <Navigate to="/"/>}/>  
          <Route path="/user" element={isLoggedIn ? <UserPage user={user} supabaseClient={supabaseClient} logout={logout}/> : <Navigate to="/"/>}/>  
        </Routes>
      </div>
  )
}

export default App