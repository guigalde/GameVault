import Buttons from './Buttons';
import { Link } from 'react-router-dom';
import { useContext } from 'react';
import { UserContext } from '../helpers/user_context';
import { request } from '../helpers/axios_helper';

export default function Header({logoSrc, pageTitle}) {

  const { user, setUser } = useContext(UserContext);
  function handleVideogameLoad() {
    alert("This procedure may take a few minutes. Please wait.");
    request(
      'GET',
      '/api/populateVideogames'
      ).then((response) => {
        alert(response.data);
      }).catch((error) => {alert(error)});
  }
    return (
      <nav className="d-flex justify-content-between align-items-center App-header">
        <div className="d-flex align-items-center">
          <Link to="/">
            <img src={logoSrc} className="App-logo" alt="logo" />
          </Link>
          <h1 className="App-title">{pageTitle}</h1>
        </div>
          {user.role==="ADMIN" &&
              <button className="btn btn-primary btn-block mb-4"  onClick = {handleVideogameLoad}style={{ color: 'white', backgroundColor: '#AE3C7A', borderColor: 'black' }}><b>Load database</b></button>
            }
          <Buttons  />
      </nav>
    );
  }