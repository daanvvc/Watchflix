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

    return (
        <Auth 
          supabaseClient={props.supabaseClient} 
          appearance={{ theme: ThemeSupa }} 
          providers={['github']}
        />
    );
}

export default LoginPage