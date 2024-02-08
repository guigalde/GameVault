import './App.css'
import logo from '../logo.svg'
import Header from './Header.js'

function App(){
    return (
        <div>
            <Header pageTitle="GameVault" logoSrc={logo}/>
        </div>
    )
}

export default App