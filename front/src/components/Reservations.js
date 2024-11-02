import React, { useEffect, useState } from 'react';
import { getReservations } from '../services/reservationService';
import TicketCard from "./TicketCard";
import './Reservations.css'
import { getEvent } from '../services/eventService';

const Reservations = () => {
    const [reservations, setReservations] = useState([]);

    useEffect(() => {
        getReservations(localStorage.getItem("id"))
            .then(data => {
                console.log("data: ", data);
                const reservations = groupReservationsByEvent(data);
                setReservations(reservations);
            })
            .catch(error => {
                console.error('Error fetching events:', error);
                setReservations([]);
            });
    }, []);

    function groupReservationsByEvent(reservations) {
        const events = Array.from(new Set(reservations.map(item => item.event)));
        console.log("events: ",events)
        const eventList = events.map(async item => {
            const event = item ? { event: item } : null;
            let thisEventName;
            let thisEventDate;
            let thisEventLocations;

            thisEventLocations = reservations.filter(reservation => reservation.event === item)
            groupReservationsByLocation(thisEventLocations)

            getEvent(event).then(data => {
                console.log("event data: ",data);
                thisEventName = data.name;
                thisEventDate = data.date;

                thisEventLocations.locations.map(itemLocation=>{
                    const location = data.locations.find(location => location.id == itemLocation.location);
                    itemLocation.name = location.name
                    itemLocation.price = location.price
                })
            });

            console.log("eventList name: ", thisEventName);
            console.log("eventList date: ", thisEventDate);
            console.log("eventList locations: ", thisEventLocations);

            return {
            ...event,
            eventName: thisEventName,
            eventDate: thisEventDate,
            locations: thisEventLocations
            };
        })
        console.log("eventList: ", eventList);
        return eventList;
    }

    function groupReservationsByLocation(reservations) {
        const locations = Array.from(new Set(reservations.map(item => item.location)));
        console.log("locations: ",locations)
        const locationList = locations.map(item => {
            const location = item ? { location: item } : null;

            return {
            ...location,
            reservations: reservations.filter(reservation => reservation.location === item)
            };
        });
        console.log("locationList: ", locationList);
        return locationList;
    }

    return (
        <div className="home-page">
            <div className="events-section container mt-3">
                <h2 className="text-white">Mis reservas</h2>
                <div className="row tickets">
                    {
                        reservations.map((reservation) => {
                            console.log("reservations front: ", reservations)
                            console.log("reservations front: ", reservation)
                            console.log("eventId card: ", reservation.event)
                            console.log("title card: ", reservation.eventName)
                            console.log("eventDate card: ", reservation.eventDate)
                            console.log("locations card: ", reservation.locations)
                            reservation.locations.forEach( location => {
                                console.log("location name: ",location.name)
                                console.log("location price: ",location.price)
                                console.log("location length: ",location.reservations.length)
                            })
                            const totalPrice = reservation.locations.reduce((sum, loc) => sum + loc.price + loc.reservations.length, 0)
                            console.log("totalPrice: ", totalPrice)

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
