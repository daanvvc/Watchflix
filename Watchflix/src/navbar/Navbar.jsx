import React from 'react';
import './Navbar.css'

const Navbar = (props) => {
  return (
    <div className="navbar">
        Welcome, {props.email}
        <button onClick={() => props.logout()}>
            Logout
        </button>
    </div>
  );
};

export default Navbar;