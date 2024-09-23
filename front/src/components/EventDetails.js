import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import ZoneCard from '../components/ZoneCard';
import { getEvent } from '../services/eventService';
import './EventDetails.css';


const EventDetails = () => {
  const { eventId } = useParams(); 
  console.log("eventId:", eventId);

  const eventName = "Nombre de Evento";
  const eventDate = "2024-09-23T01:03";
  const availableTicketsAmount = 150;
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
    availableTicketsAmount: availableTicketsAmount,
    date: eventDate,
    id: 0,
    name: eventName,
    open_sale: true,
    soldTicketsAmount: 0,
    totalSales: 0,
    zones: [popular, platea]
  }  

  const [event, setEvent] = useState([]);

  useEffect(() => {
    getEvent(eventId)
      .then(data => {
        console.log(data);
        setEvent(data);
      })
      .catch(error => {
        console.error('Error fetching events:', error);
        setEvent(eventMock)
      });
    }, []);
    
  const actualDate = new Date(event.date).toLocaleString().slice(0, -3).concat("hs");
  console.log("event: ", event);
 

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
            {eventMock.zones.map(zone => (
              <ZoneCard key={zone.zoneLocation} open_sale={event.open_sale} eventId={event.id} eventName={event.name} eventDate={event.date} zoneLocation={zone.zoneLocation} zonePrice={zone.zonePrice} availableTickets={event.availableTicketsAmount} />
            ))}

          </div>
        </div>
      </div>
    </div>
  );
}

export default EventDetails;
