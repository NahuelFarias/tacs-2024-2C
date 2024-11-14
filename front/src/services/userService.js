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
        const response = await fetch('http://3.140.245.119:8080/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username,
                password,
                email
            })
        });
        return response.ok;
    } catch (error) {
        console.error('Error creating user:', error);
        return false;
    }
}
