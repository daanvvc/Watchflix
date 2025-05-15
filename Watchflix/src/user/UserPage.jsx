import CurrentMFA from "./components/CurrentMFA";
import SetupMFA from "./components/SetupMFA";

function UserPage(props) {
    
    return (
        <>
            <li key={props.user?.email}> email: {props.user?.email} </li>
            <li key={props.user?.username}> username: {props.user?.username} </li>
            {props.user?.role === "ADMIN" && <li key="Admin">You're an admin!</li>}
            <SetupMFA supabaseClient={props.supabaseClient}/>
            <br />
            <CurrentMFA supabaseClient={props.supabaseClient}/>
        </>
    );
}

export default UserPage