import axiosClient from './axiosClient';

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
