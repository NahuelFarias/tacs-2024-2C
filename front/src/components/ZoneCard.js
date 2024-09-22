import React from 'react';
import './EventCard.css';

const EventCard = ({ zoneLocation, availableTickets }) => (
  <div className="col-md-4 m-2">
    <div className="card bg-secondary text-white">
      <div className="card-body d-flex justify-content-between">
        <div class="details">
          <h5 className="zone-title">{zoneLocation}</h5>
          <h6 class='available-tickets'>{availableTickets}</h6>
        </div>
        <div class='reserv-button'>
          <button class='button'>Reservar</button>
        </div>
      </div>
    </div>
  </div>
);   

export default EventCard;
