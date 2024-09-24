import axiosClient from './axiosClient';

export const tryAuthenticate = (username, password) => {
  
    return axiosClient.post('/login', { username, password })
      .then(response => {
        if (response.status === 200) {
          localStorage.setItem("username", username);
          localStorage.setItem("token", response.data.token);
          localStorage.setItem("id",response.data.id);
          localStorage.setItem("rol",response.data.rol)
          localStorage.setItem("loggedIn", "true")
          return true
        }
      })
      .catch(error => {
        console.log("error fetching JWT:", error);
        if (error.response && error.response.status === 401) {
          window.alert(`Authentication error: ${error.response.data.errorCause}`);
          return false
        }
        throw error;
      });
  };