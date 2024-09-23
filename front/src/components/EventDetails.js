import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import ZoneCard from '../components/ZoneCard';
import { getEvent } from '../services/eventService';
import './EventDetails.css';


const EventDetails = () => {
  //const [event, setEvent] = useState([]);
  const { eventId } = useParams();
 
  const eventName = "Nombre de Evento";
  const eventLocation = "Grand Rex";
  const eventDate = "2024-09-23T01:03";
  const eventAdmin = "Gobierno de la Cuidad de Buenos Aires";
  const popular = {
    zoneLocation : 'popular',
    zonePrice: '10',
    availableTickets : 15
  }
  const platea = {
    zoneLocation : 'platea',
    zonePrice: '15',
    availableTickets : 8
  }

  const eventMock = {
    id: eventId,
    name: eventName,
    location: eventLocation,
    date: eventDate,
    admin: eventAdmin,
    zones: [popular, platea]
  }

  const actualDate = new Date(eventMock.date).toLocaleString().slice(0, -3).concat("hs");

  useEffect(() => {
    console.log(eventId);
    /*getEvent(1)
      .then(data => {
        setEvent(data);
        console.log(data);
        console.log(event);
      })
      .catch(error => console.error('Error fetching event detail:', error))*/
  }, []);

  return (
    <div className="d-flex flex-column p-4 align-items-center w-100">
      <div className="event-details">

        <div className="d-flex justify-content-between mt-2 w-100">
          <h1>{eventMock.name}</h1>

          <h2>{actualDate}</h2>
        </div>

        <img className="w-100" src="/evento.jpg" alt="Foto del evento"></img>


        <div className="mt-4">
          <h3>Asientos disponibles</h3>
          <div className='d-flex row'>
            {eventMock.zones.map(zone => (
              <ZoneCard key={zone.zoneLocation} eventId={eventMock.id} eventName={eventMock.name} eventDate={eventMock.date} zoneLocation={zone.zoneLocation} zonePrice={zone.zonePrice} availableTickets={zone.availableTickets} />
            ))}

          </div>
        </div>
      </div>
    </div>
  );
}

export default EventDetails;
