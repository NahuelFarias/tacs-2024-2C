import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Menu from './components/Menu';
import Footer from './components/Footer';
import Registration from './components/Registration';
import Home from './components/Home';
import Login from './components/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };
  
  return (
    <Router>
    <div className="d-flex flex-column min-vh-100">
    
      <Header toggleMenu={toggleMenu} />

      <Menu menuOpen={menuOpen} />
      <Routes>
        <Route path="/" element={<Home/>} />
        <Route path='/login' element={<Login/>}/>
        <Route path='/register' element={<Registration/>}/>
        {/* <Route path='/event/:id' element={<Home eventos={eventos}/>}/> */}
      </Routes>

      <Footer />
    </div>
    </Router>
    
  );
}

export default App;
