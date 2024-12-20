import axiosClient from "./axiosClient";
import { getEvent } from "./eventService";
import Promise from 'bluebird';

//TODO: Ojo que puede romper
export const getReservations = (userId) => {
    return axiosClient.get(`/users/${userId}/reserves`)
        .then(response => response.data)
        .catch(error => {
            console.error('Error al obtener eventos:', error);
            throw error;
        });
};


export const tryCreateReservation = (eventId, userId, zoneLocation, tickets) => {
    return axiosClient.post(`/events/${eventId}/reserves`, 
      { name: zoneLocation, quantityTickets: tickets},
      { params: { user_id: userId } } 
    )
      .then(response => {
        if (response.status === 200) {
          return true
        }
      })
      .catch(error => {
        window.alert(`Error reserving tickets: ${error.response.data.errorCause}`);
        throw error;
      });
  };
