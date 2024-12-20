import React from 'react';
import  { useNavigate } from 'react-router-dom';
import './FoundEvent.css'; // Opcional: estilos para el componente

const FoundEvent = ({ eventId, title, onClose }) => {
    const navigate = useNavigate();

    const handleDetailsClick = () => {
        // Aquí puedes implementar la lógica para navegar a la página de detalles.
        navigate(`/eventDetails/${eventId}`);
      };

    
    return (
    <div className="found-event alert alert-info position-relative">
        <h5 className="event-title">{title}</h5>
        <span className="event-detail-link" onClick={handleDetailsClick} style={{ cursor: 'pointer', textDecoration: 'none', marginRight: '50px' }}>
            Ver detalles
        </span>
        <button type="button" className="close" onClick={onClose} aria-label="Close">
        <span aria-hidden="true">&times;</span>
        </button>
    </div>
);
}

export default FoundEvent;
