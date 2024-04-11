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
import VideogameList from './components/VideogameList';
import { useNavigate } from 'react-router-dom';

function App() {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    username: "",
    email: "",
    password: "",
    role: ""
  });
  

  return (
    <UserContext.Provider value={{user, setUser}}>
      <div className="App">
        <Header 
          pageTitle= "GameVault" 
          logoSrc= {logo}
        />
        <div className="AppContent" style={{height:'100%', width:'100%'}}>
          <Routes>
            <Route path="/login" exact element={<LoginForm/>}/>          
            <Route path="/register" exact element={<RegisterForm/>}/>
            <Route path="/" exact element={<WelcomeContent/>}/>
            <Route path="/editAccount" exact element={<EditForm/>}/>
            <Route path="/videogames" exact element={<VideogameList/>}/>
          </Routes>
        </div>
      </div>
      
    </UserContext.Provider>
  );
}

export default App;