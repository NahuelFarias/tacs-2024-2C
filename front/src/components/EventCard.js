import React from 'react';
import './EventCard.css';

const EventCard = ({ title }) => (
  <div className="col-md-4 mb-4">
    <div className="card bg-secondary text-white">
      <div className="card-body">
        <h5 className="card-title">{title}</h5>
        <img src="/evento.jpg" alt="Icono" className="evento" />
      </div>
    </div>
  </div>
);

export default EventCard;
