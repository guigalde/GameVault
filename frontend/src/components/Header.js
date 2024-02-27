import * as react from 'react';
import Buttons from './Buttons';

export default function Header(props){
    return (
        <header className='App-header'>
            <img src={props.logoSrc} className="App-logo" alt="logo" />
            <h1 className="App-title">{props.pageTitle}</h1>
        </header>
    );
}