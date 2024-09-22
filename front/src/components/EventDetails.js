import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import Menu from './components/Menu';
import SearchBar from './components/SearchBar';
import EventCard from './components/EventCard';
import ZoneCard from './components/ZoneCard';
import Footer from './components/Footer';
import { getEvents, getEvent } from './services/eventService';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  const [menuOpen, setMenuOpen] = useState(false);
  const [eventos, setEventos] = useState([]);  // Estado para los eventos
  const eventName = "Nombre de Evento";
  const eventLocation = "Grand Rex";
  const eventDate = "20/12/2024 21:00Hs";
  const eventAdmin = "Gobierno de la Cuidad de Buenos Aires";
  const popular = {
    id:2,
    zoneLocation : 'popular',
    availableTickets : 15
  }
  const platea = {
    id: 1,
    zoneLocation : 'platea',
    availableTickets : 8
  }

  const event = {
    id: 1,
    name: eventName,
    location: eventLocation,
    date: eventDate,
    admin: eventAdmin,
    zones: [platea, popular]
  }

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  useEffect(() => {
    // getEvent(1)
    //   .then(data => {
    //     eventName = data.eventName;
    //     eventLocation = data.eventLocation;
    //     eventDate = data.eventDate;
    //     eventAdmin = data.eventAdmin;
    //   })
    //   .catch(error => console.error('Error fetching event detail:', error))

    getEvents()
      .then(data => {
        setEventos(data);
      })
      .catch(error => console.error('Error fetching events:', error));
  }, []);



  // VISTA GENERAL
  // return (
  //   <div className="d-flex flex-column min-vh-100">
  //     {/* Header */}
  //     <Header toggleMenu={toggleMenu} />

  //     {/* Menu */}
  //     <Menu menuOpen={menuOpen} />

  //     {/* Search bar */}
  //     <SearchBar />

  //     {/* Eventos */}
  //     <div className="events-section container">
  //       <h2 className="text-white">Eventos Destacados</h2>
  //       <div className="row">
  //          {/* Mapeo de los eventos obtenidos desde el backend */}
  //          {eventos.map(evento => (
  //           <EventCard key={evento.id} title={evento.nombre} />
  //         ))}
  //       </div>
  //     </div>

  //     {/* Footer */}
  //     <Footer />
  //   </div>
  // );


  
//   @GetMapping()
//   public Event getEvent(int eventId) {
//       return eventService.getEvent(eventId);
//   }

  return (
    <div className="d-flex flex-column min-vh-100">
      {/* Header */}
      <Header toggleMenu={toggleMenu} />

      {/* Menu */}
      <Menu menuOpen={menuOpen} />
      <div class="p-5 w-100">
        <h1 class="mb-2">{event.name}</h1>

        <img class="w-100" src="/evento.jpg" alt="Foto del evento"></img>

        <div class="d-flex justify-content-between mt-2 w-100">
          <h2>{event.location} - {event.date}</h2>

          <h3> {event.admin} </h3>
        </div>

        <div class="mt-2">
          Asientos disponibles
          <div class='d-flex'>
          {event.zones.map(zone => (
              <ZoneCard key={zone.id} zoneLocation={zone.zoneLocation}  availableTickets={zone.availableTickets} /> 
          ))}

          </div>
        </div>
      </div>

      {/* Footer */}
      <Footer />
    </div>
  );
}

export default App;
