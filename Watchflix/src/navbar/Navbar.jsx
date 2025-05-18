import './Navbar.css'
import { NavLink } from 'react-router-dom';

const Navbar = (props) => {
  console.log(props.isAdmin)
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
        {props.isAdmin && 
        <>
          <NavLink to={"/upload"}>
            upload          
          </NavLink>
          {" "}
          <NavLink to={"/adminMovies"}>
            adminMovies          
          </NavLink>
          {" "}
        </>
        }
        <button onClick={() => props.logout()}>
            Logout
        </button>
    </div>
  );
};

export default Navbar;