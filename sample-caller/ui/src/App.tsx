import React from "react";
import {BrowserRouter, Routes, Link, Route} from "react-router-dom";
import {MainForm} from "./main-view/MainView"


const App = () => (
  <BrowserRouter>
    <div>
      <nav className="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <Link to="/" className="navbar-brand">Service To Service Call Client</Link>
        <button className="navbar-toggler" type="button" data-toggle="collapse"
                data-target="#navbarsExampleDefault"
                aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarsExampleDefault">
          <ul className="navbar-nav mr-auto">
            <li className="nav-item active">
              <Link to="/" className="nav-link">Home</Link>
            </li>
          </ul>
        </div>
      </nav>
      <div className="containerFluid">
        <div className="row">
          <div className="col-sm-10 offset-sm-1">
            <Routes>
            <Route path="/" element={<MainForm />}/>
            </Routes>
          </div>

        </div>
      </div>
    </div>
    </BrowserRouter>
);

export default App;
