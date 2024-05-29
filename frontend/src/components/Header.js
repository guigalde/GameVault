import { Link } from 'react-router-dom';
import { getUserInfo, request } from '../helpers/axios_helper';
import { useNavigate, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';

export default function Header({logoSrc, pageTitle}) {

  const user = {
    id: getUserInfo().id,
    username: getUserInfo().sub,
    email: getUserInfo().email,
    role: getUserInfo().role
  };

  const location = useLocation();

  const [videogamesTabStyle, setVideogamesTabStyle] = useState({borderLeft: '1.5px solid black', borderRight: '1.5px solid black'});
  const [myGamesTabStyle, setMyGamesTabStyle] = useState({borderRight: '1.5px solid black', width: '10vh'});
  const [wishlistTabStyle, setWishlistTabStyle] = useState({borderRight: '1.5px solid black'});
  const [collectionsTabStyle, setCollectionsTabStyle] = useState({borderRight: '1.5px solid black'});
   

  const navigate = useNavigate();

  function onLogOutClick(){
    window.localStorage.removeItem("auth_token");
    navigate("/");
    window.location.reload();
  }

  function handleDeleteAccount() {
    request(
      'DELETE',
      'api/users/' + user.id
      ).then((response) => {
        alert(response.data);
        window.localStorage.removeItem("auth_token");
        navigate("/");
      }).catch((error) => {navigate("/error")});
  }
  function handleVideogameLoad() {
    alert("This procedure may take a few minutes. Please wait.");
    request(
      'GET',
      '/api/populateVideogames'
      ).then((response) => {
        alert(response.data);
      }).catch((error) => {navigate("/error")});
  }

  //Reactive header colors
  useEffect(() => {
    if (location.pathname === "/videogames" || location.pathname === "/videogameDetails/*"){
      setVideogamesTabStyle({borderLeft: '1.5px solid black', borderRight: '1.5px solid black', backgroundColor: '#EEB5EB'});
      setMyGamesTabStyle({borderRight: '1.5px solid black'});
      setWishlistTabStyle({borderRight: '1.5px solid black'});
      setCollectionsTabStyle({borderRight: '1.5px solid black'});
    }else if (location.pathname === "/myGames" || location.pathname === "/personalVideogameDetails/*"){
      setMyGamesTabStyle({borderRight: '1.5px solid black', backgroundColor: '#EEB5EB'});
      setVideogamesTabStyle({borderLeft: '1.5px solid black', borderRight: '1.5px solid black'});
      setWishlistTabStyle({borderRight: '1.5px solid black'});
      setCollectionsTabStyle({borderRight: '1.5px solid black'});
    }else if (location.pathname === "/wishlist"){
      setWishlistTabStyle({borderRight: '1.5px solid black', backgroundColor: '#EEB5EB'});
      setVideogamesTabStyle({borderLeft: '1.5px solid black', borderRight: '1.5px solid black'});
      setMyGamesTabStyle({borderRight: '1.5px solid black'});
      setCollectionsTabStyle({borderRight: '1.5px solid black'});
    }else if (location.pathname === "/collections" || location.pathname === "/collection/*"){
      setCollectionsTabStyle({borderRight: '1.5px solid black', backgroundColor: '#EEB5EB'});
      setVideogamesTabStyle({borderLeft: '1.5px solid black', borderRight: '1.5px solid black'});
      setMyGamesTabStyle({borderRight: '1.5px solid black'});
      setWishlistTabStyle({borderRight: '1.5px solid black'});
    }
  }, [location]);
  
    return (
      <nav className="d-flex justify-content-between align-items-center App-header">
        <div className="d-flex align-items-center" style={{height:'100%'}}>
          <Link className="logo-container" to="/">
            <img src={logoSrc} className="App-logo" alt="logo" />
          </Link>
          <h1 className="App-title" style={{fontWeight:"600"}}>{pageTitle}</h1>
          <Link to="/videogames" className="link">
            <div className="nav-item" style={videogamesTabStyle}>
              <b style={{ padding: '20px' }}>Videogames</b>
            </div>
          </Link>
          {user.id !== null &&
            <>
              <Link to="/myGames" className="link">
                <div className="nav-item" style={myGamesTabStyle}>
                  <b style={{ padding: '20px' }}>My Games</b>
                </div>
              </Link>
              <Link to="/wishlist" className="link">
                <div className="nav-item" style={wishlistTabStyle}>
                  <b style={{ padding: '20px' }}>Wishlist</b>
                </div>
              </Link>
              <Link to="/collections" className="link">
                <div className="nav-item" style={collectionsTabStyle}>
                  <b style={{ padding: '20px' }}>Collections</b>
                </div>
              </Link>
            </>
          }
        </div>
        <div className="align-items-center" style={{
                width: '30%',
                display: 'flex',
                justifyContent: 'flex-end',
                alignItems: 'center',
                flexWrap: 'wrap'
              }}>
          {!user.username ? (
            <Link to="/login">
              <button className="btn btn-secondary" type="button" style={{ color: 'white', backgroundColor: '#3CACAE', borderColor: 'black' }}>
                <b>Sign in</b>
              </button>
            </Link>
          ) : (
            <>
              {user.role === "ADMIN" &&
                <button className="btn btn-primary btn-block" onClick={handleVideogameLoad} style={{ color: 'white', backgroundColor: '#AE3C7A', borderColor: 'black', marginRight:"3%" }}><b>Load database</b></button>
              }
              <div className="dropdown">
                <button className="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style={{ color: 'white', backgroundColor: '#3CACAE', borderColor: 'black' }}>
                  <b>{user.username}</b>
                </button>
                <div className="dropdown-menu" aria-labelledby="dropdownMenuButton" style={{ color: 'black', backgroundColor: '#C8F4F9', borderColor: 'black' }}>
                  <Link to="/editAccount" style={{ textDecoration: 'none' }}>
                    <b className="dropdown-item">Edit profile</b>
                  </Link>
                  <b className="dropdown-item" style={{cursor: 'pointer'}} onClick={handleDeleteAccount}>Delete account</b>
                  <b className="dropdown-item" style={{cursor: 'pointer'}} onClick={onLogOutClick}>Log out</b>
                </div>
              </div>
            </>
          )}
        </div>
      </nav>
    );
  }