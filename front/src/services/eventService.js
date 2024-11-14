import axiosClient from './axiosClient';

export const getEvent = (eventId) => {
  return axiosClient.get(`/events/${eventId}`)
  .then(response => response.data)
  .catch(error => {
    console.error('Error al obtener eventos:', error);
    throw error;
  });
}

export const getEvents = () => {
  return axiosClient.get('/events')
    .then(response => response.data)
    .catch(error => {
      console.error('Error al obtener eventos:', error);
      throw error;
    });
};

export const getEventByName = (eventName) => {
  return axiosClient.get('/events/search', {
      params: {
        name: eventName
      }
    })
    .then(response => response.data)
    .catch(error => {
      console.error('Error al obtener eventos:', error);
      throw error;
    });
};

export const formToCreateEventRequest = (eventName, date, locations, imageUrl) => {
  const createEventRequest = {
    name: eventName,
    date: date,
    imageUrl: imageUrl,
    locations: locations.map((location) => ({name: location.name, price: parseInt(location.price), quantityTickets: parseInt(location.tickets, 10)}))
  }

  return createEventRequest
}

export const tryCreateEvent = (createEvent) => {
  return axiosClient.post('/events', createEvent)
    .then(response => {
      if (response.status === 200) {
        return { success: true }
      }
    })
    .catch(error => {
      window.alert(`Error creating event: ${error.response.data.errorCause}`);
      throw error;
    });
};

export const closeEvent = (eventId) => {
  return axiosClient.put(`/events/${eventId}/close`)
  .then(response => {
    if (response.status === 200) {
      return true
    }
  })
  .catch(error => { throw error })
}