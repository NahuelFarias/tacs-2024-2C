import React, {useEffect, useState} from 'react';
import './Header.css';
import {useNavigate} from "react-router-dom";

const Header = ({toggleMenu}) => {
    const navigate = useNavigate();
    const [showStatsButton, setShowStatsButton] = useState(false);

    const redirectToStats = () => {
        navigate('/stats');
    };

    useEffect(() => {
        const checkAdmin = () => {
            const isAdmin = localStorage.getItem('rol') == 'ROLE_ADMIN';
            setShowStatsButton(isAdmin);
        };

        checkAdmin();
        const interval = setInterval(checkAdmin, 1000);
        return () => clearInterval(interval);
    }, []);

    return (
    <header className="bg-dark text-white p-3 d-flex justify-content-between align-items-center">
        <div className="logo">
            <img src="/icono.png" alt="Icono" className="logo-icon"/> {/* Usamos la imagen */}
            <h1 className="text-white ml-3">Events</h1>
        </div>
        <div className="button-container">
            {showStatsButton && <button className="btn col-auto btn-outline-light" onClick={redirectToStats}>
                <i className="fa-solid fa-chart-simple"></i> Ir a Estadisticas
            </button> }
            <button className="btn btn-outline-light" onClick={toggleMenu}>
                <i className="fa fa-bars"></i> {/* Ícono de menú */}
            </button>
        </div>
    </header>
    )
};

export default Header;
