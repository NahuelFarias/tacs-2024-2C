import React, { useState } from 'react';
import PrimaryButton from './PrimaryButton';
import { tryCreateUser } from '../services/userService';
import './Registration.css'
import {useNavigate} from "react-router-dom";

const Registration = () => {
    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ passwordConfirmation, setPasswordConfirmation ] = useState('');
    const [ email, setEmail ] = useState('');
    const [ error, setError ] = useState('');
    const navigate = useNavigate()

    const handlePasswordConfirmationChange = (event) => {
        setPasswordConfirmation(event.target.value)
    }

    const handleUsernameChange = (event) => {
        setUsername(event.target.value)
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value)
    }

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    }

    const checkCredentials = () => {
        if (username.length < 7) {
            setError("El nombre de usuario debe contener mas caracteres")
            return false
        } else if (password.length < 8) {
            setError("La contraseña debe contener mas caracteres")
            return false
        } else if (password !== passwordConfirmation) {
            setError("Las contraseñas ingresadas no coinciden")
            return false
        } else if (!email || !email.includes('@')) {
            setError("Por favor ingrese un email válido")
            return false
        }
        setError("")
        return true
    }

    const handleSubmit = (event) => {
        event.preventDefault()
        const credentialsAreValid = checkCredentials()

        if (credentialsAreValid) {
            const result = tryCreateUser(username, password, email)
            if (result) {
                navigate("/login", { state: { message: 'Registro exitoso! Por favor, ahora inicia sesión.' } })
            }
        }
    }

    return (
        <div className="registration-background d-flex justify-content-center align-items-center" style={{ backgroundColor: '#1a1a1a' }}>
          <div className="registration-box p-5 m-5 rounded shadow">
            <h2 className="text-center text-dark mb-4">Crear una cuenta</h2>
            <form className="d-flex flex-column justify-content-between mt-5 mb-4" style={{ height: '100%' }}>
                <div className="mb-4">
                    <p className="text-dark input-label">
                        Nombre de usuario (al menos 7 caracteres)
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
                        Contraseña (debe contener más de 8 caracteres)
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
                <div className="mb-5">
                    <p className="text-dark input-label">
                        Repeti tu contraseña
                    </p>
                    <input
                        type="password"
                        id="password-confirmation"
                        value={passwordConfirmation}
                        onChange={handlePasswordConfirmationChange}
                        placeholder=""
                        className="form-control"
                    />
                </div>
                <div className="mb-4">
                    <p className="text-dark input-label">
                        Email
                    </p>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={handleEmailChange}
                        placeholder=""
                        className="form-control"
                    />
                </div>
                {error && <h3 className="text-danger text-center"
                              style={{fontSize: '0.85rem', marginBottom: '1rem'}}>{error}</h3>}
              <PrimaryButton onClick={handleSubmit}>Regístrate</PrimaryButton>
            </form>
          </div>
        </div>
      );    
}

export default Registration;
