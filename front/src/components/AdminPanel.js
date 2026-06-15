import React, { useState } from 'react';
import { resetPassword } from '../services/userService';

const AdminPanel = () => {
    const [username, setUsername] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [result, setResult] = useState(null);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setResult(null);
        setError('');
        try {
            const count = await resetPassword(username, newPassword);
            setResult(`Contraseña actualizada para ${count} usuario/s.`);
        } catch (err) {
            setError(err.response?.data?.errorCause || 'Error al resetear contraseña');
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
            <div className="p-5 rounded shadow" style={{ backgroundColor: 'white', width: '90%', maxWidth: '450px' }}>
                <h2 className="text-center text-dark mb-4">Panel de Administración</h2>
                <h5 className="text-dark mb-4">Resetear contraseña de usuario</h5>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <p className="text-dark mb-1">Nombre de usuario</p>
                        <input
                            type="text"
                            className="form-control"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder="Ingrese el username"
                        />
                    </div>
                    <div className="mb-4">
                        <p className="text-dark mb-1">Nueva contraseña</p>
                        <input
                            type="text"
                            className="form-control"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            placeholder="Ingrese la nueva contraseña"
                        />
                    </div>
                    {error && <p className="text-danger text-center mb-3" style={{ fontSize: '0.85rem' }}>{error}</p>}
                    {result && <p className="text-success text-center mb-3">{result}</p>}
                    <button type="submit" className="btn btn-dark w-100">Resetear contraseña</button>
                </form>
            </div>
        </div>
    );
};

export default AdminPanel;
