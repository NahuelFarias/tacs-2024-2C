import axiosClient from './axiosClient';

export const getStats = () => {
    return axiosClient.get('/statistics/use')
        .then(response => response.data)
        .catch(error => {
            console.error('Error al obtener estadisticas:', error);
            throw error;
        });
};
