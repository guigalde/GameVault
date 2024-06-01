import {useState} from 'react';
import {request} from '../../helpers/axios_helper';
import { useNavigate } from 'react-router-dom';

export default function SearchVideogameDropdown({videogames, setShowForm}){
    
    const [searchGame, setSearchGame] = useState('');
    const navigate = useNavigate();

    async function handleSaveGame(e){
        e.preventDefault();
        const matchingGame = videogames.find(videogame => videogame.name === searchGame);
        if(!matchingGame){
            alert("Collection not found");
            return;
        }
        try{
            const response = await request("POST", "/api/videogame/addGame", matchingGame, navigate);
            if(response.status === 200){
                alert(response.data);
                setShowForm(false);
            }else{
                alert(response.data);
            }
        }catch(error){
            navigate("/error")
        }

    }
    
    return (
        <div className='popup'>
             <div className='popup-inner'>
                 <h2 className="text-center d-flex justify-content-center" style={{alignItems: "center", color:'black'}}>Select the game you want:</h2>
                 <form className = "form-collections"onSubmit={handleSaveGame}>
                     <div className="form-group">
                         <datalist id="games" className="scrollable-datalist">
                             {videogames.map(videogame => 
                                 <option key={videogame.id} value={videogame.name} />)}
                         </datalist>
                         <input type="text" list="games" className="form-control" value={searchGame} onChange={(e)=>setSearchGame(e.target.value)}/>
                     </div>
                     <div style={{display: "flex", justifyContent: "center",alignItems: "center"}}>
                         <button type="submit" className="btn btn-primary">Add</button>
                         <button type="button" className="btn btn-danger" onClick={()=>setShowForm(false)}>Cancel</button>
                     </div>
                 </form>
             </div>
         </div>
    )
}