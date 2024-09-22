import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import ZoneCard from '../components/ZoneCard';
import { getEvent } from '../services/eventService';
import './EventDetails.css';


const EventDetails = () => {
  const [event, setEvent] = useState([]);
  const { eventId } = useParams();
 
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

  const eventMock = {
    id: 1,
    name: eventName,
    location: eventLocation,
    date: eventDate,
    admin: eventAdmin,
    zones: [platea, popular]
  }

  useEffect(() => {
    console.log(eventId);
    getEvent(1)
      .then(data => {
        setEvent(data);
        console.log(data);
        console.log(event);
      })
      .catch(error => console.error('Error fetching event detail:', error))
  }, []);

  return (
    <div className="d-flex flex-column p-4 align-items-center w-100">
        <div className="event-details">

            <h1>{eventId}</h1>
            <div className="d-flex justify-content-between mt-2 w-100">
                <h2>{eventMock.name}</h2>

                <h3>{eventMock.date}</h3>
            </div>

            <img className="w-100" src="/evento.jpg" alt="Foto del evento"></img>


            <div className="mt-2">
                <h2>Asientos disponibles</h2>
                <div className='d-flex'>
                {eventMock.zones.map(zone => (
                    <ZoneCard key={zone.id} zoneLocation={zone.zoneLocation}  availableTickets={zone.availableTickets} /> 
                    ))}

                </div>
            </div>
        </div>
    </div>
  );
}

export default EventDetails;
