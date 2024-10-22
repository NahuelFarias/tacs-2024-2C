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
                eventDate: element.event.date,
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
        console.log(acc)
        return acc;
    }, {});

    const reservationsByEvent = Object.entries(groupedReservations);

    return (
        <div className="home-page">
            <div className="events-section container mt-3">
                <h2 className="text-white">Mis reservas</h2>
                <div className="row tickets">

                    {reservationsByEvent.map(([eventId, { eventName, eventDate, locations }]) => {
                        const totalPrice = locations.reduce((sum, loc) => sum + loc.price * loc.count, 0);

                        return (
                            <TicketCard
                                key={eventId}
                                eventId={eventId}
                                title={eventName}
                                eventDate={eventDate}
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
