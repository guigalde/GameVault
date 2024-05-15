

import { Link } from 'react-router-dom';
import { request, getUserInfo} from '../../helpers/axios_helper';
import { useNavigate } from 'react-router-dom';

export default function Buttons() {

  const user = {
    id: getUserInfo().id,
    username: getUserInfo().sub,
    email: getUserInfo().email,
    role: getUserInfo().role
};


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
        window.location.reload();
      }).catch((error) => {alert(error)});
  }
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
    
      <div>
        {!user.username ? (
          <Link to="/login">
            <button className="btn btn-secondary" type="button"  style={{ color: 'white', backgroundColor: '#3CACAE', borderColor: 'black' }}>
                <b>Sign in</b>
            </button>
          </Link>
        ) : (
          <>
              {user.role==="ADMIN" &&
              <button className="btn btn-primary btn-block mb-4"  onClick = {handleVideogameLoad}style={{ color: 'white', backgroundColor: '#AE3C7A', borderColor: 'black' }}><b>Load database</b></button>
             }
            <div className="dropdown">
              <button className="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style={{ color: 'white', backgroundColor: '#3CACAE', borderColor: 'black' }}>
                <b>{user.username}</b>
              </button>
              <div className="dropdown-menu" aria-labelledby="dropdownMenuButton" style={{ color: 'black', backgroundColor: '#C8F4F9', borderColor: 'black' }}>
                    <Link to="/editAccount" style={{ textDecoration: 'none' }}>
                      <b className="dropdown-item">Edit profile</b>
                    </Link>
                    <b className="dropdown-item" onClick = {handleDeleteAccount}>Delete account</b>
                    <b className="dropdown-item" onClick={onLogOutClick}>Log out</b>
              </div>
              </div>
              </>
        )}
      </div>
 );
};
  

