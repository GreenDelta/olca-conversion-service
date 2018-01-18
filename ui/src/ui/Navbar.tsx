import * as React from "react";

export class NavBar extends React.Component<{}, {}> {

    public render() {
        return (
            <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                <div className="container">
                    <a href="#" className="navbar-brand">ConvLCA</a>
                    <button className="navbar-toggler"
                        type="button" data-toggle="collapse"
                        data-target="#navContent"
                        aria-controls="navContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navContent">
                        <ul className="navbar-nav ml-auto">
                            <li className="nav-item active">
                                <a className="nav-link"
                                    href="https://github.com/GreenDelta/olca-conversion-service">
                                    <i className="fa fa-github" aria-hidden="true" />
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        );
    }
}
