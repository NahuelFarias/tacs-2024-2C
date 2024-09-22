import React from 'react';

const Menu = ({ menuOpen }) => {
  return (
    menuOpen && (
      <div className="menu bg-dark text-white p-3">
        <ul className="list-unstyled">
          <li><a href="/login" className="text-white">Iniciar Sesión</a></li>
          <li><a href="/signup" className="text-white">Registrarse</a></li>
        </ul>
      </div>
    )
  );
};

export default Menu;
