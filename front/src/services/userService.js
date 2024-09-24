import axiosClient from './axiosClient';

export const getUsers = () => {
  return axiosClient.get('/users')
    .then(response => response.data)
    .catch(error => {
      console.error('Error al obtener usuarios:', error);
      throw error;
    });
};

export const tryCreateUser = (username, password) => {
  console.log(`creating user ${username}`)
  return axiosClient.post('/users', { username, password })
    .then(response => {
      if (response.status === 201) {
        window.alert("account created successfully")
      }
    })
    .catch(error => {
      if (error.response) {
        window.alert(`error creating account: ${error.response.data.errorCause}`);
      }
      throw error;
    });
}
