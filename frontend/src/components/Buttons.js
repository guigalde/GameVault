

import { Link } from 'react-router-dom';
import { UserContext } from '../helpers/user_context';
import { useContext, useState } from 'react';
import { request } from '../helpers/axios_helper';

export default function Buttons({ onLogOutClick }) {

  const {user, setUser} = useContext(UserContext);
  const [showDropdown, setShowDropdown] = useState(false);

  const handleLogOutClick = () => {
    if (typeof onLogOutClick === 'function') {
      onLogOutClick();
    }
  };

  function handleDeleteAccount() {
    request(
      'DELETE',
      'api/users/' + user.id
      ).then((response) => {
        alert(response.data);
        window.localStorage.removeItem("auth_token");
        setUser({
          isLogged: false,
          username: "",
          email: "",
          role: ""
        });
      }).catch((error) => {alert(error)});
  }

  return (
    <div className="row">
      <div className="col-md-12 text-center" style={{ marginTop: '30px' }}>
        {!user.isLogged ? (
          <Link to="/login">
            <div className="SignInHeader">
              Sign in
            </div>
          </Link>
        ) : (
          <>
            <div className="SignInHeader" onClick={()=>{setShowDropdown(!showDropdown);console.log(showDropdown)}}>{user.username}</div>
              {showDropdown && 
                  <>
                  <Link to="/editAccount">
                    <DropdownItem text = {"Edit profile"}/>
                  </Link>
                  <DropdownItem onClick = {handleDeleteAccount} text = {"Delete account"}/>
                  <DropdownItem onClick={handleLogOutClick} text = {"Log out"}/>
                  </>
                }
          </>
        )}
      </div>
    </div>
 );
};

function DropdownItem({onClick, text}){
  return(
      <b onClick = {onClick}> {text} </b>
  );
}
  

