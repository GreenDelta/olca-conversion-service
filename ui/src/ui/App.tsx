import * as React from "react";

export class App extends React.Component<{}, {}> {

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
                                <label htmlFor="sourceCombo">Source Format</label>
                                <select id="sourceCombo">
                                    <option value="EcoSpold 1">EcoSpold 1</option>
                                    <option value="ILCD">ILCD</option>
                                    <option value="JSON-LD">JSON-LD</option>
                                </select>
                                <label htmlFor="targetCombo">Target Format</label>
                                <select id="targetCombo">
                                    <option value="EcoSpold 1">EcoSpold 1</option>
                                    <option value="ILCD">ILCD</option>
                                    <option value="JSON-LD">JSON-LD</option>
                                </select>
                                <input className="button button-outline app-button-blue" value="Convert it!" type="submit" />
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}
