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
import EventCreation from './components/EventCreation';
import StatsOverview from "./components/stadistics/StatsOverview";

function App() {
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };
  
  return (
    <div className='d-flex flex-column'>

    <Router>

      <Header toggleMenu={toggleMenu} />
      <Menu menuOpen={menuOpen} />

      <div className="d-flex flex-column min-vh-100">
        
        <Routes>
          <Route path="/" element={<Home/>} />
          <Route path='/login' element={<Login/>}/>
          <Route path='/signup' element={<Registration/>}/>
          <Route path='/createEvent' element={<EventCreation/>}></Route>
          <Route path="/stats" element={<StatsOverview />}/>
          {/* <Route path='/event/:id' element={<Home eventos={eventos}/>}/> */}
        </Routes>

      </div>
      <Footer />  

    </Router> 
    </div>

    
  );
}

export default App;
