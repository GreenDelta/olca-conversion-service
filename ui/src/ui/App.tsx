import { FormatCombo } from "./FormatCombo";
import * as React from "react";

import { Format } from "../model/model";

interface State {
    sourceFormat: Format;
    targetFormat: Format;
}

export class App extends React.Component<{}, State> {

    constructor() {
        super();
        this.state = {
            sourceFormat: Format.ILCD,
            targetFormat: Format.JSON_LD,
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
                                <input placeholder="Full data set url ..." id="urlField" type="text" />

                                <FormatCombo isSource={true}
                                    selected={this.state.sourceFormat}
                                    other={this.state.targetFormat}
                                    onSelet={(f) => this.setState({sourceFormat: f})} />

                                <FormatCombo isSource={false}
                                    selected={this.state.targetFormat}
                                    other={this.state.sourceFormat}
                                    onSelet={(f) => this.setState({targetFormat: f})} />

                                <input className="button button-outline app-button-blue" value="Convert it!" type="submit" />
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}
