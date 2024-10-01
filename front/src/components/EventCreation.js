import React, { useState } from 'react';
import PrimaryButton from './PrimaryButton';
import { formToCreateEventRequest, tryCreateEvent } from '../services/eventService';
import './EventCreation.css'
import {useNavigate} from "react-router-dom";

const EventCreation = () => {
    const [eventName, setEventName] = useState('');
    const [dateTime, setDateTime] = useState();
    const [created, setCreated] = useState('');
    const [locations, setLocations] = useState([{ name: '', price: '', tickets: '' }]);
    const [error, setError] = useState('');

    const navigate = useNavigate()

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
  
    const handleSubmit = async (e) => {
      e.preventDefault();
      if (eventIsValid()) {
          const createEventRequest = formToCreateEventRequest(eventName, dateTime, locations)
          const result = await tryCreateEvent(createEventRequest)
          if (result.success) {
              setCreated("Evento Creado!")
          }
      }

    };

    const redirectToHome = () => {
        navigate('/');
    };


    return (
      <div className="creation-background d-flex justify-content-center align-items-center">
        <div className="creation-box p-5 m-5 rounded shadow" style={{ backgroundColor: 'white', width: '90%', maxWidth: '500px', height: 'auto' }}>
          <h2 className="text-center text-dark mb-5">Crear Nuevo Evento</h2>
          <form className="d-flex flex-column justify-content-between" style={{ height: '100%' }} onSubmit={handleSubmit}>
              <div className="mb-4">
                  <p className="text-dark input-label">
                      Nombre del evento
                  </p>
                  <input
                      type="text"
                      id="name"
                      value={eventName}
                      onChange={handleNameChange}
                      placeholder=""
                      className="form-control"
                  />
              </div>
              <div className="mb-5">
                  <p className="text-dark input-label">
                      Fecha del evento
                  </p>
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
                  {locations.length > 0 && <h4 className="text-center text-dark mb-4">Ubicaciones</h4>}
              {locations.map((location, index) => (
                  <div key={index} className="mb-3 p-2 border rounded">
                      <p className="text-dark input-label mt-1">
                          Nombre de la ubicación
                      </p>
                      <div className="d-flex justify-content-between align-items-center mb-2">
                          <input
                              type="text"
                              placeholder=""
                              value={location.name}
                              onChange={(e) => handleLocationChange(index, 'name', e.target.value)}
                              className="form-control me-2"
                              style={{flex: 1}}
                          />
                          <button
                              type="button"
                              className="btn btn-danger btn-sm"
                              onClick={() => removeLocation(index)}
                          >
                              X
                          </button>
                      </div>
                      <div className="row mt-4">
                          <div className="col-6">
                              <p className="text-dark input-label">
                                  Precio localidad
                              </p>
                              <input
                                  type="number"
                                  placeholder=""
                                  value={location.price}
                                  onChange={(e) => handleLocationChange(index, 'price', e.target.value)}
                                  className="form-control mb-2"
                              />
                          </div>
                          <div className="col-6">
                              <p className="text-dark input-label">
                              Cantidad tickets
                              </p>
                              <input
                                  type="number"
                                  placeholder=""
                                  value={location.tickets}
                                  onChange={(e) => handleLocationChange(index, 'tickets', e.target.value)}
                                  className="form-control mb-2"
                              />
                          </div>
                      </div>
                  </div>
              ))}

                  <div className="d-flex justify-content-center mt-3">
                      {!created && <button type="button" className="btn btn-secondary" onClick={addLocation}>Agregar otra ubicación</button>}
              </div>
            </div>
              {error && <h3 className="text-danger text-center" style={{ fontSize: '0.85rem', marginBottom: '1rem' }}>{error}</h3>}
              {!created && <PrimaryButton type="submit">Crear evento</PrimaryButton>}
              {created && <div className="alert alert-success mt-4">{created}</div>}
              {created && <p className="text-center text-dark mt-3">
                  Volver a la <span className="home-link" onClick={redirectToHome}>Página Principal</span>
              </p>}
          </form>
        </div>
      </div>
    );
};

export default EventCreation;