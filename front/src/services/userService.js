import axiosClient from './axiosClient';

export const getUsers = () => {
  return axiosClient.get('/users')
    .then(response => response.data)
    .catch(error => {
      console.error('Error al obtener usuarios:', error);
      throw error;
    });
};


export const tryCreateUser = async (username, password, email) => {
    try {
        await axiosClient.post('/users', { username, password, email });
        return true;
    } catch (error) {
        console.error('Error creating user:', error);
        return false;
    }
}

export const resetPassword = (username, newPassword) => {
    return axiosClient.put('/users/reset-password', null, {
        params: { username, newPassword }
    }).then(response => response.data);
}
