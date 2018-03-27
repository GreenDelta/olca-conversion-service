import * as React from "react";

import { FormatCombo } from "./FormatCombo";
import * as model from "../model/model";

interface Prop {
    setup: model.Setup;
    onChange: (setup: model.Setup) => void;
    onRun: () => void;
}

export class SetupPanel extends React.Component<Prop, {}> {

    public render() {
        const setup = this.props.setup;
        return (
            <form>
                <UrlBox url={setup.url}
                    onChange={(url) => this.onUrlChange(url)} />
                <FormatCombo isSource={true}
                    selected={setup.sourceFormat}
                    other={setup.targetFormat}
                    onSelet={(f) => this.onSourceFormatChange(f)} />
                <FormatCombo isSource={false}
                    selected={setup.targetFormat}
                    other={setup.sourceFormat}
                    onSelet={(f) => this.onTargetFormatChange(f)} />
                <div className="text-center">
                    <input className="app-button btn btn-outline-secondary"
                        value="Convert it!" type="button"
                        onClick={() => this.props.onRun()} />
                </div>
            </form>
        );
    }

    private onUrlChange(url: string) {
        const sourceFormat = this.predictSourceFormat(url);
        const targetFormat = this.getOtherFormat(sourceFormat);
        this.props.onChange({
            url, sourceFormat, targetFormat,
        });
    }

    private onSourceFormatChange(sourceFormat: model.Format) {
        const targetFormat = this.getOtherFormat(sourceFormat);
        this.props.onChange({
            url: this.props.setup.url,
            sourceFormat, targetFormat,
        });
    }

    private onTargetFormatChange(targetFormat: model.Format) {
        this.props.onChange({
            url: this.props.setup.url,
            sourceFormat: this.props.setup.sourceFormat,
            targetFormat,
        });
    }

    private predictSourceFormat(url: string): model.Format {
        if (!url) {
            return model.Format.ECOSPOLD_1;
        }
        if (url.indexOf("/processes/") >= 0) {
            return model.Format.ILCD;
        }
        if (url.indexOf("/PROCESS/") >= 0) {
            return model.Format.JSON_LD;
        }
        if (url.indexOf(".spold") >= 0) {
            return model.Format.ECOSPOLD_2;
        }
        if (url.indexOf(".CSV") >= 0 || url.indexOf(".csv") >= 0) {
            return model.Format.SIMAPRO_CSV;
        }
        return model.Format.ECOSPOLD_1;
    }

    private getOtherFormat(f: model.Format): model.Format {
        for (const other of model.FORMATS) {
            if (other !== f) {
                return other;
            }
        }
        return f;
    }

}

const UrlBox: React.SFC<{
    url: string,
    onChange: (url: string) => void,
}> = (props) => {
    return (
        <div className="form-group">
            <label htmlFor="urlField">Data Set URL</label>
            <input
                className="form-control"
                id="urlField" type="text"
                value={props.url}
                onChange={(e) => props.onChange(e.target.value)} />
        </div>
    );
};
