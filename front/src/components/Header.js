import React from 'react';
import './Header.css';

const Header = ({ toggleMenu }) => (
  <header className="bg-dark text-white p-3 d-flex justify-content-between align-items-center">
    <div className="logo">
        <img src="/icono.png" alt="Icono" className="logo-icon" /> {/* Usamos la imagen */}
        <h1 className="text-white ml-3">Events</h1>
    </div>
    <button className="btn btn-outline-light" onClick={toggleMenu}>
      <i className="fa fa-bars"></i> {/* Ícono de menú */}
    </button>
  </header>
);

export default Header;
