import * as React from "react";

export class Footer extends React.Component<{}, {}> {
    public render() {
        return (
            <footer className="footer">
                <div className="container">
                    <a className="footer-link" href="https://www.greendelta.com/">&copy; GreenDelta 2018</a>
                </div>
            </footer>
        );
    }
}
