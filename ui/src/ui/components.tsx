import * as React from "react";

export const UrlBox: React.SFC<{
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
