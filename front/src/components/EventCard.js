import React from 'react';

const EventCard = ({ title }) => (
  <div className="col-md-4 mb-4">
    <div className="card bg-secondary text-white">
      <div className="card-body">
        <h5 className="card-title">{title}</h5>
        <p className="card-text">Aquí va una imagen</p>
      </div>
    </div>
  </div>
);

export default EventCard;
