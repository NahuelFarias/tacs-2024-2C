import axiosClient from './axiosClient';

export const getUsers = () => {
  return axiosClient.get('/users')
    .then(response => response.data)
    .catch(error => {
      console.error('Error al obtener usuarios:', error);
      throw error;
    });
};
