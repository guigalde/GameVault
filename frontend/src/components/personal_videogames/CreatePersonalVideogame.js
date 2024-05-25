import {useState} from 'react';
import {request, getUserInfo} from '../../helpers/axios_helper';
import {useNavigate} from 'react-router-dom';
import { validateMyGame } from '../../helpers/mygame_validation';
import FormError from '../FormError';

export default function AddToMyGamesForm({gameName, gameId, setShowForm, isFromWishlist, retrieveGames}) {
    const user = {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };
    const [error, setError] = useState({});
    const [completed, setCompleted] = useState(false);
    const navigate = useNavigate();
    const [personalVideogame, setPersonalVideogame] = useState({
        videogameId: gameId,
        timePlayed : null,
        mark: null,
        acquiredOn: null,
        completedOn: null,
        completionTime: null,
        platform: "",
        notes: ""
    });


    

    function handleAddToMyGamesSubmit(event){
        setError(validateMyGame(personalVideogame));
        event.preventDefault();
        if(error.timePlayedError === "" && error.markError === "" && error.completionTimeError === "" && error.completedOnError === "" && error.acquiredOnError === "" && error.platformError === ""){
            request(
            'POST',
            'api/addPersonalVideogame/'+user.id,
            personalVideogame
            ).then((response) => {
                alert(response.data);
                setShowForm(false);
                navigate("/mygames");
            }).catch((error) => {
                alert(error);
            });
            if(isFromWishlist===true){
                request(
                    'DELETE',
                    'api/deleteFromWishlist/'+user.id+'/'+gameId
                ).then((response) => {
                    retrieveGames();
                }).catch((error) => {
                    alert(error);
                });
            }
        }

    }

    return (
        <div className='popup'>
            <div className='popup-inner'>
                <h2 className="text-center d-flex justify-content-center">Add {gameName} to my games</h2>
                <form onSubmit={handleAddToMyGamesSubmit}>
                    <div className="form-group">
                        <label>Time played:</label>
                        <FormError error={error.timePlayedError}/>
                        <input type="number" className="form-control" value={personalVideogame.timePlayed} onChange={(e) => setPersonalVideogame({...personalVideogame,timePlayed: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <label>Mark:</label>
                        <FormError error={error.markError}/>
                        <input type="number" className="form-control custom-select" value={personalVideogame.mark} onChange={(e) => setPersonalVideogame({...personalVideogame,mark: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <label>Completed:</label>
                        <input type='checkbox' onChange={(e) => setCompleted(e.target.checked)}/>
                    </div>
                    {completed &&
                        <>
                        <div className="form-group">
                            <label>Completion time:</label>
                            <FormError error={error.completionTimeError}/>
                            <input type="number" className="form-control" value={personalVideogame.completionTime} onChange={(e) => setPersonalVideogame({...personalVideogame,completionTime: e.target.value})}/>
                        </div>
                        <div className="form-group">
                            <label>Completed on:</label>
                            <FormError error={error.completedOnError}/>
                            <input type="date" className="form-control" value={personalVideogame.completedOn} onChange={(e) => setPersonalVideogame({...personalVideogame,completedOn: e.target.value})}/>
                        </div>
                        </>
                    }
                    <div className="form-group">
                        <label>Acquired on:</label>
                        <FormError error={error.acquiredOnError}/>
                        <input type="date" className="form-control" value={personalVideogame.acquiredOn} onChange={(e) => setPersonalVideogame({...personalVideogame,acquiredOn: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <label>Platform:</label>
                        <FormError error={error.platformError}/>
                        <input type="text" className="form-control" value={personalVideogame.platform} onChange={(e) => setPersonalVideogame({...personalVideogame,platform: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <label>Notes:</label>
                        <textarea type= 'text-area' className="form-control" value={personalVideogame.notes} onChange={(e) => setPersonalVideogame({...personalVideogame,notes: e.target.value})}/>
                    </div>
                    <button type="submit" className="btn btn-primary">Add to my games</button>
                    <button type="button" className="btn btn-danger" onClick={()=>setShowForm(false)}>Cancel</button>
                </form>
            </div>
        </div>
    );    
}