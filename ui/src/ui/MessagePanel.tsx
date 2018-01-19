import * as React from "react";

import * as model from "../model/model";

interface Props {
    running: boolean;
    error?: string;
    result?: model.Result;
}

export class MessagePanel extends React.Component<Props, {}> {

    public render() {
        if (this.props.running) {
            return <ProgressBox />;
        }
        if (this.props.error) {
            return <ErrorBox error={this.props.error} />;
        }
        if (this.props.result) {
            return <ResultInfo r={this.props.result} />;
        }
        return <p />;
    }
}

const ResultInfo: React.SFC<{ r: model.Result }> = (props) => {
    return (
        <div className="alert alert-success message-box" role="alert">
            The data set was converted successfully.{" "}
            <a href={`/api/result/${props.r.zipFile}`}
                title={props.r.zipFile}
                className="alert-link">Click here to download it</a> or see
                the converted process data set below.
        </div>
    );
};

const ErrorBox: React.SFC<{ error: string }> = (props) => {
    return (
        <div className="alert alert-danger message-box" role="alert">
            Conversion failed: {props.error}
        </div>
    );
};

const ProgressBox: React.SFC<{}> = () => {
    return (
        <div className="alert alert-light message-box" role="alert">
            Converting ...
            <div>
                <img src="progress.gif" />
            </div>
        </div>
    );
};
