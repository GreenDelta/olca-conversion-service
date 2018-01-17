import * as React from "react";
import * as Prism from "prismjs";

import * as model from "../model/model";

interface Prop {
    result: model.Result;
}

export class ResultPanel extends React.Component<Prop, {}> {

    public render() {
        const r = this.props.result;
        const code = Prism.highlight(r.process, Prism.languages.xml);
        return (
            <div>
                <div className="alert alert-success" role="alert">
                    The data set was conmverted successfully.{" "}
                    <a href={`/api/result/${r.zipFile}`}
                        title={r.zipFile}
                        className="alert-link">Click here to download it.</a>
                </div>
                <pre>
                    <code className="language-xml">{r.process}</code>
                </pre>
                <div dangerouslySetInnerHTML={{__html: code}} />
            </div>
        );
    }
}
