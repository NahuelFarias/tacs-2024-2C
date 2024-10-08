import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import ZoneCard from '../components/ZoneCard';
import { closeEvent, getEvent } from '../services/eventService';
import './EventDetails.css';


const EventDetails = () => {
  const { eventId } = useParams();

  const [event, setEvent] = useState([]);

  const [userIsAdmin, setUserIsAdmin] = useState(false);

  useEffect(() => {
    const checkAdmin = () => {
      const isAdmin = localStorage.getItem('rol') === 'ROLE_ADMIN';
      setUserIsAdmin(isAdmin);
    };

    checkAdmin();
    const interval = setInterval(checkAdmin, 1000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    getEvent(eventId)
      .then(data => {
        setEvent(data);
      })
      .catch(error => {
        console.error('Error fetching events:', error);
      });
  }, []);
  const actualDate = new Date(event.date).toLocaleString().slice(0, -3).concat("hs");

  const handleCloseEvent = () => {
    if (closeEvent(eventId)) {
      window.location.reload()
    }

  };

  return (
    <div className="d-flex flex-column p-4 align-items-center w-100">
      <div className="event-details">

        <div className="d-flex justify-content-between mt-2 w-100">
          <h1>{event.name}</h1>
          <h2>{actualDate}</h2>
        </div>

        <img className="w-100" src={event.image_url} alt="Foto del evento"></img>

        <div className="mt-4">
          <div className="d-flex justify-content-between">
            <h3>Asientos disponibles</h3>
            {!event.open_sale && <h5>No está abierta la venta de tickets</h5>}
          </div>

          <div className="d-flex row">
            {event.locations && event.locations.map(zone => (
              <ZoneCard
                key={zone.name}
                eventId={event.id}
                eventName={event.name}
                eventDate={event.date}
                open_sale={event.open_sale}
                zoneLocation={zone.name}
                zonePrice={zone.price}
                availableTickets={zone.quantityTickets || 0}
              />
            ))}
          </div>

          {(userIsAdmin && event.open_sale) && (
            <div className="mt-4">
              <button onClick={handleCloseEvent} className="btn btn-danger">
                Cerrar venta
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default EventDetails;