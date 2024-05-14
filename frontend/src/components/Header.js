import Buttons from './Buttons';
import { Link } from 'react-router-dom';
import { getUserInfo } from '../helpers/axios_helper';

export default function Header({logoSrc, pageTitle}) {

  const user = {
    id: getUserInfo().id,
    username: getUserInfo().sub,
    email: getUserInfo().email,
    role: getUserInfo().role
  };
  
    return (
      <nav className= "d-flex justify-content-between align-items-center App-header">
        <div className="d-flex align-items-center">
          <Link to="/">
            <img src={logoSrc} className="App-logo" alt="logo" />
          </Link>
          <h1 className="App-title">{pageTitle}</h1>
          <Link to="/videogames" className="link">
            <div className="nav-item" style={{borderLeft:'1.5px solid black' , borderRight:'1.5px solid black'}}>
              <b style={{padding:'20px'}}>Videogames</b>
            </div>
          </Link>
          {user.id!==null && 
          <>
          <Link to="/myGames" className="link">
            <div className="nav-item" style={{borderRight:'1.5px solid black'}}>
              <b style={{padding:'20px'}}>My Games</b>
            </div>
          </Link>
          <Link to="/wishlist" className="link">
            <div className="nav-item" style={{borderRight:'1.5px solid black'}}>
              <b style={{padding:'20px'}}>Wishlist</b>
            </div>
          </Link>
          <Link to="/collections" className="link">
            <div className="nav-item" style={{borderRight:'1.5px solid black'}}>
              <b style={{padding:'20px'}}>Collections</b>
            </div>
          </Link>
          </>
          }
        </div>
        <Buttons/>
      </nav>
    );
  }