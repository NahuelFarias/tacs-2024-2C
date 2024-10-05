
import {React} from "react";
import { Card, Button } from "react-bootstrap";
import './TicketCard.css'
import {useNavigate} from "react-router-dom";

const TicketCard = ({ ticketId, reservationDate, title, locationName, price, eventId}) => {

    const navigate = useNavigate();  // Hook para redirigir

    const handleViewEvent = () => {
        navigate(`/eventDetails/${eventId}`);  // Redirigir a la página del evento con el ID
    };
    // Supongamos que reservationDate está en formato 'YYYY-MM-DD HH:mm:ss'
    const [date, timeMiliseconds] = reservationDate.split('T'); // Separa la fecha y la hora
    const [time] = timeMiliseconds.split('.');
    return (
        <Card className="my-3 myTicket">
            <Card.Body>
                <Card.Title className="card-title" >
                    Reservation for "{title}"
                </Card.Title>
                <hr />
                <Card.Text className="myTicket-text">
                    <strong>Date:</strong> {date}
                </Card.Text>
                <Card.Text>
                    <strong>Time:</strong> {time}
                </Card.Text>
                <Card.Text>
                    <strong>Price:</strong> ${price}
                </Card.Text>
                <Card.Text>
                    <strong>Location:</strong> {locationName}
                </Card.Text>
                <Button variant="dark" className="w-100" onClick={handleViewEvent}>View Event</Button>
            </Card.Body>
        </Card>

    );
};

export default TicketCard;