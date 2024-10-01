import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";

const Menu = ({ menuOpen }) => {
    const [showStatsButton, setShowStatsButton] = useState(false);
    const [showReservationsButton, setShowReservationsButton] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const checkAdmin = () => {
            const isAdmin = localStorage.getItem('rol') == 'ROLE_ADMIN';
            setShowStatsButton(isAdmin);
        };

        checkAdmin();
        const interval = setInterval(checkAdmin, 1000);
        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        const checkLoggedIn = () => {
            const loggedIn = localStorage.getItem('loggedIn') != null;
            setShowReservationsButton(loggedIn);
        };

        checkLoggedIn();
        const interval = setInterval(checkLoggedIn, 1000);
        return () => clearInterval(interval);
    }, []);

    // Funcion generada de forma temporal, para poder probar el front
    const simpleLogOut = () => {
        localStorage.removeItem("username");
        localStorage.removeItem("token");
        localStorage.removeItem("id");
        localStorage.removeItem("rol")
        localStorage.removeItem("loggedIn")
    }

  return (
    menuOpen && (
      <div className="menu bg-dark text-white p-3">
          <ul className="list-unstyled">
              <li><a href="/" className="text-white">Inicio</a></li>
              {!showReservationsButton && <li><a href="/login" className="text-white">Iniciar sesión</a></li>}
              {!showReservationsButton && <li><a href="/signup" className="text-white">Registrarse</a></li>}
              {showStatsButton && <li><a href="/createEvent" className="text-white">Crear evento</a></li>}
              {showReservationsButton && <li><a href="/reservations" className="text-white">Ver mis reservas</a></li>}
              {showReservationsButton &&
                  <li><a href="/" className="text-white" onClick={simpleLogOut}>Cerrar sesión</a></li>}
          </ul>
      </div>
    )
  );
};

export default Menu;
