import axiosClient from "./axiosClient";

export const getReservations = (userId) => {
    return axiosClient.get('/users/' + localStorage.getItem("id") + '/reserves')
        .then(response => response.data)
        .catch(error => {
            console.error('Error al obtener eventos:', error);
            throw error;
        });
};