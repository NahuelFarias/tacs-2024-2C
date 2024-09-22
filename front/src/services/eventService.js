import axiosClient from './axiosClient';

export const getEvent = (eventId) => {
  return true/*axiosClient.get(`/events/:${eventId}`)
    .then(response => response.data)
    .catch(error => {
      console.error(`Error al obtener el evento con id = ${eventId}, error:`, error)
    })*/
}

export const getEvents = () => {
  return axiosClient.get('/events')
    .then(response => response.data)
    .catch(error => {
      console.error('Error al obtener eventos:', error);
      throw error;
    });
};

export const formToCreateEventRequest = (eventName, date, locations) => {
  const createEventRequest = {
    name: eventName,
    date: date,
    ticketGenerator: {
      locations: locations.map((location) => ({name: location.name, price: parseInt(location.price)})),
      ticketsMap: locations.reduce((dict, location) => {dict[location.name] = parseInt(location.tickets); return dict}, {})
    }
  }

  console.log(createEventRequest)
  return createEventRequest
}

export const tryCreateEvent = (createEvent) => {
  console.log(`trying to create event ${createEvent.name}`);
  return axiosClient.post('/events', createEvent)
    .then(response => {
      if (response.status === 200) {
        console.log(`event "${createEvent.name}" created successfully`)
        return true
      }
    })
    .catch(error => {
      console.log("error creating event: ", error);
      window.alert(`Error creating event: ${error.response.data.errorCause}`);
      throw error;
    });
};