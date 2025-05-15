import { useEffect, useState } from "react";
import { Auth } from "@supabase/auth-ui-react";
import { ThemeSupa } from "@supabase/auth-ui-shared";
import MFACheck from "./components/MFACheck"; // adjust path if needed

function LoginPage(props) {
  const [mfaChallenge, setMfaChallenge] = useState(null);

  useEffect(() => {
    props.supabaseClient.auth.getSession().then(({ data: { session } }) => {
      props.setSession(session);
    });

    const { data: { subscription } } =
      props.supabaseClient.auth.onAuthStateChange(async (event, session) => {
        if (event === "SIGNED_IN") {
          // List all factors that were added by the user
          const { data: factorsData, error: factorsError } =
            await props.supabaseClient.auth.mfa.listFactors();

          if (factorsError) {
            console.error("Failed to list MFA factors", factorsError);
            props.setSession(session);
            return;
          }

          // Check there is a verified factor
          const verifiedFactor = factorsData?.totp?.find((factor) => factor.status === "verified");

          // If MFA, set MFA, otherwise set the session
          if (verifiedFactor) {
            const { data: challengeData, error: challengeError } =
              await props.supabaseClient.auth.mfa.challenge({
                factorId: verifiedFactor.id,
              });

            if (challengeError) {
              console.error("Challenge error:", challengeError);
              props.setSession(session);
              return;
            }

            setMfaChallenge({
              factorId: verifiedFactor.id,
              challengeId: challengeData.id,
            });
          } else {
            props.setSession(session); 
          }
        }
      });

    return () => subscription.unsubscribe();
  }, []);

  const onVerified = async () => {
    const { data: sessionData } =
      await props.supabaseClient.auth.getSession();
    props.setSession(sessionData.session);
    setMfaChallenge(null);
  }

  return (
    <>
      {!mfaChallenge ? (
        <Auth
          supabaseClient={props.supabaseClient}
          appearance={{ theme: ThemeSupa }}
          providers={["github"]}
        />
      ) : (
        <MFACheck
          supabaseClient={props.supabaseClient}
          factorId={mfaChallenge.factorId}
          challengeId={mfaChallenge.challengeId}
          onVerified={onVerified}
        />
      )}
    </>
  );
}

export default LoginPage;
