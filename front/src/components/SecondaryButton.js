import React from 'react';

const SecondaryButton = ({ onClick, children, type = 'button', className = '', isDisabled = false}) => {
  return (
    <button
      disabled={isDisabled}
      type={type}
      onClick={onClick}
      className={`btn ${className}`}
      style={{
        backgroundColor: '#bfc9ca',
        color: 'black',
        width: '100%',
        border: 'none',
        borderRadius: '0.25rem',
        padding: '0.5rem 1rem',
        cursor: 'pointer',
        transition: 'background-color 0.3s',
      }}
      onMouseOver={(e) => (e.currentTarget.style.backgroundColor = '#aab7b8')}
      onMouseOut={(e) => (e.currentTarget.style.backgroundColor = '#bfc9ca')}
    >
      {children}
    </button>
  );
};

export default SecondaryButton;