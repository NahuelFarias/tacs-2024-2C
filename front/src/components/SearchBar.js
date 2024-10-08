import React, { useState } from 'react';
import FoundEvent from './FoundEvent';
import ErrorMessage from './ErrorMessage';
import { getEventByName } from '../services/eventService';

const SearchBar = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [error, setError] = useState('');
  const [foundEvent, setFoundEvent] = useState(null);

  const handleSearch = () => {
    getEventByName(searchTerm.toLowerCase())
      .then(event => {
        setFoundEvent(event);
        setError('');
      })
      .catch(() => {
        setFoundEvent(null);
        setError('Evento no encontrado');
      })
  };

  const handleCloseError = () => {
    setError('');
  };

  const handleCloseEvent = () => {
    setFoundEvent(null);
  };

  return (
    <div className="search-bar p-3">
      <div className="input-group" style={{ maxWidth: "600px", margin: "0 auto" }}>
        <input 
          type="text" 
          className="form-control" 
          placeholder="Buscar eventos o artistas..." 
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)} 
        />
        <span className="input-group-text" style={{ cursor: 'pointer' }} onClick={handleSearch}>
          🔍
        </span>
      </div>

      {error && <ErrorMessage message={error} onClose={handleCloseError}/>}
      {foundEvent && <FoundEvent eventId={foundEvent.id} title={foundEvent.name} onClose={handleCloseEvent} />}
    </div>
  );
};

export default SearchBar;
