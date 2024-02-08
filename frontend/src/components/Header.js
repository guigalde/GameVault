import * as react from 'react';

export default function Header(props){
    return (
        <header>
            <img src={props.logoSrc} alt ="logo"/>
            <h1>{props.pageTitle}</h1>
        </header>
    );
}