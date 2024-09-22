import React from 'react';
import './ErrorMessage.css';

const ErrorMessage = ({ message, onClose }) => (
  <div className="alert alert-danger mt-2 position-relative" role="alert">
    {message}
    <button type="button" className="close" onClick={onClose} aria-label="Close">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
);

export default ErrorMessage;