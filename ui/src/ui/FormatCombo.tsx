import * as React from "react";

import * as model from "../model/model";

interface Props {
    isSource: boolean;
    selected: model.Format;
    other: model.Format;
    onSelet: (f: model.Format) => void;
}

export class FormatCombo extends React.Component<Props, {}> {

    public render() {
        const isSource = this.props.isSource;
        const id = isSource ? "sourceCombo" : "targetCombo";
        return (
            <div className="form-group">
                <label htmlFor={id}>{isSource ? "Source Format" : "Target Format"}</label>
                <select className="form-control" id={id}
                    onChange={(e) => this.fireSelect(e.target.value as model.Format)}
                    value={this.props.selected}>
                    {this.getFormats()}
                </select>
            </div>
        );
    }

    private getFormats(): JSX.Element[] {
        const isSource = this.props.isSource;
        const options = [];
        for (const f of model.FORMATS) {
            if (!isSource && f === this.props.other) {
                continue;
            }
            options.push(<option value={f}>{f}</option>);
        }
        return options;
    }

    private fireSelect(f: model.Format) {
        if (!f || f === this.props.selected) {
            return;
        }
        this.props.onSelet(f);
    }
}
