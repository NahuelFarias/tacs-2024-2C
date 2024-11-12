import axios from 'axios';
import Cookies from 'js-cookie'

const axiosClient = axios.create({
    baseURL: process.env.REACT_APP_API_URL || 'http://3.140.245.119:8080', // URL base de la API
    headers: {
        'Content-Type': 'application/json'
    }
});

axiosClient.interceptors.request.use((config) => {
    const token = Cookies.get('Token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

axiosClient.interceptors.response.use(
  response => response,
  error => {
    console.error('Error en la solicitud:', error);
    return Promise.reject(error);
  }
);

export default axiosClient;
