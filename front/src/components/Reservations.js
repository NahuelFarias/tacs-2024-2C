import React, { useEffect, useState } from 'react';
import { getReservations } from '../services/reservationService';
import TicketCard from "./TicketCard";
import './Reservations.css'
import { getEvent } from '../services/eventService';

const Reservations = () => {
    const [reservations, setReservations] = useState();

    useEffect(() => {
        getReservations(localStorage.getItem("id"))
            .then(data => {
                console.log("data: ", data);
                const reservations = groupReservationsByEvent(data);
                console.log(reservations);
                setReservations(reservations);
            })
            .catch(error => {
                console.error('Error fetching events:', error);
                setReservations([]);
            });
    }, []);

     function groupReservationsByEvent(reservations) {
        const reservationList = []
        const events = Array.from(new Set(reservations.map(item => item.event)));
        console.log("events: ",events)
        events.forEach(async item => {
            const event = item ? { event: item } : null;
            console.log(event);
            let thisEventName;
            let thisEventDate;
            let thisEventLocations;

            thisEventLocations = reservations.filter(reservation => reservation.event === item)
            console.log("thisEventLocations: ", thisEventLocations)
            thisEventLocations = groupReservationsByLocation(thisEventLocations)

            await getEvent(event.event).then(data => {
                console.log("event data: ",data);
                console.log("event data.name: ",data.name);
                console.log("event data.date: ",data.date);
                thisEventName = data.name;
                thisEventDate = data.date;

                thisEventLocations.map(itemLocation=>{
                    const location = data.locations.find(location => location.id == itemLocation.location);
                    itemLocation.name = location.name
                    itemLocation.price = location.price
                })
            });

            console.log("reservationList event: ", event.event)
            console.log("reservationList name: ", thisEventName);
            console.log("reservationList date: ", thisEventDate);
            console.log("reservationList locations: ", thisEventLocations);

            reservationList.push({
            event: event.event,
            eventName: thisEventName,
            eventDate: thisEventDate,
            locations: thisEventLocations
            });
        })
        console.log("reservationList: ", reservationList);
        return reservationList;
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
                        console.log("reservations front: ", reservations)
                    }
                    <div>
                        {reservations[0].event}
                    </div>
                    { reservations.map((reservation) => {
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
