import {React, useEffect, useState} from 'react';
import './EventCard.css';
import PrimaryButton from './PrimaryButton';
import ReservationModal from './ReservationModal';

const ZoneCard = ({eventId, eventName, eventDate, zoneLocation, zonePrice, availableTickets }) => {
  const [openReservationModal, serReservationModal] = useState(false);

  const infoReserva = {
    eventId: eventId,
    eventName: eventName,
    eventDate: eventDate,
    zoneLocation: zoneLocation,
    zonePrice: zonePrice,
    availableTickets: availableTickets,
  }

  function loggear () {
    serReservationModal(true);
    console.log(`esta Loggeado bien, ${eventId}, ${eventDate}, ${zoneLocation}, ${zonePrice}, ${availableTickets}`)
  }

  return (
    <div className="p-2 w-50">
      <div className="card bg-secondary text-white">
        <div className="card-body d-flex justify-content-between">
          <div className="details">
            <h5 className="zone-title">{zoneLocation} - {zonePrice}$</h5>
            <h6 className='available-tickets'>{availableTickets} tickets disponibles</h6>
          </div>
          <div className='d-flex position-relative'>
            <PrimaryButton onClick={() => loggear()}>Reservar</PrimaryButton>
          </div>
        </div>
      </div>

      {openReservationModal && <ReservationModal closeModal={serReservationModal} data={infoReserva}/>}
    </div>
    
  );
};   

export default ZoneCard;