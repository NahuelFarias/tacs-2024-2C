import React, { useState } from 'react';
import Header from './components/Header';
import Menu from './components/Menu';
import SearchBar from './components/SearchBar';
import EventCard from './components/EventCard';
import Footer from './components/Footer';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

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
          {/* Aquí agregarías un mapeo de tus eventos */}
          <EventCard title="Evento 1" />
          <EventCard title="Evento 2" />
          <EventCard title="Evento 3" />
        </div>
      </div>

      {/* Footer */}
      <Footer />
    </div>
  );
}

export default App;
