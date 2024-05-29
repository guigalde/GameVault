import {useState, useEffect} from 'react';
import {request, getUserInfo} from '../../helpers/axios_helper';
import {useNavigate} from 'react-router-dom';
import { validateMyGame } from '../../helpers/mygame_validation';
import FormError from '../FormError';

export default function AddToMyGamesForm({gameName, gameId, setShowForm, isFromWishlist, dataRetrieve, personalVideogameToEdit=null}) {

    const user = {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };

    const [error, setError] = useState({});
    const [completed, setCompleted] = useState(false);
    const navigate = useNavigate();
    const isEdit = useState(!personalVideogameToEdit);
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


    
    function handleSubmit(){
        setError(validateMyGame(personalVideogame));
        if(error.timePlayedError === "" && error.markError === "" && error.completionTimeError === "" && error.completedOnError === "" && error.acquiredOnError === "" && error.platformError === ""){
            if(isEdit){
                handleEdit();
            }else{
                handleAddToMyGames();
            }
        }

    }
    async function handleAddToMyGames(){ 
        
        await request(
        'POST',
        'api/addPersonalVideogame/'+user.id,
        personalVideogame, navigate
        ).then((response) => {
            alert(response.data);
            setShowForm(false);
            navigate("/mygames");
        }).catch((error) => {
            navigate('/error');        
        });
        if(isFromWishlist===true){
            await request(
                'DELETE',
                'api/deleteFromWishlist/'+user.id+'/'+gameId, null , navigate
            ).then((response) => {
                dataRetrieve();
            }).catch((error) => {
                alert(error);
                navigate('/error');
            });
        }

    }

    async function handleEdit(){
        console.log(personalVideogame);
        try{
            const response = await request("POST", "/api/personalVideogame/update/"+personalVideogame.id+"/"+ user.id , personalVideogame, navigate);
            if(response.status === 200){
                setShowForm(false);
                alert(response.data);
                dataRetrieve();
            }
        }catch(error){
            console.log(error);
            alert("Failed to update personal videogame:", error);
        }
    }

    useEffect(() => {
        if(isEdit){

            /*In the backend the DTO for the personal videogame has the acquiredOn and completedOn for recieving data from the frontend and 
            completedOnString and acquiredOnString for sending it to the front to display, in the edit case which wasnt contemplated before 
            I solve the problem as follows in the ifs*/
            let formattedAcquiredOn = null;
            if(personalVideogameToEdit.acquiredOn !== null){
                let partsOfAcquiredOn = personalVideogameToEdit.acquiredOn.split("/");
                formattedAcquiredOn = `${partsOfAcquiredOn[2]}-${partsOfAcquiredOn[1]}-${partsOfAcquiredOn[0]}`;
            }

            let formattedCompletedOn = null;
            if(personalVideogameToEdit.completedOn !== null ){
                let partsOfCompletedOn = personalVideogameToEdit.completedOn.split("/");
                formattedCompletedOn = `${partsOfCompletedOn[2]}-${partsOfCompletedOn[1]}-${partsOfCompletedOn[0]}`;
            }
            
            setPersonalVideogame({
                id: personalVideogameToEdit.id,
                videogameId: personalVideogameToEdit.videogame.id,
                timePlayed: personalVideogameToEdit.timePlayed,
                mark: personalVideogameToEdit.mark,
                acquiredOn: formattedAcquiredOn,
                completedOn: formattedCompletedOn,
                completionTime: personalVideogameToEdit.completionTime,
                platform: personalVideogameToEdit.platform,
                notes: personalVideogameToEdit.notes
            });
            setCompleted(personalVideogameToEdit.completedOn !== null || personalVideogameToEdit.completionTime !== null);
        }
    }, [personalVideogameToEdit]);

    return (
        <div className='popup'>
            <div className='popup-inner'>
                {isEdit ? 
                <h2 className="text-center d-flex justify-content-center">Edit {gameName}</h2>
                :
                <h2 className="text-center d-flex justify-content-center">Add {gameName} to my games</h2>
                }
                <form>
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
                    <button type="submit" className="btn btn-primary" onClick={(e)=>{e.preventDefault();handleSubmit()}}>{isEdit?"Save changes":"Add to my games"}</button>
                    <button type="button" className="btn btn-danger" onClick={()=>setShowForm(false)}>Cancel</button>
                </form>
            </div>
        </div>
    );    
}