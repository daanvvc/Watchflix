import React from 'react';
import './Navbar.css'
import { NavLink } from 'react-router-dom';

const Navbar = (props) => {
  return (
    <div className="navbar">
        Welcome, {props.username}
        {" "}
        <NavLink to={"/"}>
          home          
        </NavLink>
        {" "}
        <NavLink to={"/user"}>
          {props.username}
        </NavLink>
        {" "}
        <NavLink to={"/upload"}>
          upload          
        </NavLink>
        {" "}
        <NavLink to={"/adminMovies"}>
          adminMovies          
        </NavLink>
        {" "}
        <button onClick={() => props.logout()}>
            Logout
        </button>
    </div>
  );
};

export default Navbar;