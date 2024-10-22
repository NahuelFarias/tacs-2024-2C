import React, { useEffect, useState } from 'react';
import EventCard from '../components/EventCard';
import ErrorMessage from './ErrorMessage';
import { getEvents } from '../services/eventService';

const Home = () => {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([])
  const [searchTerm, setSearchTerm] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    getEvents()
      .then(data => {
        setEvents(data);
        setFilteredEvents(data);
      })
      .catch(error => {
        console.error('Error fetching events:', error);
        setEvents([])
      });
  }, []);

  const handleSearch = (inputValue) => {
    setSearchTerm(inputValue);

    let eventsFiltered = events.filter(event => 
      (event.name.toLowerCase().includes(inputValue.toLowerCase())))

    setFilteredEvents(eventsFiltered)

    if(eventsFiltered.length != 0){
      setError('');
    }
    else {
      setError('No hay eventos que coincidan con los terminos de busqueda');
    }
  };

  const handleCloseError = () => {
    setFilteredEvents(events);
    setSearchTerm('');
    setError('');
  };

  return (
      <div className="d-flex justify-content-center">
        <div className='w-75'>

          <div className="search-bar p-3">
            <div className="input-group" style={{ maxWidth: "600px", margin: "0 auto" }}>
              <input 
                type="text" 
                className="form-control" 
                placeholder="Buscar eventos o artistas..." 
                value={searchTerm}
                onChange={(e) => {
                  handleSearch(e.target.value)
                }} 
              />
            </div>
          </div> 
    
          {error && <ErrorMessage message={error} onClose={handleCloseError}/>}

          {!error && 
            <div className="events-section container">
              <h2 className="text-white">Eventos Destacados</h2>
              <div className="row">
                {filteredEvents?.map(event => (
                  <EventCard key={event.id} eventId={event.id} title={event.name} imageUrl={event.image_url} />
                ))}
              </div>
            </div>
          }
        </div>
      </div>
    );
}

export default Home;
