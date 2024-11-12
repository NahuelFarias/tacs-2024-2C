import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from "react-router-dom";
import { motion } from 'framer-motion';
import Cookies from "js-cookie";

const Menu = ({ menuOpen }) => {
    const [showStatsButton, setShowStatsButton] = useState(false);
    const [showReservationsButton, setShowReservationsButton] = useState(false);

    useEffect(() => {
        const checkAdmin = () => {
            const isAdmin = localStorage.getItem('rol') === 'ADMIN';
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
        localStorage.removeItem('rol')

        Cookies.remove('Token');

        localStorage.removeItem("loggedIn")
    }

  return (
      menuOpen && (
          <motion.div
              className="menu bg-dark text-white p-3"
              initial={{ height: 0, opacity: 0 }}
              animate={{ height: 'auto', opacity: 1 }}
              exit={{ height: 0, opacity: 0 }}
              transition={{ duration: 0.2 }}
          >
              <motion.ul
                  className="list-unstyled text-end"
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ delay: 0.3, duration: 0.5 }}
              >
                  {!showReservationsButton && <li><Link to="/login" className="text-white">Iniciar Sesión</Link></li>}
                  {!showReservationsButton && <li><Link to="/signup" className="text-white">Registrarse</Link></li>}
                  {showStatsButton && <li><Link to="/createEvent" className="text-white">Crear evento</Link></li>}
                  {showReservationsButton && <li><Link to="/reservations" className="text-white">Ver Mis Reservas</Link></li>}
                  {showReservationsButton &&
                      <li><Link to="/" className="text-white" onClick={simpleLogOut}>Cerrar Sesion</Link></li>}
              </motion.ul>
          </motion.div>
      )
  );
};

export default Menu;
