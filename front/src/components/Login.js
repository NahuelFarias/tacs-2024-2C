import React, { useState } from 'react';
import { tryAuthenticate } from '../services/authService';
import { useNavigate } from 'react-router-dom'
import PrimaryButton from './PrimaryButton';

const Login = () => {
    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ error, setError ] = useState('');
    const navigate = useNavigate()


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
        }
        setError("")
        return true
    } 

    const handleSubmit = (event) => {
        event.preventDefault()

        const credentialsAreValid = checkCredentials()

        if (credentialsAreValid) {
            if (tryAuthenticate(username, password)) {
                navigate("/")
            }
        }
    }

    return (
        <div className="d-flex justify-content-center align-items-center vh-100" style={{ backgroundColor: '#1a1a1a' }}>
          <div className="p-5 rounded shadow" style={{ backgroundColor: 'white', width: '90%', maxWidth: '500px', height: 'auto' }}>
            <h2 className="text-center text-dark mb-4">Login</h2>
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
              {error && <h3 className="text-danger text-center" style={{ fontSize: '0.85rem', marginBottom: '1rem' }}>{error}</h3>}
              <PrimaryButton onClick={handleSubmit}>Login</PrimaryButton>
              {/* <button type="button" className="btn" style={{ backgroundColor: '#ff4f92', color: 'white' }} onClick={handleSubmit}>Login</button> */}
            </form>
          </div>
        </div>
      );    
}

export default Login;