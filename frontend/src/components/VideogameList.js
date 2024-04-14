
import { request, getUserInfo} from '../helpers/axios_helper';
import { useState, useEffect } from 'react';
import AddToMyGamesForm from './AddToMyGamesForm.js';

export default function VideogameList(){
    const [filters, setFilters] = useState({});
    const [provisionalFilters, setProvisionalFilters] = useState({
        searchTerms: "",
        publisher: "",
        minReleaseDate: null,
        maxReleaseDate: null,
        genre: "",
        platform: ""

    });
    const user = {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };
    const [page,setPage] = useState(0);
    const [games, setGames] = useState([]);
    const [totalPages, setTotalPages] = useState(0);

    const currentPage = page +1;
    const previousPage = page;
    const nextPage = page + 2;

    const [genres, setGenres] = useState([]);
    const [platforms, setPlatforms] = useState([]);
    const [publishers, setPublishers] = useState([]);

    const [showPlatforms, setShowPlatforms] = useState(false);
    const [showGenres, setShowGenres] = useState(false);
    const [showPublishers, setShowPublishers] = useState(false);

    const [showForm, setShowForm] = useState(false);
    const [gameId, setGameId] = useState(null);
    const [gameName, setGameName] = useState(null);
    

    async function retrieveGames(){
        console.log(filters)
        const response = await request("POST", "/api/videogames/"+page,
        filters)
        if(response.status === 200){
            setGames(response.data[0]);
            setTotalPages(response.data[1]);
        }else{
            console.log("Error");
        }
    }

    async function retrieveGenres(){
        const response = await request("GET", "/api/videogames/genres", null);
        if(response.status === 200){
            setGenres(response.data);
        }
    }

    async function retrievePlatforms(){
        const response = await request("GET", "/api/videogames/platforms", null);
        if(response.status === 200){
            setPlatforms(response.data);
        }
    }

    async function retrievePublishers(){
        const response = await request("GET", "/api/videogames/publishers", null);
        if(response.status === 200){
            setPublishers(response.data);
        }
    
    }

    useEffect(() => {
        retrieveGames();
        retrieveGenres();
        retrievePlatforms();
        retrievePublishers();
    }, [filters, page]);

    useEffect(() => {
        setPage(0);
    }, [filters]);

    const searchedGenres = genres.filter(g => g.toLowerCase().includes(provisionalFilters.genre.toLowerCase()));
    const searchedPlatforms = platforms.filter(p => p.toLowerCase().includes(provisionalFilters.platform.toLowerCase()));
    const searchedPublishers = publishers.filter(p => p.toLowerCase().includes(provisionalFilters.publisher.toLowerCase()));
    return(
        
        <div style={{display: 'flex', flexDirection: 'row', height:'100%', width:'100%'}}>
            <div className="filters d-flex justify-content-center" style={{width: '15%', height: '100%', display:'felx', flexDirection:'column'}}>
                <form style={{padding: '10px'}}>
                    <h3>Filters</h3>
                    <div className="form-group">
                        <label htmlFor="searchterms"><h6>Search</h6></label>
                        <input type="text" className="form-control" id="name" onChange={(e) => setProvisionalFilters({...provisionalFilters, searchTerms: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <h6>Release Date</h6>
                        <label htmlFor="minReleaseDate"> Lower limit</label>
                        <input type="date" className="form-control" id="minReleaseDate" onChange={(e) => setProvisionalFilters({...provisionalFilters, minReleaseDate: e.target.value})}/>
                        <label htmlFor="maxReleaseDate"> Upper limit</label>
                        <input type="date" className="form-control" id="maxReleaseDate" onChange={(e) => setProvisionalFilters({...provisionalFilters, maxReleaseDate: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="genres"><h6>Genres</h6></label>
                        <input type="text" className="form-control" id="genres" value = {provisionalFilters.genre} 
                        onChange={(e) => setProvisionalFilters({...provisionalFilters, genre: e.target.value})}
                        onFocus={()=>setShowGenres(true)}
                        onBlur={()=>setTimeout(setShowGenres(false),100)} />
                        <div>
                            {showGenres &&  searchedGenres.length !==1 && searchedGenres.map(genre => (
                                <div key={genre}  onMouseDown={(e) => e.preventDefault()} onClick={() => setProvisionalFilters({...provisionalFilters, genre: genre})}>{genre}</div>
                            ))}
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="platforms"><h6>Platforms</h6></label>
                        <input type="text" className="form-control" id="platforms" value={provisionalFilters.platform} onChange={(e) => setProvisionalFilters({...provisionalFilters, platform:e.target.value})}
                        onFocus={()=>setShowPlatforms(true)}
                        onBlur={()=>setTimeout(setShowPlatforms(false),100)} />
                        <div>
                            {showPlatforms && searchedPlatforms.length !==1 && searchedPlatforms.map(platform => (
                                <div key={platform} onMouseDown={(e) => e.preventDefault()} onClick={() => setProvisionalFilters({...provisionalFilters, platform:platform})}>{platform}</div>
                            ))}
                        </div>
                    </div>
                    <div className="form-group">
                        <label htmlFor="publisher"><h6>Publisher</h6></label>
                        <input type="text" className="form-control" id="publisher" value={provisionalFilters.publisher} onChange={(e) => setProvisionalFilters({...provisionalFilters, publisher:e.target.value})}
                        onFocus={()=>setShowPublishers(true)}
                        onBlur={()=>setTimeout(setShowPublishers(false),100)} />
                        <div>
                            {showPublishers && searchedPublishers.length !==1 && searchedPublishers.map(publisher => (
                                <div key={publisher} onMouseDown={(e) => e.preventDefault()} onClick={() => setProvisionalFilters({...provisionalFilters, publisher:publisher})}>{publisher}</div>
                            ))}
                        </div>
                    </div>
                    <p></p>
                    <button type="submit" className="btn btn-primary" onClick={(e) => {e.preventDefault(); setFilters(provisionalFilters);}} style={{ marginRight: '20px' }} >Apply filters</button>
                    <button className="btn btn-danger" onClick={() => { setProvisionalFilters({searchTerms: "",publisher: "", minReleaseDate: null, maxReleaseDate: null, genre: "", platform: ""})}}>Delete filters</button>
                </form>
                <p></p>
                <nav aria-label="Page navigation example" style={{padding: '10px'}}>
                    <b>Page {currentPage} of {totalPages}</b>
                    <ul class="pagination">
                        <li class="page-item" onClick={()=>setPage(0)}>
                        <t class="page-link" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span> 
                        </t>
                        </li>
                        {previousPage > 0 &&
                        <li class="page-item" onClick={()=>setPage(page-1)}><t class="page-link">{previousPage}</t></li>}
                        <li class="page-item active"><t class="page-link">{currentPage}</t></li>
                        {nextPage <= totalPages &&
                        <li class="page-item"><t class="page-link" onClick={()=>setPage(page+1)}>{nextPage}</t></li>}
                        <li class="page-item" onClick={()=>setPage(totalPages-1)}>
                        <t class="page-link" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </t>
                        </li>
                    </ul>
                    </nav>
            </div>
            <div className="games d-flex justify-content-center" style={{width: '85%', height: '100%'}}>
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Platforms</th>
                            <th>Genres</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {games.map((game) => {
                            return (
                                <tr key={game.id}>
                                    <td>{game.name}</td>
                                    <td>{game.platforms}</td>
                                    <td>{game.genres}</td>
                                    <td>
                                        {user.username? <button className="btn btn-primary btn-block mb-4"  onClick = {()=>{setShowForm(true);setGameId(game.id); setGameName(game.name)}}style={{ color: 'black', backgroundColor: '#DC80D5', borderColor: 'black' }}><b>Add to my games</b></button>:null}
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
            {showForm && <AddToMyGamesForm gameId={gameId} setShowForm={setShowForm} gameName={gameName}/> }
        </div>
    );
}