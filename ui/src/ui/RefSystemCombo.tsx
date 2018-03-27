import * as React from "react";

interface Props {
    onChange: (fn: string) => void;
}

interface State {
    selected: string;
    refSystems: string[];
}

export class RefSystemCombo extends React.Component<Props, State> {

    constructor(props) {
        super(props);
        this.state = {
            selected: "default",
            refSystems: ["default"],
        };
        const req = new XMLHttpRequest();
        req.open("GET", "/api/refsystems");
        req.onload = () => {
            if (req.status === 200) {
                this.setState({ refSystems: JSON.parse(req.responseText) });
            }
        };
        req.send();
    }

    public render() {
        const options = [];
        for (const refSystem of this.state.refSystems) {
            options.push(<option value={refSystem}>{refSystem}</option>);
        }
        return (
            <div className="form-group">
                <label htmlFor="refSystemCombo">
                    Reference system
                </label>
                <select className="form-control" id="refSystemCombo"
                    onChange={(e) => this.props.onChange(e.target.value)}
                    value={this.state.selected}>
                    {options}
                </select>
            </div>
        );
    }
}
