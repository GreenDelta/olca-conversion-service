import * as React from "react";
import * as ReactDOM from "react-dom";

import { App } from "./ui/App";

export function main() {
    ReactDOM.render(
        <App />,
        document.getElementById("app-root"),
    );
}
