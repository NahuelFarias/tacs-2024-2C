import React, { useState } from 'react';
import PrimaryButton from './PrimaryButton';
import { formToCreateEventRequest, tryCreateEvent } from '../services/eventService';

const EventCreation = () => {
    const [eventName, setEventName] = useState('');
    const [dateTime, setDateTime] = useState();
    const [locations, setLocations] = useState([{ name: '', price: '', tickets: '' }]);
    const [error, setError] = useState('');

    const minDateTime = new Date().toISOString().slice(0, 16);

    const handleNameChange = (e) => setEventName(e.target.value);
    const handleDateTimeChange = (e) => setDateTime(e.target.value)
  
    const handleLocationChange = (index, field, value) => {
      const newLocations = [...locations];
      newLocations[index][field] = value;
      setLocations(newLocations);
    };
  
    const addLocation = () => {
      setLocations([...locations, { name: '', price: '', tickets: '' }]);
    };
  
    const removeLocation = (index) => {
      const newLocations = locations.filter((_, i) => i !== index);
      setLocations(newLocations);
    };

    const eventIsValid = () => {
      if (eventName.length < 5) {
        setError("event name is not valid")
        return false
      }
      const invalidLocationIndexes = locations.map((location, index) =>
      [location.name.length < 4 || location.price === '' || location.tickets === '', index])
      .filter((validity) => validity[0])
      .map((validity) => validity[1] + 1)

      if (invalidLocationIndexes.length > 0) {
        setError(`location(s) ${invalidLocationIndexes.join(", ")} are not valid`)
        return false
      }
      setError("")
      return true
    }
  
    const handleSubmit = (e) => {
      e.preventDefault();
      if (eventIsValid()) {
        const createEventRequest = formToCreateEventRequest(eventName, dateTime, locations)
        tryCreateEvent(createEventRequest)
      }

    };
  
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ backgroundColor: '#1a1a1a' }}>
        <div className="p-5 m-5 rounded shadow" style={{ backgroundColor: 'white', width: '90%', maxWidth: '500px', height: 'auto' }}>
          <h2 className="text-center text-dark mb-4">Create Event</h2>
          <form className="d-flex flex-column justify-content-between" style={{ height: '100%' }} onSubmit={handleSubmit}>
            <div className="mb-4">
              <input
                type="text"
                id="name"
                value={eventName}
                onChange={handleNameChange}
                placeholder="Event name"
                className="form-control"
              />
            </div>
            <div className="mb-4">
              <input
                type="datetime-local"
                id="datetime"
                value={dateTime}
                onChange={handleDateTimeChange}
                className="form-control"
                min={minDateTime}
              />
            </div>
  
            <div className="mb-4">
              {locations.length > 0  && <h4 className="text-center text-dark mb-4">Locations</h4>}
              {locations.map((location, index) => (
                <div key={index} className="mb-3 p-2 border rounded">
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <input
                      type="text"
                      placeholder="Location name"
                      value={location.name}
                      onChange={(e) => handleLocationChange(index, 'name', e.target.value)}
                      className="form-control me-2"
                      style={{ flex: 1 }}
                    />
                    <button
                      type="button"
                      className="btn btn-danger btn-sm"
                      onClick={() => removeLocation(index)}
                    >
                      X
                    </button>
                  </div>
                  <div className="row">
                    <div className="col-6">
                      <input
                        type="number"
                        placeholder="Price"
                        value={location.price}
                        onChange={(e) => handleLocationChange(index, 'price', e.target.value)}
                        className="form-control"
                      />
                    </div>
                    <div className="col-6">
                      <input
                        type="number"
                        placeholder="Tickets available"
                        value={location.tickets}
                        onChange={(e) => handleLocationChange(index, 'tickets', e.target.value)}
                        className="form-control"
                      />
                    </div>
                  </div>
                </div>
              ))}
  
              <div className="d-flex justify-content-center mt-3">
                <button type="button" className="btn btn-secondary" onClick={addLocation}>Add Location</button>
              </div>
            </div>
  
            {error && <h3 className="text-danger text-center" style={{ fontSize: '0.85rem', marginBottom: '1rem' }}>{error}</h3>}
            <PrimaryButton type="submit">Create Event</PrimaryButton>
          </form>
        </div>
      </div>
    );
  };

export default EventCreation;