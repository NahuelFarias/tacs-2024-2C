import React, { useState } from 'react';
import PrimaryButton from './PrimaryButton';
import { tryCreateUser } from '../services/userService';

const Registration = () => {
    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ passwordConfirmation, setPasswordConfirmation ] = useState('');
    const [ error, setError ] = useState('');

    const handlePasswordConfirmationChange = (event) => {
        setPasswordConfirmation(event.target.value)
    }

    const handleUsernameChange = (event) => {
        setUsername(event.target.value)
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value)
    }

    const checkCredentials = () => {
        if (username.length < 3) {
            setError("username is not valid")
            return false
        } else if (password.length < 8) {
            setError("password is not valid")
            return false
        } else if (password !== passwordConfirmation) {
            setError("password and confirmation don't match")
            return false
        }
        setError("")
        return true
    } 

    const handleSubmit = (event) => {
        event.preventDefault()
        //console.log(`${username} - ${password}`)

        const credentialsAreValid = checkCredentials()

        if (credentialsAreValid) {
            tryCreateUser(username, password)
        }
    }

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ backgroundColor: '#1a1a1a' }}>
          <div className="p-5 m-5 rounded shadow" style={{ backgroundColor: 'white', width: '90%', maxWidth: '500px', height: 'auto' }}>
            <h2 className="text-center text-dark mb-4">Sign Up</h2>
            <form className="d-flex flex-column justify-content-between" style={{ height: '100%' }}>
              <div className="mb-4">
                <input
                  type="text"
                  id="username"
                  value={username}
                  onChange={handleUsernameChange}
                  placeholder="Your Username"
                  className="form-control"
                />
              </div>
              <div className="mb-4">
                <input
                  type="password"
                  id="password"
                  value={password}
                  onChange={handlePasswordChange}
                  placeholder="Your Password"
                  className="form-control"
                />
              </div>
              <div className="mb-4">
                <input
                  type="password"
                  id="password-confirmation"
                  value={passwordConfirmation}
                  onChange={handlePasswordConfirmationChange}
                  placeholder="Confirm Your Password"
                  className="form-control"
                />
              </div>
              {error && <h3 className="text-danger text-center" style={{ fontSize: '0.85rem', marginBottom: '1rem' }}>{error}</h3>}
              <PrimaryButton onClick={handleSubmit}>Register</PrimaryButton>
            </form>
          </div>
        </div>
      );    
}

export default Registration;