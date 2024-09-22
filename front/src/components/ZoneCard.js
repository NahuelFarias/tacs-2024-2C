import React from 'react';
import './EventCard.css';
import PrimaryButton from './PrimaryButton';

const ZoneCard = ({ zoneLocation, availableTickets }) => {
  function loggear () {
    console.log("esta Loggeado bien")
  }
  
  return (
    <div className="col-md-4 m-2">
      <div className="card bg-secondary text-white">
        <div className="card-body d-flex justify-content-between">
          <div className="details">
            <h5 className="zone-title">{zoneLocation}</h5>
            <h6 className='available-tickets'>{availableTickets}</h6>
          </div>
          <div className='d-flex'>
            <PrimaryButton >Reservar</PrimaryButton>
          </div>
        </div>
      </div>
    </div>
    
  );
};   

export default ZoneCard;
