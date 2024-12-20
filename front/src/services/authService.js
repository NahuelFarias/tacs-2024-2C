import axiosClient from './axiosClient';
import CryptoJS from 'crypto-js';
import Cookies from "js-cookie";

export const tryAuthenticate = (concreteUsername, concretePassword) => {
  
    return axiosClient.post('/login', {
        username: concreteUsername,
        password: concretePassword
    })
      .then(response => {
        if (response.status === 200) {
          localStorage.setItem("username", concreteUsername);

          Cookies.set('Token',response.data.token);

          localStorage.setItem("id",response.data.id);
          localStorage.setItem("rol",response.data.rol)
          localStorage.setItem("loggedIn", "true")

          axiosClient.defaults.headers['Role'] = localStorage.getItem('rol')
          return { success: true }
        }
      })
      .catch(error => {
        if (error.response && error.response.status === 401) {
          return { success: false, message: `Authentication error: ${error.response.data.errorCause}` };
        }
        else if (error.response && error.response.status === 403) {
           return { success: false, message: 'Credenciales incorrectas. Por favor, inténtalo de nuevo.' };
        }

        return { success: false, message: `Authentication error: ${error.response.data.errorCause}` };
      });
  };

export const getSalt = (username) => {
    return axiosClient.get(`/login/salt`,{params: {username}})
        .then(response => {
            return {success: true, salt: response.data}
        })
        .catch(error => {
            if (error.response && error.response.status === 403) {
                return { success: false, message: 'Credenciales incorrectas. Por favor, inténtalo de nuevo.' };
            }
            console.error('Error al obtener sal del usuario:', error);
            throw error;
        });
}

export const hashPassword = (password, salt) => {
    const key128Bits = CryptoJS.PBKDF2(password, CryptoJS.enc.Base64.parse(salt), {
        keySize: 256 / 32,
        iterations: 65536,
        hasher: CryptoJS.algo.SHA1
    });
    return salt + ":" + CryptoJS.enc.Base64.stringify(key128Bits);
};
