import { useState } from "react";

function SetupMFA(props) {
  const [qrCode, setQrCode] = useState(null);
  const [factorId, setFactorId] = useState(null);
  const [code, setCode] = useState("");
  const [status, setStatus] = useState("");

  const handleEnroll = () => {
    props.supabaseClient.auth.mfa.enroll({
      factorType: 'totp',
      friendlyName: 'authenticator-app',
    })
    .then(result => {
        setQrCode(result.data.totp.qr_code);
        setFactorId(result.data.id);
        setStatus("Scan the QR code and enter the 6-digit code from your app.");
    })
    .catch(error => {
        console.error("Enroll error:", error);
        setStatus("Failed to enroll MFA.");
    })
  };

  const handleVerify = () => {
    props.supabaseClient.auth.mfa.challengeAndVerify({
      factorId,
      code,
    }).
    then((result) => {
        console.log(result)
        setStatus("MFA setup complete!");
    })
    .catch(error => {
        console.error("Verify error:", error);
        setStatus("Invalid code. Try again.");
    })
  };

  return (
    <>
      <button onClick={handleEnroll}>Set up MFA</button>

      {qrCode && (
        <div>
          <p>{status}</p>
          <img src={qrCode} alt="Scan this QR code" />
          <input
            type="text"
            placeholder="Enter 6-digit code"
            value={code}
            onChange={(e) => setCode(e.target.value)}
          />
          <button onClick={handleVerify}>Verify Code</button>
        </div>
      )}

      {status && !qrCode && <p>{status}</p>}
    </>
  );
}

export default SetupMFA;