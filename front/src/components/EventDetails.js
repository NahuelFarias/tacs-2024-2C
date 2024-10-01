import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import ZoneCard from '../components/ZoneCard';
import { getEvent } from '../services/eventService';
import './EventDetails.css';


const EventDetails = () => {
  const { eventId } = useParams();

  const [event, setEvent] = useState([]);

  useEffect(() => {
    getEvent(eventId)
      .then(data => {
        // console.log(data);
        setEvent(data);
      })
      .catch(error => {
        console.error('Error fetching events:', error);
      });
    }, []);
    
  const actualDate = new Date(event.date).toLocaleString().slice(0, -3).concat("hs");
  // console.log("event: ", event);
 

  return (
    <div className="d-flex flex-column p-4 align-items-center w-100" >
      <div className="event-details">

        <div className="d-flex justify-content-between mt-2 w-100">
          <h1>{event.name}</h1>

          <h2>{actualDate}</h2>
        </div>

        <img className="w-100" src="/evento.jpg" alt="Foto del evento"></img>


        <div className="mt-4">
          <div className="d-flex justify-content-between">
            <h3>Asientos disponibles</h3>
        {!event.open_sale && <h5>No esta abierta la venta de tickets</h5>}
          </div>
          <div className='d-flex row'>
            {event.locations && event.locations.map(zone => (
                <ZoneCard
                    key={zone.name}
                    eventId={event.id}
                    eventName={event.name}
                    eventDate={event.date}
                    open_sale={event.open_sale}
                    zoneLocation={zone.name}
                    zonePrice={zone.price}
                    availableTickets={zone.quantityTickets || 0} // Asegúrate de que availableTickets esté en tu JSON o define un valor por defecto
                />  ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default EventDetails;
