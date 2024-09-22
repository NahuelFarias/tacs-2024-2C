import React, {useEffect, useState} from 'react';
import {getEvents} from "../../services/eventService";
import SearchBar from "../SearchBar";
import EventCard from "../EventCard";

const Home = () => {
    const [eventos, setEventos] = useState([]);  // Estado para los eventos


    useEffect(() => {
        getEvents()
            .then(data => {
                setEventos(data);
            })
            .catch(error => console.error('Error fetching events:', error));
    }, []);

    return (
        <div className="home-page">
            <SearchBar />

            <div className="events-section container">
                <h2 className="text-white">Eventos Destacados</h2>
                <div className="row">
                    {/* Mapeo de los eventos obtenidos desde el backend */}
                    {eventos.map(evento => (
                        <EventCard key={evento.id} title={evento.nombre}/>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Home;
