import UserApi from "../api/UserApi";
import CurrentMFA from "./components/CurrentMFA";
import SetupMFA from "./components/SetupMFA";
import { useState } from "react";

function UserPage(props) {
  const [outputText, setOutputText] = useState("");

  const getRightOfAccess = () => {
    UserApi.getRightOfAccess(props.user?.id)
      .then((response) => {
        const blob = response.data;

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "gdpr-user-data.zip";
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
      })
      .catch((error) => {
        console.error(error);
        setOutputText("Something went wrong!");
      });
  };

  const deleteThisAccount = () => {
    const confirmed = window.confirm("Are you sure you want to delete your account? This action cannot be undone.");
    if (!confirmed) return;

    UserApi.deleteUser(props.user?.id)
      .then((response) => {
        if (response.status === 204 || response.status === 200) {
          
          window.alert("Your deletion request is being handled. You will be logged out when clicking on the ok button." +
             "Please try to login in 10 minutes to see if the deletion was successful");

          props.logout();
        } else {
          setOutputText("Failed to delete your account. Please try again.");
        }
      })
      .catch((error) => {
        console.error(error);
        if (error.response && error.response.data) {
          setOutputText("Error: " + error.response.data.message || error.message);
        } else {
          setOutputText("Something went wrong while deleting your account.");
        }
      });
  };

  return (
    <>
      <button onClick={getRightOfAccess}>Get your information (zip)</button>
      <button onClick={deleteThisAccount}>Delete your account</button>

      {outputText && <p style={{ color: "red" }}>{outputText}</p>}

      <li key={props.user?.email}>email: {props.user?.email}</li>
      <li key={props.user?.username}>username: {props.user?.username}</li>
      {props.user?.role === "ADMIN" && <li key="Admin">You're an admin!</li>}
      <SetupMFA supabaseClient={props.supabaseClient} />
      <br />
      <CurrentMFA supabaseClient={props.supabaseClient} />
    </>
  );
}

export default UserPage;
