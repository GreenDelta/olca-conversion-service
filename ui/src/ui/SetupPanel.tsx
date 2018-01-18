import * as React from "react";

import { FormatCombo } from "./FormatCombo";
import * as components from "./components";
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
                <components.UrlBox url={setup.url}
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
