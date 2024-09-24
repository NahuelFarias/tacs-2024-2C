import React, {useEffect, useState} from 'react';
import { Chart } from "react-google-charts";
import {getStats} from "../../services/statsService";
import StatsDropdown from "./Dropdown";
import './Bar.css';

const getStatsData = (loginCount, ticketCount, eventCount) => {
    return [
        ["Tipo Estadistica", "Cantidad", {role: 'style'}],
        ["Cantidad de Inicios de Sesion", loginCount,
            "stroke-color: #7B25A2; stroke-opacity: 0.8; stroke-width: 8; fill-color: #FF4E92; fill-opacity: 0.9"],
        ["Cantidad de Tickets Vendidos", ticketCount,
            "stroke-color: #7B25A2; stroke-opacity: 0.8; stroke-width: 8; fill-color: #FF4E92; fill-opacity: 0.9"],
        ["Cantidad de Eventos", eventCount,
            "stroke-color: #7B25A2; stroke-opacity: 0.8; stroke-width: 8; fill-color: #FF4E92; fill-opacity: 0.9"]
    ];
}   

const StatsOverview = () => {
    const [filter, setFilter] = useState("Daily")
    const [stats, setStats] = useState([]);
    const [options, setOptions] = useState([])

    useEffect(() => {
        getStats()
            .then(data => {
                const values = data.find(f => f.timeRange === filter)
                const result = getStatsData(values.logins, values.tickets, values.events);
                setStats(result);
                setOptions({
                    title: "",
                        chartArea: {width: "68%", height: "70%"},
                    titleTextStyle: {
                        fontSize: 24,
                            color: "#FFFFFF"
                    },
                    legend: {
                        textStyle: {
                            color: "#FFFFFF"
                        },
                        position: "top"
                    },
                    colors: ['#FF4E92'],
                    hAxis: {
                        title: "Tipo Estadistica",
                            titleTextStyle: {
                            fontSize: 12,
                                color: "#FFFFFF"
                        },
                        textStyle:{
                            color: "#FFFFFF"
                        },
                        minValue: 0
                    },
                    vAxis: {
                        title: "Valor Numerico",
                            titleTextStyle: {
                            fontSize: 12,
                                color: "#FFFFFF"
                        },
                        viewWindow: {
                            min: 0,
                                max: Math.max(...result.slice(1).map(row => row[1])) + 10,
                        },
                        textStyle:{
                            color: "#FFFFFF"
                        }
                    },
                    backgroundColor: "#212529",
                });
            })
            .catch(error => console.error('Error fetching events:', error));
    }, [filter]);

    return (
        <div className="stats-overview container mt-4">

            <StatsDropdown onSelection={setFilter}/>

            <div className="stats-chart">
                <Chart
                    chartType="ColumnChart"
                    width="100%"
                    height="400px"
                    data={stats}
                    options={options}
                    backgroundColor="#6C757D"
                />
            </div>

            <div className="container mb-4">
                <h4 className="text-white mt-4 mb-2"> Aclaración:
                </h4>
                <p1 className="text-white">Los datos aqui mostrados corresponden a la plataforma a nivel global,
                    para consultar datos especificos de un evento particular, dirigase a la seccion del mismo utilizando
                    el buscador de Eventos, y seleccione la vista avanzada de estadisticas para el mismo.
                </p1>
            </div>
        </div>
    );
};

export default StatsOverview;
