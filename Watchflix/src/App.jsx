import './App.css'
import HomePage from './home/HomePage'
import LoginPage from './login/LoginPage';
import { Route, Routes, useNavigate } from "react-router-dom";
import { useState } from 'react';
import supabaseClient from './auth/supabaseClient';
import Navbar from './navbar/Navbar';
import WatchPage from './watch/WatchPage';

function App() {
  const navigate = useNavigate();
  const [session, setSession] = useState(null)

  const isLoggedIn = () => {
    return session !== null
  } 

  async function logout() {
    await supabaseClient.auth.signOut()
    setSession(null)
    navigate('/');
  }

  return (
    <div className="App">
        {!isLoggedIn() ?
          (
            <Routes>
              <Route path="/" element={<LoginPage setSession={setSession} supabaseClient={supabaseClient} />} />
            </Routes>
          ) 
          : 
          (
            <>
            <Navbar logout={logout} email={session?.user.email}/>
            <Routes>
              <Route path="/" element={<HomePage />} />  
              <Route path="watch">
                <Route path=":id" element={<WatchPage />} />    
              </Route>
            </Routes>
            </>
          )
        }
    </div>
  )
}

export default App
