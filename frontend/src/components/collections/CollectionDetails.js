import {useState, useEffect} from 'react';
import { request, getUserInfo } from '../../helpers/axios_helper';
import { useParams, useNavigate } from 'react-router-dom';
import { Icon } from '@iconify-icon/react';
import { Link } from 'react-router-dom';
import ConfirmDelete from "../ConfirmDeleteModal";


export default function CollectionDetails(){

    const { collectionId } = useParams();
    const user = {
        id: getUserInfo().id,
        username: getUserInfo().username,
        email: getUserInfo().email,
        role: getUserInfo().role,
    };

    const navigate = useNavigate();

    const [filters, setFilters] = useState({
        timePlayedSort: null,
        markSort: null
    });
    const [provisionalFilters, setProvisionalFilters] = useState({
        searchTerms: "",
        publisher: "",
        minReleaseDate: null,
        maxReleaseDate: null,
        genre: "",
        platform: "",
        minMark: null,
        maxMark: null,
        minTimePlayed: null,
        maxTimePlayed: null,
        completed: null,
        timePlayedSort: null,
        markSort: null
    });

    const [page,setPage] = useState(0);

    const [collection, setCollection] = useState({});
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

    const [showModal, setShowModal] = useState(false);
    

    async function retrieveCollection(){
        try{
        const response = await request("POST", "/api/collections/"+user.id+ "/" +collectionId + "/"+ page, filters)
        if(response.status === 200){
            setCollection(response.data[0]);
            setGames(response.data[0].collectionGames);
            setTotalPages(response.data[1]);
        }else{
            alert('Something went wrong: '+response.data);
        }
        }catch(error){
            console.log(error);
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

    async function removeGame(gameId){
        try{
            const response = await request("POST", "/api/collections/removeGame/"+collection.id+"/"+gameId, null);
            if(response.status === 200){
                retrieveCollection();
                alert(response.data);
            }
            
        }
        catch(error){
            console.log(error);
        }
    }

    function onClickRow(gameId){
        navigate('/personalVideogameDetails/'+gameId);
    }

    async function handleDeleteCollection(){
        try{
            const response = await request("DELETE", "/api/collections/delete/"+collection.id+"/"+user.id, null);
            alert(response.data);
            if(response.status === 200){
                navigate('/myCollections');
            }
        }catch(error){
            console.log(error);
        }
    }

    useEffect(() => {
        retrieveCollection();
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
        <>
        <div style={{display: 'flex', flexDirection: 'row', height:'15%', width:'100%'}}>
            <h2 style={{ marginRight: '10%' }}>{collection.name}</h2>
            <t style={{ marginRight: '10%' }}>{collection.creationDateString}</t>
            <t style={{ marginRight: '10%' }}>{collection.lastUpdateString}</t>
            <p style={{ marginRight: '10%' }}>{collection.description}</p>
            <button className="btn btn-primary btn-block mb-4 ml-auto mr-auto"
                        onClick={()=>{setShowModal(true)}}
                        style={{ color: 'white', backgroundColor: '#AE3C7A', borderColor: 'black', marginLeft:'15px', marginRight:'15px'}}>
                    <b>Delete</b>
            </button>
        </div>
        <div style={{display: 'flex', flexDirection: 'row', height:'85%', width:'100%'}}>
            <div className="filters d-flex justify-content-center" style={{width: '15%', height: '100%', display:'felx', flexDirection:'column'}}>
                <form style={{padding: '10px'}}>
                    <h3>Filters</h3>
                    <div className="form-group">
                        <label htmlFor="searchterms"><h6>Search</h6></label>
                        <input type="text" className="form-control" id="name" onChange={(e) => setProvisionalFilters({...provisionalFilters, searchTerms: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <h6>Mark</h6>
                        <label htmlFor="minReleaseDate"> Lower limit</label>
                        <input type="number" className="form-control" value={provisionalFilters.minMark} onChange={(e) => setProvisionalFilters({...provisionalFilters, minMark: e.target.value})}/>
                        <label htmlFor="maxReleaseDate"> Upper limit</label>
                        <input type="number" className="form-control" value={provisionalFilters.maxMark} onChange={(e) => setProvisionalFilters({...provisionalFilters, maxMark: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <h6>Time played</h6>
                        <label htmlFor="minReleaseDate"> Lower limit</label>
                        <input type="number" className="form-control" value={provisionalFilters.minTimePlayed} onChange={(e) => setProvisionalFilters({...provisionalFilters, minTimePlayed: e.target.value})}/>
                        <label htmlFor="maxReleaseDate"> Upper limit</label>
                        <input type="number" className="form-control" value={provisionalFilters.maxTimePlayed} onChange={(e) => setProvisionalFilters({...provisionalFilters, maxTimePlayed: e.target.value})}/>
                    </div>
                    <div className="form-group">
                        <h6>Completed</h6>
                        <select className="form-control" id="completed" onChange={(e) => {
                                    const completedValue = e.target.value === "" ? null : e.target.value === "true";
                                    setProvisionalFilters({...provisionalFilters, completed: completedValue});
                                }}>
                            <option value={""}>---</option>
                            <option value={true}>Yes</option>
                            <option value={false}>No</option>
                        </select>
                    </div>

                    <p></p>
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
                    <button className="btn btn-danger" onClick={() => { setProvisionalFilters({searchTerms: "",publisher: "", minReleaseDate: null, maxReleaseDate: null, genre: "",
                        platform: "", minMark: null, maxMark: null, minTimePlayed: null, maxTimePlayed: null, completed: null,timePlayedSort: null, makrSort: null})}}>Delete filters</button>
                </form>
                <p></p>
                {totalPages>1 && <nav aria-label="Page navigation example" style={{padding: '10px'}}>
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
                    </nav>}
            </div>
            <div className="games d-flex justify-content-center" style={{width: '85%', height: '100%'}}>
                {games.length === 0 ? <h3 style={{marginTop: '5%'}}>No games found, <Link to="/myGames">add some from your list!</Link>
                </h3>:
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Platform</th>
                            <th>Completed</th>
                            <th>Time played
                                {filters.timePlayedSort===null? 
                                    <>
                                    <span className="sort-arrow" onClick={() => setFilters({...filters, timePlayedSort:true, markSort: null})}>
                                        ▲
                                    </span>
                                    <span className="sort-arrow" onClick={() => setFilters({...filters, timePlayedSort:false, markSort: null})}>
                                        ▼
                                    </span>
                                    </>
                                :
                                    <span className="sort-arrow" onClick={() => setFilters({...filters, timePlayedSort: !filters.timePlayedSort, markSort: null})}>
                                        {!filters.timePlayedSort ? '▲' : '▼'}
                                    </span>
                                }
                            </th>
                            <th>Mark
                            {filters.markSort===null? 
                                    <>
                                    <span className="sort-arrow" onClick={() => setFilters({...filters, markSort:true, timePlayedSort: null})}>
                                        ▲
                                    </span>
                                    <span className="sort-arrow" onClick={() => setFilters({...filters, markSort:false, timePlayedSort: null})}>
                                        ▼
                                    </span>
                                    </>
                                :
                                    <span className="sort-arrow" onClick={() => setFilters({...filters, markSort: !filters.markSort, timePlayedSort: null})}>
                                        {!filters.markSort ? '▲' : '▼'}
                                    </span>
                                }

                            </th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {games.map((game) => {
                            return (
                                <tr key={game.id} onClick={()=>onClickRow(game.id)}>
                                <td className="column-name">{game.videogame.name}</td>
                                        <td className="column-platform">{game.platform}</td>
                                        <td className="column-completed">{game.completionTime === null ? <t>No</t> : <t>Yes</t>}</td>
                                        <td className="column-time-played">{game.timePlayed}</td>
                                        <td className="column-mark">{game.mark}</td>
                                        <td>
                                            <Icon icon="typcn:minus" onClick={(e)=>{e.stopPropagation();removeGame(game.id)}}/>
                                        </td>
                                    </tr>
                            );
                        })}
                    </tbody>
                </table>}
            </div>
        </div>
        {showModal && <ConfirmDelete setShowModal={setShowModal} handleDelete={handleDeleteCollection} text={"Are you sure you want to delete this collection?"} />}
        </>
    );
}