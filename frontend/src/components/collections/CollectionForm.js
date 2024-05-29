import {useState, useEffect} from 'react';
import {request, getUserInfo} from '../../helpers/axios_helper';
import FormError from '../FormError';
import {useNavigate} from 'react-router-dom';


export default function CreateCollectionForm({setShowForm, dataRetrieve, collectionToEdit}) {

    const user = {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };

    const [collection, setCollection] = useState({
        name: "",
        description: "",
        collectionGames: []
    });
    const navigate = useNavigate();

    const [games, setGames] = useState([]);
    const [selectedGames, setSelectedGames] = useState([]);
    const [searchGame, setSearchGame] = useState('');
    const [gameNames, setGameNames] = useState('');
    const [error, setError] = useState('');
    //Added to use useEffect on this and avoid the asyncronous state update problems
    const [collectionToSend, setCollectionToSend] = useState(null);
    const [readyToSend, setReadyToSend] = useState(false);

    async function retrieveMyGames(){
        try{
            const response = await request("GET", "/api/myGamesNames/"+user.id, null, navigate)
            if(response.status === 200){
                setGames(response.data);
            }else{
                console.log("Error");
            }
        }catch(error){
            navigate("/error")
        }
    }

    function handleCreateCollection(data) {
        request('POST', '/api/collections/create/'+user.id, data, navigate)
            .then(response => {
                if(response.status === 200) {
                    alert(response.data);
                    setShowForm(false);
                    dataRetrieve();
                } else {
                    alert(response.data);
                }
            })
            .catch(error => {
                navigate("/error");
            });
        setReadyToSend(false);
        setCollectionToSend(null);
    }

    async function handleEditCollection(data) {
        console.log(data);
        try{
            const response = await request("POST", "/api/collections/update/"+user.id, data, navigate);
            if(response.status === 200){
                setShowForm(false);
                dataRetrieve();
                alert(response.data);
            }else{
                alert(response.data);
            }
        }
        catch(error){
            console.log(error);
            navigate("/error");
        }
        setReadyToSend(false);
        setCollectionToSend(null);
    }

    function handleSubmit(e){
        e.preventDefault();
        if(collection.name === '') {
            setError('Name is required');
            return;
        }
        setCollectionToSend({
                id: collection.id,
                name: collection.name,
                description: collection.description,
                collectionGames: selectedGames
          });
        setReadyToSend(true);
        //The sending method is executed in a useEffect to avoid asyncronous state update problems
    }

    function handleInputChange(e){
        const value = e.target.value;
        const matchingGame = games.find(game => game.name === value);
        if (matchingGame) {
            if(selectedGames.includes(matchingGame)){
                alert("Game already added");
                return;
            }
            setSelectedGames([...selectedGames, matchingGame]);
            if(gameNames === ''){
                setGameNames(matchingGame.name);
            }else{
                setGameNames(gameNames+", "+matchingGame.name);
            }
        } else {
            setSearchGame(value);
        }
    };

    useEffect(() => {
        retrieveMyGames();
        if(collectionToEdit){
            setCollection(collectionToEdit);
            setSelectedGames(collectionToEdit.collectionGames.map(game => ({id: game.id, name: game.videogame.name})));
            setGameNames(collectionToEdit.collectionGames.map(game => game.videogame.name).join(', '));
        }
    }, []);

    

    useEffect(() => {
        if(readyToSend === true && collectionToSend !== null){
            if(collectionToEdit){
                handleEditCollection(collectionToSend);
            }else{
                handleCreateCollection(collectionToSend);
            }
        }
    },[readyToSend, collectionToSend]);
    
    useEffect(() => {
        console.log(selectedGames);
    }, [selectedGames]);



    return (
        <div className='popup'>
            <div className='popup-inner'>
                <h2 className="text-center d-flex justify-content-center" style={{alignItems: "center"}}>{collectionToEdit?"Update collection":"Create collection"}</h2>
                <form className = "form-collections">
                    <div className="form-group">
                        <label>Name:</label>
                        <FormError error={error}/>
                        <input type="text" className="form-control" value={collection.name} onChange={(e) => setCollection({...collection,name: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <label>Description:</label>
                        <textarea type= 'text-area' className="form-control" value={collection.description} onChange={(e) => setCollection({...collection,description: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <label>Games:</label>
                        <p>{gameNames}</p>
                        <datalist id="games" className="scrollable-datalist">
                            {games.map(game => 
                                <option key={game.id} value={game.name} />)}
                        </datalist>
                        <input type="text" list="games" className="form-control" value={searchGame} onChange={(e)=>handleInputChange(e)}/>
                    </div>
                    <div style={{display: "flex", justifyContent: "center",alignItems: "center"}}>
                        <button type="submit" className="btn btn-primary" onClick={(e)=>handleSubmit(e)}>{collectionToEdit?"Save changes":"Create"}</button>
                        <button type="button" className="btn btn-danger" onClick={()=>setShowForm(false)}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};