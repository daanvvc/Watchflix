import { useState } from 'react';

function MFACheck({ supabaseClient, factorId, challengeId, onVerified }) {
  const [code, setCode] = useState('');
  const [error, setError] = useState('');

  const handleVerify = async () => {
    supabaseClient.auth.mfa.verify({
      factorId,
      challengeId,
      code,
    })
    .then ((result) => {
      if (result.error) {
        console.error('MFA verify error', error);
        setError('Invalid code. Try again.');
        return;
      }
      onVerified(); 
    })
    .catch(error => {
        console.error('MFA verify error', error);
        setError('Invalid code. Try again.');
    })
  };

  return (
    <div>
      <h3>Enter your TOTP code</h3>
      <input
        type="text"
        value={code}
        onChange={(e) => setCode(e.target.value)}
        placeholder="123456"
      />
      <button onClick={handleVerify}>Verify</button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}

export default MFACheck;
