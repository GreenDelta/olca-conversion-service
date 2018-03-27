import * as React from "react";
import * as ReactDOM from "react-dom";

import { SetupPanel } from "./SetupPanel";
import { ResultPanel } from "./ResultPanel";
import * as model from "../model/model";
import { NavBar } from "./Navbar";
import { APIPanel } from "./APIPanel";
import { Footer } from "./Footer";
import { MessagePanel } from "./MessagePanel";

interface State {
    setup: model.Setup;
    result?: model.Result;
    error?: string;
    running: boolean;
}

export class App extends React.Component<{}, State> {

    constructor() {
        super();
        this.state = {
            setup: {
                url: "http://eplca.jrc.ec.europa.eu/ELCD3/resource/processes/1a7da06d-e8b7-4ff1-920c-209e9009dbe0",
                sourceFormat: model.Format.ILCD,
                targetFormat: model.Format.JSON_LD,
                refSystem: "default",
            },
            running: false,
        };
    }

    public render() {
        return (
            <div>
                <NavBar />
                <div className="container">
                    <div className="row">
                        <div className="col-md-6">
                            <h3 className="main-header">
                                ConvLCA is an open source conversion service for
                                LCA data based on <a className="main-link" href="http://www.openlca.org/">openLCA</a>
                            </h3>
                            <SetupPanel
                                setup={this.state.setup}
                                onChange={(setup) => this.setState({ setup })}
                                onRun={() => this.runConversion()} />
                            <MessagePanel {...this.state} />
                        </div>
                        <div className="col-md-6">
                            <APIPanel setup={this.state.setup}/>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col result-box">
                            {this.getResultPanel()}
                        </div>
                    </div>
                </div>
                <Footer />
            </div>
        );
    }

    private getResultPanel(): JSX.Element {
        if (this.state.result) {
            return <ResultPanel result={this.state.result} />;
        }
        return <p />;
    }

    private runConversion() {
        this.setState({ error: null, result: null, running: true });
        const req = new XMLHttpRequest();
        req.open("POST", "/api/convert");
        req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        req.onload = () => {
            if (req.status !== 200) {
                this.setState({
                    error: req.responseText,
                    result: null,
                    running: false,
                });
            } else {
                const result = JSON.parse(req.responseText);
                console.log(result);
                this.setState({
                    result,
                    error: null,
                    running: false,
                });
            }
        };
        req.send(JSON.stringify(this.state.setup));
    }
}
