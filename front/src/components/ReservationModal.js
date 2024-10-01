import {React, useState, useEffect, useRef} from 'react';
import PrimaryButton from './PrimaryButton';
import SecondaryButton from './SecondaryButton';
import { tryCreateReservation } from '../services/reservationService'
import './ReservationModal.css'

const ReservationModal = ({closeModal, data}) => {
/*    const [totalPrice, setTotalprice] = useState(data.zonePrice);*/
    const reservation = {  };
    
    const actualDate = new Date(data.eventDate).toLocaleString().slice(0, -3).concat("hs");

/*    const [tickets, setTickets] = useState(1);
    const handleTickets = (e) => {
        setTotalprice(e.target.value*data.zonePrice);
        setTickets(e.target.value);
    }*/
    
    let reservationModalRef = useRef();

    useEffect(() => {
        let closeReservationModal = (e) => {
            if(!reservationModalRef.current.contains(e.target)){
                closeModal(false);
            }
        };
    
        document.addEventListener("mousedown", closeReservationModal);

        return() =>{
            document.removeEventListener("mousedown", closeReservationModal);
        }
    });

    const makeReservation = () => {
        console.log(data.eventId, data.zoneLocation)
        tryCreateReservation(data.eventId, data.zoneLocation);
    }

    
    return (
        <div className="d-flex modal-Background justify-content-center align-items-center">
            <div className="modal-Container p-5 m-5 rounded shadow" ref={reservationModalRef}>
                <h1 className="text-center text-dark mb-4">Reserva tus asientos</h1>
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
                            </div >
                            <div className="d-flex justify-content-between align-items-center mb-2 text-dark">
{/*                                <h4>Cantidad:</h4>
                                <input
                                    type="number"
                                    placeholder="Tickets available"
                                    max={data.availableTickets}
                                 value={tickets}
                                    onChange={handleTickets}
                                    className="form-control justify-content-end w-50"
                                    />*/}
                            </div>
                            <h5 className='text-dark'>Tickets disponibles: {data.availableTickets}</h5>
                        </div>
                    <div className="row d-flex justify-content-between text-dark">
                        <div className="col-6">
                            <h2>Total:</h2>
                        </div>
                        <div className="col-6 d-flex justify-content-end">
                            <h2>{data.zonePrice}$</h2>
                        </div>
                    </div>
                    </div>
        
                    <PrimaryButton type="button" onClick={()=> makeReservation()}>Create Event</PrimaryButton>
                    <SecondaryButton className='mt-2' type="button" onClick={() => closeModal(false)}>Cancel</SecondaryButton>
                </form>
            </div>
        </div>
    );
}

export default ReservationModal;