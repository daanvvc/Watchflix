import { useEffect } from "react";
import { Auth } from '@supabase/auth-ui-react'
import { ThemeSupa } from '@supabase/auth-ui-shared'

function LoginPage(props) {
    useEffect(() => {
        props.supabaseClient.auth.getSession().then(({ data: { session } }) => {
        props.setSession(session)
      })
      const { data: { subscription } } = props.supabaseClient.auth.onAuthStateChange((_event, session) => {
      try {
        console.log("Auth state changed");
        props.setSession(session);
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
        <Auth supabaseClient={props.supabaseClient} appearance={{ theme: ThemeSupa }} onSignUp={(error) => handleSignUpError(error)} />
    );
}

export default LoginPage