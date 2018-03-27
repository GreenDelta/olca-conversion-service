import * as React from "react";
import * as Prism from "prismjs";

import * as model from "../model/model";

interface Props {
    setup: model.Setup;
}

const API_ENDPOINT = "http://localhost/api/";

export class APIPanel extends React.Component<Props, {}> {

    public render() {
        const code = this.pythonSource();
        return (
            <div className="api-panel">
                <div className="card">
                    <div className="card-header">
                        The conversion service provides a simple web API which
                        you can access from any language; e.g. from Python:
                    </div>
                    <div className="card-body">
                        <pre>
                            <code dangerouslySetInnerHTML={{ __html: code }} />
                        </pre>
                    </div>
                </div>
            </div>
        );
    }

    private pythonSource() {
        const setup = this.props.setup;
        const source = `
import requests

API_ENDPOINT = "${API_ENDPOINT}"

def main():
    # convert a data set
    setup = {
        "url": "${setup.url}",
        "sourceFormat": "${setup.sourceFormat}",
        "targetFormat": "${setup.targetFormat}",
        "refSystem": "${setup.refSystem}",
    }
    r = requests.post(API_ENDPOINT + "convert", json=setup)
    if r.status_code != 200:
        print("Conversion failed: " + r.text)
        exit(1)
    data = r.json()

    # download the data set
    r = requests.get(API_ENDPOINT + "result/" + data["zipFile"], stream=True)
    with open(data["zipFile"], 'wb') as f:
        for chunk in r:
            f.write(chunk)

if __name__ == "__main__":
    main()`.trim();
        return Prism.highlight(source, Prism.languages.python);
    }

}
