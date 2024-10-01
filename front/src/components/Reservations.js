import React, { useEffect, useState } from 'react';
import EventCard from '../components/EventCard';
import { getReservations } from '../services/reservationService';

const Reservations = () => {
    const [reservations, setReservations] = useState([]);

    useEffect(() => {
        getReservations(localStorage.getItem("id")).then(data => {
                setReservations(data);
            })
            .catch(error => {
                console.error('Error fetching events:', error);
                setReservations([])
            });
    }, []);

    return (
        <div className="home-page">
            <div className="events-section container mt-3">
                <h2 className="text-white">Mis reservas</h2>
                <div className="row">
                    {reservations?.map(res => (
                        <EventCard key={res.event.id} eventId={res.event.id} title={res.event.name} />
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Reservations;
