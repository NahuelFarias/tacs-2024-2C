import React from 'react';
import './EventCard.css';
import PrimaryButton from './PrimaryButton';

const ZoneCard = ({ zoneLocation, zonePrice, availableTickets }) => {
  function loggear () {
    console.log("esta Loggeado bien")
  }
  
  return (
    <div className="p-2 w-50">
      <div className="card bg-secondary text-white">
        <div className="card-body d-flex justify-content-between">
          <div className="details">
            <h5 className="zone-title">{zoneLocation} - {zonePrice}$</h5>
            <h6 className='available-tickets'>{availableTickets} tickets disponibles</h6>
          </div>
          <div className='d-flex'>
            <PrimaryButton onClick={() => loggear()}>Reservar</PrimaryButton>
          </div>
        </div>
      </div>
    </div>
    
  );
};   

export default ZoneCard;
