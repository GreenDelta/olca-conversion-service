import * as React from "react";
import * as Prism from "prismjs";

import * as model from "../model/model";

interface Props {
    setup: model.Setup;
}

export class APIPanel extends React.Component<Props, {}> {

    public render() {
        const code = this.pythonSource();
        return (
            <div className="api-panel">
                <div className="card">
                    <div className="card-header">
                        Python
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
        const source = `import requests

API_ENDPOINT = "http://localhost/"

def main():
    setup = {
        "url": "${setup.url}",
        "sourceFormat": "${setup.sourceFormat}",
        "targetFormat": "${setup.targetFormat}"
    }
    response = requests.post(API_ENDPOINT + "convert", json=setup)
    if response.status_code != 200:
        print("Conversion failed: " + response.text)
        exit(1)

if __name__ == '__main__':
    main()`;
        return Prism.highlight(source, Prism.languages.python);
    }

}
