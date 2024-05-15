import logo from './logo.png';
import './App.css';
import {Routes, Route} from 'react-router-dom';
import Header from './components/navbar/Header';
import { WelcomeContent } from './components/WelcomeContent';
import LoginForm from './components/user_related/LoginForm';
import RegisterForm from './components/user_related/RegisterForm';
import EditForm from './components/user_related/EditProfile';
import VideogameList from './components/videogames/VideogameList';
import MyGamesList from './components/my_games/MyGamesList';
import Wishlist from './components/Wishlist';
import { getUserInfo } from './helpers/axios_helper';
import ListMyCollections from './components/collections/ListMyCollections';

function App() {
  const JWTExpirationDate = new Date(getUserInfo().exp*1000);
  const currentDate = new Date();
  if (JWTExpirationDate < currentDate) {
    window.localStorage.removeItem('auth_token');
  }
  return (
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
            <Route path="/myGames" exact element={<MyGamesList/>}/>
            <Route path="/wishlist" exact element={<Wishlist/>}/>
            <Route path="/collections" exact element={<ListMyCollections/>}/>
          </Routes>
        </div>
      </div>
        );
}

export default App;