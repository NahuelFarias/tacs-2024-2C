import React, { useEffect, useState } from 'react';
import { getReservations } from '../services/reservationService';
import TicketCard from "./TicketCard";
import './Reservations.css'

const Reservations = () => {
    const [reservations, setReservations] = useState([]);

    useEffect(() => {
        getReservations(localStorage.getItem("id"))
            .then(data => {
                setReservations(data);
            })
            .catch(error => {
                console.error('Error fetching events:', error);
                setReservations([]);
            });
    }, []);

    const groupedReservations = reservations.reduce((acc, element) => {
        const eventId = element.event.id;
        const location = element.event.locations.find(l => l.id == element.reservation.location);

        if (!acc[eventId]) {
            acc[eventId] = {
                eventName: element.event.name,
                reservationDate: element.reservation.reservationDate,
                locations: []
            };
        }

        const existingLocation = acc[eventId].locations.find(loc => loc.name === location.name);

        if (existingLocation) {
            existingLocation.count += 1;
        } else {
            acc[eventId].locations.push({
                name: location.name,
                price: location.price,
                count: 1
            });
        }

        return acc;
    }, {});

    const reservationsByEvent = Object.entries(groupedReservations);

    return (
        <div className="home-page">
            <div className="events-section container mt-3">
                <h2 className="text-white">Mis reservas</h2>
                <div className="row tickets">
                    {reservationsByEvent.map(([eventId, { eventName, reservationDate, locations }]) => {
                        const totalPrice = locations.reduce((sum, loc) => sum + loc.price * loc.count, 0);

                        return (
                            <TicketCard
                                key={eventId}
                                eventId={eventId}
                                title={eventName}
                                reservationDate={reservationDate}
                                locations={locations}
                                totalPrice={totalPrice}
                            />
                        );
                    })}
                </div>
            </div>
        </div>
    );
}

export default Reservations;
