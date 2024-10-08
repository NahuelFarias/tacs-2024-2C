import { React, useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import PrimaryButton from './PrimaryButton';
import SecondaryButton from './SecondaryButton';
import { tryCreateReservation } from '../services/reservationService';
import './ReservationModal.css';

const ReservationModal = ({ closeModal, data }) => {
    const [isLoading, setIsLoading] = useState(false); // Estado de carga
    const navigate = useNavigate();
    const reservationModalRef = useRef();
    const actualDate = new Date(data.eventDate).toLocaleString().slice(0, -3).concat("hs");


    const [totalPrice, setTotalprice] = useState(data.zonePrice);
    const [reservation, setReservation] = useState({ eventId: data.eventId, zone: data.zoneLocation, amount: 1});

    const [tickets, setTickets] = useState(1);
    const handleTickets = (e) => {
        setTotalprice(e.target.value*data.zonePrice);
        setTickets(e.target.value);
        reservation.amount = (e.target.value);
    }
    useEffect(() => {
        const closeReservationModal = (e) => {
            if (!reservationModalRef.current.contains(e.target)) {
                closeModal(false);
            }
        };

        document.addEventListener("mousedown", closeReservationModal);

        return () => {
            document.removeEventListener("mousedown", closeReservationModal);
        };
    }, [closeModal]);

    const makeReservation = () => {
        const loginStatus = localStorage.getItem('loggedIn') === "true";

        if (loginStatus) {
            setIsLoading(true);
            tryCreateReservation(data.eventId, localStorage.getItem("id"), data.zoneLocation, tickets)
                .then(() => {
                    setTimeout(() => {
                        setIsLoading(false);
                        navigate('/reservations');
                    }, 2000); // 2 segundos de delay
                })
                .catch((error) => {
                    console.error("Error al realizar la reserva:", error);
                    setIsLoading(false);
                });
        }
        else {
            navigate("/login", { state: { message: 'Para reservar, debes iniciar sesion.' } })
        }
    }

    return (
        <div className="d-flex modal-Background justify-content-center align-items-center">
            <div className="modal-Container p-5 m-5 rounded shadow" ref={reservationModalRef}>
                <h1 className="text-center text-dark mb-4">Reserva tus asientos</h1>
                {isLoading ? (
                    <div className="text-center">
                        <h2 className="text-dark">Procesando tu reserva...</h2>
                        <div className="spinner-border text-dark" role="status">
                            <span className="visually-hidden">Cargando...</span>
                        </div>
                    </div>
                ) : (
                    <form className="d-flex flex-column justify-content-between" style={{ height: '100%' }}>
                        <div className="mb-4">
                            <h3 className="text-dark">{data.eventName}</h3>
                            <h4 type="datetime-local" className="text-dark">{actualDate}</h4>
                        </div>
                        <div className="mb-4">
                            <div className="mb-3 p-2 border rounded">
                                <div className="d-flex justify-content-between align-items-center mb-2 text-dark">
                                    <h3>{data.zoneLocation}</h3>
                                    <h3>{data.zonePrice}$</h3>
                                </div>
                                <div className="d-flex justify-content-between align-items-center mb-2 text-dark">
                                    <h4>Cantidad:</h4>
                                    <input
                                        type="number"
                                        placeholder="Tickets available"
                                        max={data.availableTickets}
                                        value={tickets}
                                        onChange={handleTickets}
                                        className="form-control justify-content-end w-50"
                                    />
                                </div>
                                <h5 className='text-dark'>Tickets disponibles: {data.availableTickets}</h5>
                            </div>
                            <div className="row d-flex justify-content-between text-dark">
                                <div className="col-6">
                                    <h2>Total:</h2>
                                </div>
                                <div className="col-6 d-flex justify-content-end">
                                    <h2>{totalPrice}$</h2>
                                </div>
                            </div>
                        </div>
                        <PrimaryButton type="button" onClick={makeReservation}>Realizar reserva</PrimaryButton>
                        <SecondaryButton className='mt-2' type="button" onClick={() => closeModal(false)}>Cancelar</SecondaryButton>
                    </form>
                )}
            </div>
        </div>
    );
}

export default ReservationModal;
