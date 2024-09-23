import React from 'react';

const PrimaryButton = ({ onClick, children, type = 'button', className = '', isDisabled = false}) => {
  return (
    <button
      disabled={isDisabled}
      type={type}
      onClick={onClick}
      className={`btn ${className}`}
      style={{
        backgroundColor: '#ff4f92',
        color: 'white',
        width: '100%',
        border: 'none',
        borderRadius: '0.25rem',
        padding: '0.5rem 1rem',
        cursor: 'pointer',
        transition: 'background-color 0.3s',
      }}
      onMouseOver={(e) => (e.currentTarget.style.backgroundColor = '#e03f7e')}
      onMouseOut={(e) => (e.currentTarget.style.backgroundColor = '#ff4f92')}
    >
      {children}
    </button>
  );
};

export default PrimaryButton;