import axios from 'axios';

const axiosClient = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080', // URL base de la API
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.response.use(
  response => response,
  error => {
    console.error('Error en la solicitud:', error);
    return Promise.reject(error);
  }
);

export default axiosClient;
