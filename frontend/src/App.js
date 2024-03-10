import logo from './logo.png';
import './App.css';
import {Routes, Route} from 'react-router-dom';
import { useState } from 'react';
import Header from './components/Header';
import { UserContext } from './helpers/user_context';
import { WelcomeContent } from './components/WelcomeContent';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import EditForm from './components/EditProfile';

function App() {
  const [user, setUser] = useState({
    username: "",
    email: "",
    password: "",
    role: ""
  });
  function onLogOutClick(){
    setUser({
      isLogged: false,
      username: "",
      email: "",
      role: ""
    });
    window.localStorage.removeItem("auth_token");
  }

  return (
    <UserContext.Provider value={{user, setUser}}>
      <div className="App">
        <Header 
          pageTitle= "GameVault" 
          logoSrc= {logo}
          onLogOutClick= {onLogOutClick}
        />
        <div className="AppContent">
          <Routes>
            <Route path="/login" exact element={<LoginForm/>}/>          
            <Route path="/register" exact element={<RegisterForm/>}/>
            <Route path="/" exact element={<WelcomeContent/>}/>
            <Route path="/editAccount" exact element={<EditForm/>}/>
          </Routes>
        </div>
      </div>
      
    </UserContext.Provider>
  );
}

export default App;