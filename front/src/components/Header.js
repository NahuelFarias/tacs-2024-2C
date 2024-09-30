import {React, useEffect, useRef, useState} from 'react';
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

    const redirectToHome = () => {
        navigate(`/`);
    };

    let logoRef = useRef();
    useEffect(() => {
        let closeReservationModal = (e) => {
            if(logoRef.current.contains(e.target)){
                redirectToHome();
            }
        };
        document.addEventListener("mousedown", closeReservationModal);
        return() =>{
            document.removeEventListener("mousedown", closeReservationModal);
        }
    });

    return (
    <header className="bg-dark text-white p-3 d-flex justify-content-between align-items-center">
        <div className="logo">
            <button className="btn btn-outline-light col-auto" onClick={toggleMenu}>
                <i className="fa fa-bars"></i>
            </button>
            <div className="col-1"></div>

            <div className="logo" ref={logoRef}>
                <img src="/icono.png" alt="Icono" className="logo-icon col-auto btn-outline-light"/>
                <h1 className="text-white ml-3" ref={logoRef}>Events</h1>
            </div>

        </div>
        <div className="button-container">
            {showStatsButton && <button className="btn col-auto btn-outline-light" onClick={redirectToStats}>
            <i className="fa-solid fa-chart-simple"></i> Ir a Estadisticas
            </button> }
        </div>
    </header>
    )
};

export default Header;
