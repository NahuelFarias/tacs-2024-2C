import React from "react";
import { Card, Button, Accordion } from "react-bootstrap";
import './TicketCard.css';
import { useNavigate } from "react-router-dom";


const TicketCard = ({ eventId, title, eventDate, locations, totalPrice }) => {

    const navigate = useNavigate();

    const handleViewEvent = () => {
        navigate(`/eventDetails/${eventId}`);
    };

    const [datePart, timeMiliseconds] = eventDate ? eventDate.split('T') : ['', ''];
    const [time] = timeMiliseconds ? timeMiliseconds.split('.') : [''];

    const formatDate = (dateString) => {
        const parts = dateString.split('-');
        if (parts.length === 3) {
            return `${parts[0]}/${parts[1]}/${parts[2]}`;
        }
        return dateString;
    };

    return (
        <Card className="my-3 myTicket">
            <Card.Body>
                <Card.Title className="card-title">
                    {title} - {formatDate(datePart)}
                </Card.Title>
                <hr />
                <Card.Text>
                    <strong>Hora:</strong> {time}
                </Card.Text>
                <Card.Text className="myTicket-text">
                    <strong>Ubicaciones:</strong>
                    <Accordion className="mb-3">
                        <Accordion.Item eventKey="0">
                            <Accordion.Header>Ver ubicaciones</Accordion.Header>
                            <Accordion.Body>
                                <table className="table table-hover">
                                    <thead>
                                        <tr>
                                            <th scope="col">Nombre</th>
                                            <th scope="col">Cantidad de Tickets</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {locations.map((loc, index) => (
                                            <tr key={index}>
                                                <td>{loc.name}</td>
                                                <td className="item_table">{loc.reservations.length}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </Accordion.Body>
                        </Accordion.Item>
                    </Accordion>
                </Card.Text>
                <Card.Text className="myTicket-text">
                    <strong>Precio final:</strong> ${totalPrice}
                </Card.Text>
                <Button variant="dark" className="w-100" onClick={handleViewEvent}>Ver evento</Button>
            </Card.Body>
        </Card>
    );
};

export default TicketCard;
