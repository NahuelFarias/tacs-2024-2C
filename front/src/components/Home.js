import React, { useEffect, useState } from 'react';
import SearchBar from '../components/SearchBar';
import EventCard from '../components/EventCard';
import { getEvents } from '../services/eventService';

const Home = () => {
  const [events, setEvents] = useState([]);  // Estado para los eventos

  useEffect(() => {
    getEvents()
      .then(data => {
        setEvents(data);
      })
      .catch(error => {
        console.error('Error fetching events:', error);
        setEvents([])
      });
  }, []);

  return (
      <div className="home-page">
        <SearchBar />  
  
        <div className="events-section container">
          <h2 className="text-white">Eventos Destacados</h2>
          <div className="row">
            {events?.map(evento => (
              <EventCard key={evento.id} title={evento.nombre} />
            ))}
          </div>
        </div>
      </div>
    );
}

export default Home;
