import { FormatCombo } from "./FormatCombo";
import * as React from "react";

import * as model from "../model/model";

export class App extends React.Component<{}, model.ConversionInfo> {

    constructor() {
        super();
        this.state = {
            url: "http://eplca.jrc.ec.europa.eu/ELCD3/resource/processes/1a7da06d-e8b7-4ff1-920c-209e9009dbe0",
            sourceFormat: model.Format.ILCD,
            targetFormat: model.Format.JSON_LD,
        };
    }

    public render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="column">
                        <h1 className="main-header">openLCA Data Conversion Service</h1>
                    </div>
                </div>
                <div className="row">
                    <div className="column">
                        <form>
                            <fieldset>
                                <label htmlFor="urlField">Data Set URL</label>
                                <input
                                    id="urlField" type="text"
                                    value={this.state.url}
                                    onChange={(e) => this.setState({ url: e.target.value })} />

                                <FormatCombo isSource={true}
                                    selected={this.state.sourceFormat}
                                    other={this.state.targetFormat}
                                    onSelet={(f) => this.setState({ sourceFormat: f })} />

                                <FormatCombo isSource={false}
                                    selected={this.state.targetFormat}
                                    other={this.state.sourceFormat}
                                    onSelet={(f) => this.setState({ targetFormat: f })} />

                                <input className="button button-outline app-button-blue"
                                    value="Convert it!" type="button"
                                    onClick={() => this.runConversion()} />
                            </fieldset>
                        </form>
                    </div>
                </div>
                <div className="row">
                    <p style={{color: "red"}}>{this.state.error}</p>
                </div>
            </div>
        );
    }

    private runConversion() {
        const req = new XMLHttpRequest();
        req.open("POST", "/api/convert");
        req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        req.onload = () => {
            if (req.status !== 200) {
                this.setState({error: req.responseText});
            }
        };
        req.send(JSON.stringify(this.state));
    }
}
