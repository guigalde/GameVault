import Buttons from './Buttons';
import { Link } from 'react-router-dom';

export default function Header({logoSrc, pageTitle}) {

    return (
      <nav className="d-flex justify-content-between align-items-center App-header">
        <div className="d-flex align-items-center">
          <Link to="/">
            <img src={logoSrc} className="App-logo" alt="logo" />
          </Link>
          <h1 className="App-title">{pageTitle}</h1>
        </div>
          <Buttons  />
      </nav>
    );
  }