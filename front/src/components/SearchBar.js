import React from 'react';

const SearchBar = () => (
  <div className="search-bar p-3">
    <input type="text" className="form-control" placeholder="Buscar eventos o artistas..." style={{ maxWidth: "600px", margin: "0 auto" }}/>
  </div>
);

export default SearchBar;
