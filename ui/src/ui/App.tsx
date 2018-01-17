import * as React from "react";
import * as ReactDOM from "react-dom";

import { SetupPanel } from "./SetupPanel";
import { ResultPanel } from "./ResultPanel";
import * as model from "../model/model";
import * as components from "./components";

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
            },
            running: false,
        };
    }

    public render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col">
                        <h3 className="main-header">openLCA Data Conversion</h3>
                    </div>
                </div>
                <div className="row">
                    <div className="col">
                        <SetupPanel
                            setup={this.state.setup}
                            onChange={(setup) => this.setState({ setup })}
                            onRun={() => this.runConversion()} />
                    </div>
                </div>
                <div className="row">
                    <div className="col result-box">
                        {this.getResultBox()}
                    </div>
                </div>
            </div>
        );
    }

    private getResultBox(): JSX.Element {
        if (this.state.error) {
            return <components.ErrorBox error={this.state.error} />;
        }
        if (this.state.result) {
            return <ResultPanel result={this.state.result} />;
        }
        if (this.state.running) {
            return <components.ProgressBox />;
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
