import logo from './logo.png';
import './App.css';
import {Routes, Route} from 'react-router-dom';
import Header from './components/Header';
import { WelcomeContent } from './components/WelcomeContent';
import LoginForm from './components/user_related/LoginForm';
import RegisterForm from './components/user_related/RegisterForm';
import EditForm from './components/user_related/EditProfile';
import VideogameList from './components/videogames/VideogameList';
import PersonalVideogameList from './components/personal_videogames/PersonalVideogameList';
import Wishlist from './components/Wishlist';
import { getUserInfo } from './helpers/axios_helper';
import ListMyCollections from './components/collections/ListMyCollections';
import VideogameDetails from './components/videogames/VideogameDetails';
import PersonalVideogameDetails from './components/personal_videogames/PersonalVideogameDetails';
import CollectionDetails from './components/collections/CollectionDetails';
import ErrorPage from './components/ErrorPage';
import {ErrorBoundary} from 'react-error-boundary';
import React from 'react';

export default function App() {
  const JWTExpirationDate = new Date(getUserInfo().exp*1000);
  const currentDate = new Date();
  if (JWTExpirationDate < currentDate) {
    window.localStorage.removeItem('auth_token');
  }
  return (
      <ErrorBoundary FallbackComponent={ErrorPage}>
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
              <Route path="/myGames" exact element={<PersonalVideogameList/>}/>
              <Route path="/wishlist" exact element={<Wishlist/>}/>
              <Route path="/collections" exact element={<ListMyCollections/>}/>
              <Route path="/videogameDetails/:gameId" element={<VideogameDetails/>}/>
              <Route path="/personalVideogameDetails/:gameId" element={<PersonalVideogameDetails/>}/>
              <Route path="/collection/:collectionId" element={<CollectionDetails/>}/>
              <Route path="/error" element={<ErrorPage/>}/>
            </Routes>
          </div>
        </div>
      </ErrorBoundary>
        );
}
