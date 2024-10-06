import axiosClient from "./axiosClient";
import { getEvent } from "./eventService";
import Promise from 'bluebird';

export const getReservations = (userId) => {
    return axiosClient.get(`/users/${userId}/reserves`)
        .then(response => {
          // Mapeamos las reservas a un array de IDs de eventos
          const eventIds = response.data.map(reservation => reservation.event);
          // Usamos Promise.map para obtener los eventos
          return Promise.map(eventIds, eventId => getEvent(eventId));
        })
        .catch(error => {
            console.error('Error al obtener eventos:', error);
            throw error;
        });
};


export const tryCreateReservation = (eventId, userId, zoneLocation) => {
    //console.log(`trying to make a reservation of ${zoneLocation}`);
    return axiosClient.post(`/events/${eventId}/reserves`, 
      { name: zoneLocation },
      { params: { user_id: userId } } 
    )
      .then(response => {
        if (response.status === 200) {
          //console.log(`Ticket reserved successfully`)
          return true
        }
      })
      .catch(error => {
        //console.log("error reserving tickets: ", error);
        window.alert(`Error reserving tickets: ${error.response.data.errorCause}`);
        throw error;
      });
  };
