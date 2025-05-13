import { useEffect } from "react";
import { Auth } from '@supabase/auth-ui-react'
import { ThemeSupa } from '@supabase/auth-ui-shared'
import { useNavigate } from "react-router-dom";

function LoginPage(props) {
    const navigate = useNavigate();

    useEffect(() => {
        props.supabaseClient.auth.getSession().then(({ data: { session } }) => {
        props.setSession(session)
        if (session) {
          navigate('/dashboard'); 
        }
      })
      const { data: { subscription } } = props.supabaseClient.auth.onAuthStateChange((_event, session) => {
      try {
        console.log("Auth state changed");
        props.setSession(session);
        if (session) {
          navigate('/dashboard'); 
        }
      } catch (error) {
        console.error('Error handling auth state change:', error);
      }
    });
      return () => subscription.unsubscribe()
    }, [])

    // Doesn't work?
    const handleSignUpError = (error) => {
      if (error.message) {
        console.log(error.message);
      } else {
        console.log('An unexpected error occurred during sign-up.');
      }
    };

    return (
        <Auth 
          supabaseClient={props.supabaseClient} 
          appearance={{ theme: ThemeSupa }} 
          providers={['github']}
          onSignUp={(error) => handleSignUpError(error)} 
        />
    );
}

export default LoginPage