import axiosClient from './axiosClient';

export const getEvents = () => {
  return axiosClient.get('/events')
    .then(response => response.data)
    .catch(error => {
      console.error('Error al obtener eventos:', error);
      throw error;
    });
};
