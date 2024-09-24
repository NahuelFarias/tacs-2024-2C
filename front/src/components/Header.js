import {React, useEffect, useRef} from 'react';
import  { useNavigate } from 'react-router-dom';
import './Header.css';

const Header = ({ toggleMenu }) => {

  const navigate = useNavigate();

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

  return(
  <header className="bg-dark text-white p-3 d-flex justify-content-between align-items-center">
    <div className="logo"  ref={logoRef}>
        <img src="/icono.png" alt="Icono" className="logo-icon" /> {/* Usamos la imagen */}
        <h1 className="text-white ml-3">Events</h1>
    </div>
    <button className="btn btn-outline-light" onClick={toggleMenu}>
      <i className="fa fa-bars"></i> {/* Ícono de menú */}
    </button>
  </header>
  );
};

export default Header;
