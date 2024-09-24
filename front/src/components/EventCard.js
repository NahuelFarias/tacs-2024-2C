import {React, useEffect, useRef} from 'react';
import  { useNavigate } from 'react-router-dom';
import './EventCard.css';


const EventCard = ({ eventId, title }) => {
  const navigate = useNavigate();

  const redirectToDetails = () => {
    navigate(`/eventDetails/${eventId}`);
  };
  
  let eventCardRef = useRef();
  useEffect(() => {
    let closeReservationModal = (e) => {
        if(eventCardRef.current.contains(e.target)){
            redirectToDetails();
        }
    };
  
    document.addEventListener("mousedown", closeReservationModal);
  
    return() =>{
        document.removeEventListener("mousedown", closeReservationModal);
    }
  });

  return (
  <div className="col-md-4 mb-4" ref={eventCardRef}>
    <div className="card bg-secondary text-white">
      <div className="card-body">
        <h5 className="card-title">{title} - {eventId}</h5>
        <img src="/evento.jpg" alt="Icono" className="evento" />
      </div>
    </div>
  </div>
  );
};

export default EventCard;
