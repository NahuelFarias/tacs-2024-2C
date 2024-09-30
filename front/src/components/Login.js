import React, { useState } from 'react';
import {getSalt, hashPassword, tryAuthenticate} from '../services/authService';
import { useNavigate } from 'react-router-dom'
import PrimaryButton from './PrimaryButton';
import './Login.css'

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate()

    const handleUsernameChange = (event) => {
        setUsername(event.target.value)
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value)
    }

    const redirectToRegister = () => {
        navigate('/signup');
    };

    const handleSubmit = async (event) => {
        event.preventDefault()

        const salt = await getSalt(username);
        const hashedPassword = hashPassword(password, salt)

        const result = await tryAuthenticate(username, hashedPassword)
        if(!result.success) {
            console.log(result.message)
            setError(result.message)
        }
        else {
            console.log("OK")
            navigate("/")
        }
    }

    return (
        <div className="login-background d-flex justify-content-center align-items-center">
            <div className="login-box p-5 m-5 rounded shadow">
                <h2 className="text-center text-dark mb-5">Bienvenido! </h2>
                <form className="d-flex flex-column justify-content-between mt-5" style={{height: '100%'}}>
                    <div className="mb-4 mt-4">
                        <p className="text-dark input-label">
                            Nombre de Usuario
                        </p>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={handleUsernameChange}
                            placeholder=""
                            className="form-control"
                        />
                    </div>
                    <div className="mb-4">
                        <p className="text-dark input-label">
                            Contraseña
                        </p>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={handlePasswordChange}
                            placeholder=""
                            className="form-control"
                        />
                    </div>
                    {error && <h3 className="text-danger text-center"
                                  style={{fontSize: '0.85rem', marginBottom: '1rem'}}>{error}</h3>}
                    <PrimaryButton onClick={handleSubmit}>Iniciar Sesion</PrimaryButton>
                </form>
                <p className="text-center text-dark mt-3">
                    No tenés cuenta? <span className="register-link" onClick={redirectToRegister}>Registrate</span>
                </p>
            </div>
        </div>
    );
}

export default Login;
