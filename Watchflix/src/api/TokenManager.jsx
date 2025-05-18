import supabaseClient from "../auth/supabaseClient";


const TokenManager = {
    getAccessToken: () => {
        return JSON.parse(localStorage.getItem("sb-dncoimjidifbzrifcvsk-auth-token")).access_token;
    },
}

export default TokenManager;