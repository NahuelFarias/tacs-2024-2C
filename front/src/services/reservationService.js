import axiosClient from "./axiosClient";

export const getReservations = (userId) => {
    return axiosClient.get(`/users/${userId}/reserves`)
        .then(response => response.data)
        .catch(error => {
            console.error('Error al obtener eventos:', error);
            throw error;
        });
};


export const tryCreateReservation = (eventId, userId, zoneLocation, tickets) => {
    //console.log(`trying to make a reservation of ${zoneLocation}`);
    return axiosClient.post(`/events/${eventId}/reserves`, 
      { name: zoneLocation, quantityTickets: tickets},
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
