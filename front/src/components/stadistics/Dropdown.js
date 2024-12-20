import React, { useState } from 'react';
import {Link} from "react-router-dom";

const StatsDropdown = ({onSelection}) => {
    const [selection, setSelection] = useState("Daily");

    const selectionHandler = (event) => {
        const selectedValue = event.target.textContent;
        setSelection(selectedValue);
        onSelection(selectedValue);
    };

    return (
        <div className="container">
            <div className="row align-items-center">
                <div className="col">
                    <h2 className="text-white mb-4 mt-2">Estadisticas de Plataforma</h2>
                </div>
                <div className="col-auto d-flex align-items-center">
                    <label htmlFor="dropdownMenuButton" className="me-2 mb-4 mt-2">Seleccionar Temporalidad:</label>
                    <div className="dropdown mb-4 mt-2">
                        <button className="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                                data-bs-toggle="dropdown" aria-expanded="false">
                            {selection}
                        </button>
                        <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton">
                            <li><Link className="dropdown-item" to="." onClick={selectionHandler}>Daily</Link></li>
                            <li><Link className="dropdown-item" to="." onClick={selectionHandler}>Weekly</Link></li>
                            <li><Link className="dropdown-item" to="." onClick={selectionHandler}>Yearly</Link>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default StatsDropdown;
