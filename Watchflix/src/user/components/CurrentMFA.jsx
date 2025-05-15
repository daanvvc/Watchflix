import { useEffect, useState } from "react"

function CurrentMFA(props) {
  const [factors, setFactors] = useState([])

  useEffect(() => {
    props.supabaseClient.auth.mfa.listFactors()
    .then(result => {
      setFactors([...result.data.totp, ...result.data.phone])
    })
    .catch(error => {
        console.log(error)
    })
  }, [])

  const handleUnenroll = async (factorId) => {
    console.log(factorId)
    props.supabaseClient.auth.mfa.unenroll({ factorId })
    .then(() => {
        // Remove the factor from the list
        setFactors((prev) => prev.filter((f) => f.id !== factorId));
    })
    .catch(error => {
        console.log(error)
    })
  };

  return (
    <table>
        <tbody>
        <tr>
          <td>Factor ID</td>
          <td>Friendly Name</td>
          <td>Factor Status</td>
          <td>Phone Number</td>
        </tr>
        {factors.map((factor) => (
          <tr key={factor.id}>
            <td>{factor.id}</td>
            <td>{factor.friendly_name}</td>
            <td>{factor.factor_type}</td>
            <td>{factor.status}</td>
            <td>{factor.phone}</td>
            <td> <button onClick={() => handleUnenroll(factor.id)}>Unenroll</button> </td>          
        </tr>
        ))}
      </tbody>
    </table> 
  )
}

export default CurrentMFA;