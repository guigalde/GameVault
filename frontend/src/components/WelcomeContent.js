import * as React from 'react';

export default class WelcomeContent extends React.Component {

  render() {
    return (
        <div className="row justify-content-md-center">
            <div className="jumbotron jumbotron-fluid">
              <div className="container">
                <h1 className="display-4">Welcome to GameVault, an app made for managing videogame collections!</h1>
                <p className="lead">Login to get the full experience.</p>
              </div>
            </div>
        </div>
    );
  };
}