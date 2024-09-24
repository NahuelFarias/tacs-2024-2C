import React, {useEffect, useState} from 'react';

const Menu = ({ menuOpen }) => {
    const [showStatsButton, setShowStatsButton] = useState(false);

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
    menuOpen && (
      <div className="menu bg-dark text-white p-3">
          <ul className="list-unstyled">
              <li><a href="/login" className="text-white">Iniciar Sesión</a></li>
              <li><a href="/signup" className="text-white">Registrarse</a></li>
              {showStatsButton && <li><a href="/createEvent" className="text-white">Crear evento</a></li>}
              <li><a href="/reservations" className="text-white">Ver Mis Reservas</a></li>
          </ul>
      </div>
    )
  );
};

export default Menu;
