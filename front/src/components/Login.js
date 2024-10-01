import React, { useState } from 'react';
import {getSalt, hashPassword, tryAuthenticate} from '../services/authService';
import {useLocation, useNavigate} from 'react-router-dom'
import PrimaryButton from './PrimaryButton';
import './Login.css'

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate()

    const location = useLocation();
    const [message, setMessage] = useState(location.state?.message || '');

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
        setError('')
        setMessage('')

        let salt;
        const saltResult = await getSalt(username);
        if(!saltResult.success) {
            console.log(saltResult.message)
            setError(saltResult.message)
        }
        else {
            salt = saltResult.salt
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
    }

    return (
        <div className="login-background d-flex justify-content-center align-items-center">
            <div className="login-box p-5 m-5 rounded shadow">
                <h2 className="text-center text-dark mb-5">Bienvenido! </h2>
                {message && <div className="alert alert-success">{message}</div>}
                {error && <div className="alert alert-danger">{error}</div>}
                <form className="d-flex flex-column justify-content-between mt-4" style={{height: '100%'}}>
                    <div className="mb-4 mt-4">
                        <p className="text-dark input-label">
                            Nombre de usuario
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
                    <PrimaryButton onClick={handleSubmit}>Iniciar sesión</PrimaryButton>
                </form>
                <p className="text-center text-dark mt-3">
                    No tenés cuenta? <span className="register-link" onClick={redirectToRegister}>Regístrate</span>
                </p>
            </div>
        </div>
    );
}

export default Login;
