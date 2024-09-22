import React, { useState, useEffect } from 'react';
import {BrowserRouter, Route, Routes, useNavigate} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Home from "./components/home/Home";
import StatsOverview from "./components/stadistics/StatsOverview";
import Footer from "./components/Footer";
import Header from "./components/Header";
import Menu from "./components/Menu";

function App() {
    const [menuOpen, setMenuOpen] = useState(false);

    const toggleMenu = () => {
        setMenuOpen(!menuOpen);
    };

  return (
      <BrowserRouter>
          <div className="d-flex flex-column min-vh-100">
              {/* Header */}
              <Header toggleMenu={toggleMenu}/>

              {/* Menu */}
              <Menu menuOpen={menuOpen}/>

              <Routes>
                  <Route path="/" element={<Home />}/>
                  <Route path="/stats" element={<StatsOverview />}/>
              </Routes>
          </div>

          {/* Footer */}
          <Footer/>

      </BrowserRouter>
);
}

export default App;
