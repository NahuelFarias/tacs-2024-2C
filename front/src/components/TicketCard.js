
import {React} from "react";

const TicketCard = ({ ticketId, reservationDate, title, locationName, price }) => {
    // Supongamos que reservationDate está en formato 'YYYY-MM-DD HH:mm:ss'
    const [date, timeMiliseconds] = reservationDate.split('T'); // Separa la fecha y la hora
    const [time] = timeMiliseconds.split('.');
    return (
        <div className="col-md-4 mb-4">
            <div className="card text-white" style={{ backgroundColor: '#343a40' }}>
                <div className="card-body">
                    <h5 className="card-title mb-4">Reservation</h5>

                    <ul className="list-group mt-4">
                        <li className="list-group-item bg-dark text-white border-0">Event: {title}</li>
                        <li className="list-group-item bg-dark text-white border-0">Location: {locationName}</li>
                        <li className="list-group-item bg-dark text-white border-0">Reservation Date: {date}</li>
                        <li className="list-group-item bg-dark text-white border-0">Time: {time}</li>
                        <li className="list-group-item bg-dark text-white border-0">Price: ${price}</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default TicketCard;