import axiosClient from "./axiosClient";

export const tryCreateReservation = (createReservation) => {
    console.log(`trying to make a reservation of ${createReservation.amount} tickets`);
    return axiosClient.post('/reservation', createReservation)
      .then(response => {
        if (response.status === 200) {
          console.log(`"${createReservation.name}" ticket reserved successfully`)
          return true
        }
      })
      .catch(error => {
        console.log("error reserving tickets: ", error);
        window.alert(`Error reserving tickets: ${error.response.data.errorCause}`);
        throw error;
      });
  };