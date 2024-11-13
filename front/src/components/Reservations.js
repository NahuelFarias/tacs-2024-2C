import React, { useEffect, useState } from 'react';
import { getReservations } from '../services/reservationService';
import TicketCard from "./TicketCard";
import './Reservations.css';
import { getEvent } from '../services/eventService';

const Reservations = () => {
    const [reservations_front, setReservations] = useState([]);

    useEffect(() => {
        getReservations(localStorage.getItem("id"))
            .then(async data => {
                const reservationsData = await groupReservationsByEvent(data); // Cambia el nombre para evitar confusión
                setReservations(reservationsData); // Actualiza el estado directamente
            })
            .catch(error => {
                setReservations([]);
            });
    }, []);

    async function groupReservationsByEvent(reservations) {
        const reservationList = [];
        const events = Array.from(new Set(reservations.map(item => item.event)));


        for (const item of events) {
            const event = item ? { event: item } : null;

            let thisEventName;
            let thisEventDate;
            let thisEventLocations;

            thisEventLocations = reservations.filter(reservation => reservation.event === item);
            thisEventLocations = groupReservationsByLocation(thisEventLocations);

            const data = await getEvent(event.event);

            thisEventName = data.name;
            thisEventDate = data.date;

            thisEventLocations = thisEventLocations.map(itemLocation => {
                const location = data.locations.find(location => location.id === itemLocation.location);
                return {
                    ...itemLocation,
                    name: location.name,
                    price: location.price,
                };
            });

            reservationList.push({
                event: event.event,
                eventName: thisEventName,
                eventDate: thisEventDate,
                locations: thisEventLocations,
            });
        }

        return reservationList;
    }

    function groupReservationsByLocation(reservations) {
        const locations = Array.from(new Set(reservations.map(item => item.location)));

        const locationList = locations.map(item => ({
            location: item,
            reservations: reservations.filter(reservation => reservation.location === item),
        }));
        return locationList;
    }

    return (
        <div className="home-page">
            <div className="events-section container mt-3">
                <h2 className="text-white">Mis reservas</h2>
                <div className="row tickets">
                    {reservations_front.map((reservation) => {
                        const totalPrice = reservation.locations.reduce((sum, loc) => sum + loc.price * loc.reservations.length, 0);
                        return (
                            <TicketCard
                                key={reservation.event}
                                eventId={reservation.event}
                                title={reservation.eventName}
                                eventDate={reservation.eventDate}
                                locations={reservation.locations}
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
