import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import Menu from './components/Menu';
import SearchBar from './components/SearchBar';
import EventCard from './components/EventCard';
import Footer from './components/Footer';
import { getEvents } from './services/eventService';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  const [menuOpen, setMenuOpen] = useState(false);
  const [eventos, setEventos] = useState([]);  // Estado para los eventos

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  useEffect(() => {
    getEvents()
      .then(data => {
        setEventos(data);
      })
      .catch(error => console.error('Error fetching events:', error));
  }, []);


  return (
    <div className="d-flex flex-column min-vh-100">
      {/* Header */}
      <Header toggleMenu={toggleMenu} />

      {/* Menu */}
      <Menu menuOpen={menuOpen} />

      {/* Search bar */}
      <SearchBar />

      {/* Eventos */}
      <div className="events-section container">
        <h2 className="text-white">Eventos Destacados</h2>
        <div className="row">
           {/* Mapeo de los eventos obtenidos desde el backend */}
           {eventos.map(evento => (
            <EventCard key={evento.id} title={evento.nombre} />
          ))}
        </div>
      </div>

      {/* Footer */}
      <Footer />
    </div>
  );
}

export default App;
