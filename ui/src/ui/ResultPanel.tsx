import * as React from "react";
import * as Prism from "prismjs";

import * as model from "../model/model";

interface Prop {
    result: model.Result;
}

export class ResultPanel extends React.Component<Prop, {}> {

    public render() {
        const r = this.props.result;
        const source = this.format(r.process, r.format);
        const lang = r.format === model.Format.JSON_LD ?
            Prism.languages.javascript : Prism.languages.xml;
        const code = Prism.highlight(source, lang);
        return (
            <div>
                <div className="alert alert-success message-box" role="alert">
                    The data set was converted successfully.{" "}
                    <a href={`/api/result/${r.zipFile}`}
                        title={r.zipFile}
                        className="alert-link">Click here to download it.</a>
                </div>
                <pre>
                    <code dangerouslySetInnerHTML={{ __html: code }} />
                </pre>
            </div>
        );
    }

    private format(source: string, format: model.Format): string {
        if (!source) {
            return "";
        }
        if (format !== model.Format.JSON_LD) {
            return source;
        }
        const obj = JSON.parse(source);
        return JSON.stringify(obj, null, "  ");
    }
}
