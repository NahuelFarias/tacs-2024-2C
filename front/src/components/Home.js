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
            {events?.map(event => (
              <EventCard key={event.id} eventId={event.id} title={event.name} imageUrl={event.image_url} />
            ))}
          </div>
        </div>
      </div>
    );
}

export default Home;
