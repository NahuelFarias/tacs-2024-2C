import React from "react";
import { Card, Button, Accordion } from "react-bootstrap";
import './TicketCard.css';
import { useNavigate } from "react-router-dom";

const TicketCard = ({ eventId, title, reservationDate, locations, totalPrice }) => {
    const navigate = useNavigate();  // Hook para redirigir

    const handleViewEvent = () => {
        navigate(`/eventDetails/${eventId}`);  // Redirigir a la página del evento con el ID
    };

    // Supongamos que reservationDate está en formato 'YYYY-MM-DD HH:mm:ss'
    const [date, timeMiliseconds] = reservationDate.split('T'); // Separa la fecha y la hora
    const [time] = timeMiliseconds.split('.');

    const formatDate = (dateString) => {
        // Separa la cadena por el carácter '-'
        const parts = dateString.split('-');
        
        // Verifica que la fecha tenga tres partes
        if (parts.length === 3) {
          return `${parts[0]}/${parts[1]}/${parts[2]}`; // Devuelve el formato deseado
        }
        
        return dateString; // Devuelve la fecha original si no está en el formato esperado
      };



    return (
        <Card className="my-3 myTicket">
            <Card.Body>
                <Card.Title className="card-title">
                    {title} - {formatDate(date)}
                </Card.Title>
                <hr />
                <Card.Text>
                    <strong>Hora:</strong> {time}
                </Card.Text>

                {/* Accordion para mostrar ubicaciones como tabla */}
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
                                                <td className="item_table">{loc.count}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </Accordion.Body>
                        </Accordion.Item>
                    </Accordion>
                </Card.Text>

                {/* Precio total por evento */}
                <Card.Text className="myTicket-text">
                    <strong>Precio final:</strong> ${totalPrice}
                </Card.Text>
                <Button variant="dark" className="w-100" onClick={handleViewEvent}>Ver evento</Button>
            </Card.Body>
        </Card>
    );
};

export default TicketCard;
