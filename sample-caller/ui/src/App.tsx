import React from "react";
import {Link, Outlet} from "react-router-dom";


const App: () => React.JSX.Element = () => (
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
                    <li className="nav-item active">
                        <Link to="/help" className="nav-link">help</Link>
                    </li>
                </ul>
            </div>
        </nav>
        <div className="containerFluid">
            <div className="row">
                <div className="col-sm-10 offset-sm-1">
                    <Outlet/>
                </div>
            </div>
        </div>
    </div>
);

export default App;
