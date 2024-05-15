import {useState, useEffect} from 'react';
import {request, getUserInfo} from '../../helpers/axios_helper';
import FormError from '../FormError';


export default function CreateCollectionForm({setShowForm, getCollections}) {
    const user = {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };
    const [collection, setCollection] = useState({
        name: "",
        description: "",
        creationDate: new Date(),
        lastUpdate: new Date(),
        collectionGames: []
    });

    const [games, setGames] = useState([]);
    const [selectedGames, setSelectedGames] = useState([]);
    const [searchGame, setSearchGame] = useState('');
    const [gameNames, setGameNames] = useState('');
    const [error, setError] = useState('');

    async function retrieveMyGames(){
        const response = await request("GET", "/api/myGamesNames/"+user.id)
        if(response.status === 200){
            setGames(response.data);
        }else{
            console.log("Error");
        }
    }

    function handleCreateCollection(e) {
        e.preventDefault();
        if(collection.name === '') {
            setError('Name is required');
            return;
        }
        setCollection({...collection, collectionGames: selectedGames});
        request('POST', '/api/collections/create/'+user.id, collection)
            .then(response => {
                if(response.status === 200) {
                    alert('Collection created successfully');
                    setShowForm(false);
                    getCollections();
                } else {
                    alert('Error creating collection');
                }
            })
            .catch(error => {
                console.log(error);
            });
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
    }, []);

    return (
        <div className='popup'>
            <div className='popup-inner'>
                <h2 className="text-center d-flex justify-content-center" style={{alignItems: "center"}}>Create collection</h2>
                <form className = "form-collections"onSubmit={handleCreateCollection}>
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
                        <button type="submit" className="btn btn-primary">Create</button>
                        <button type="button" className="btn btn-danger" onClick={()=>setShowForm(false)}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};