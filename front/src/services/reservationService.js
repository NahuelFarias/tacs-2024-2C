import axiosClient from "./axiosClient";

export const getReservations = (userId) => {
    return axiosClient.get('/users/' + "1" + '/reserves')
        .then(response => response.data)
        .catch(error => {
            console.error('Error al obtener eventos:', error);
            throw error;
        });
};