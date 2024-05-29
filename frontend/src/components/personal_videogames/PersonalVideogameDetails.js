import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { request, getUserInfo } from "../../helpers/axios_helper";
import AddToCollectionDropdown from "../collections/AddToCollectionDropdown";
import ConfirmDelete from "../ConfirmDeleteModal";
import PersonalVideogameForm from "./PersonalVideogameForm";

export default function PersonalVideogameDetails() {

    const navigate = useNavigate();


    const { gameId } = useParams();
    const [personalVideogame, setPersonalVideogame] = useState({});
    const [videogame, setVideogame] = useState({});
    const [collections, setCollections] = useState([]);
    const [showCollectionForm, setShowCollectionForm] = useState(false);
    const [showEditForm, setShowEditForm] = useState(false);
    const [showConfirmDelete, setShowConfirmDelete] = useState(false);


    const user = {
        id: getUserInfo().id,
        username: getUserInfo().username,
        email: getUserInfo().email,
        role: getUserInfo().role,
    };

    useEffect(() => {
    getPersonalVideogameInfo(gameId);
    }, [gameId]);

    async function retrieveCollections(){
        const response = await request("GET", "/api/collections/"+user.id, null, navigate);
        if(response.status === 200){
            setCollections(response.data);
        }
    }
    

    async function getPersonalVideogameInfo() {
        try {
            const response = await request('GET', `/api/personalVideogame/${gameId}/${user.id}`, null, navigate);
            if (response.status === 200) {
                const data = response.data;
                const image = data.videogame.image? 'https:' + data.videogame.image.replace("t_thumb", "t_original") : "https://www.publicdomainpictures.net/pictures/280000/velka/not-found-image-15383864787lu.jpg";
                setPersonalVideogame(data);
                setVideogame({name: data.videogame.name, image: image, id: data.videogame.id});
            } else {
                alert(response.data);
            }
        } catch (error) {
            alert("Failed to load personal videogame:", error);
        }
    }

    async function handleDeletePersonalVideogame(){
        const response = await request("DELETE", "/api/deletePersonalVideogame/"+gameId+"/"+user.id, null, navigate);
        if(response.status === 200){
            alert(response.data);
            navigate('/myGames');
        } else {
            alert(response.data);
        }
    }

    return (
        <div className="container-fluid" style={{height:'100%'}}>
            <div className="row">
            <div className="col-md-12 mb-5 d-flex align-items-center justify-content-left p-3">
                <h1 className="text-left" style={{paddingLeft:'30px', paddingRight:'250px'}}>{videogame.name}</h1>
                <button className="btn btn-primary btn-block mb-4 ml-auto mr-auto"
                        onClick={()=>{navigate('/videogameDetails/'+videogame.id)}}
                        style={{ color: 'white', backgroundColor: '#3CACAE', borderColor: 'black', marginLeft:'15px', marginRight:'15px'}}>
                    <b>Videogame info</b>
                </button>
                <button className="btn btn-primary btn-block mb-4 ml-auto mr-auto"
                        onClick={()=>{setShowCollectionForm(true);retrieveCollections();}}
                        style={{ color: 'black', backgroundColor: '#DC80D5', borderColor: 'black', marginRight:'15px'}}>
                    <b>Add to collection</b>
                </button>
                <button className="btn btn-primary btn-block mb-4 ml-auto mr-auto"
                        onClick={()=>{setShowEditForm(true)}}
                        style={{ color: 'black', backgroundColor: '#DC80D5', borderColor: 'black', marginRight:'15px'}}>
                    <b>Edit</b>
                </button>
                <button className="btn btn-primary btn-block mb-4 ml-auto mr-auto"
                        onClick={()=>{setShowConfirmDelete(true)}}
                        style={{ color: 'white', backgroundColor: '#AE3C7A', borderColor: 'black', marginLeft:'15px', marginRight:'15px'}}>
                    <b>Delete</b>
                </button>
            </div>
            </div>
            <div className="row" style={{paddingLeft:'80px'}}>
                <div className="col-md-8">
                    <div className="row h-80">
                        <div className="col-md-6 mb-3 d-flex flex-column justify-content-top py-50 px-50">
                            {personalVideogame.timePlayed?
                            <p style={{fontSize: '1.5rem'}}>Time played: {personalVideogame.timePlayed} h</p>
                            : 
                            <p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No time played specified</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            {personalVideogame.acquiredOn?
                            <p style={{fontSize: '1.5rem'}}>Acquired on: {personalVideogame.acquiredOn}</p>
                            :
                            <p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No acquisition date specified</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            {personalVideogame.platform?
                            <p style={{fontSize: '1.5rem'}}>Platform: {personalVideogame.platform}</p>
                            :
                            <p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No platform specified</p>}
                        </div>
                        <div className="col-md-6 mb-3 d-flex flex-column justify-content-top py-50 px-50">
                            {personalVideogame.mark?
                            <p style={{fontSize: '1.5rem', color: '#333'}}>Mark: {personalVideogame.mark}</p>
                            :<p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No mark specified</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            {personalVideogame.completedOn?
                            <p style={{fontSize: '1.5rem', color: '#333'}}>Completed on: {personalVideogame.completedOn}</p>
                            :<p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No completion date specified</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            {personalVideogame.completionTime?
                            <p style={{fontSize: '1.5rem', color: '#333'}}>Completed on: {personalVideogame.completionTime}</p>
                            :<p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No completion time specified</p>}
                            
                            
                        </div>
                    </div>
                    <div className="row h-20" style={{paddingTop:'80px'}}>
                        {personalVideogame.notes?
                        <div className="col-12 w-50">
                            <h2>Notes</h2>
                            <p>{personalVideogame.notes}</p>
                        </div>
                        :<p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No description found</p>}
            </div>
                </div>
                <div className="col-md-4">
                    <img src={videogame.image} alt={videogame.name} className="img-fluid" style={{maxWidth: '90%', height: 'auto'}} />
                </div>
            </div>
            {showCollectionForm && <AddToCollectionDropdown collections={collections} personalVideogame={personalVideogame} setShowCollectionForm={setShowCollectionForm} />}
            {showConfirmDelete && <ConfirmDelete setShowModal={setShowConfirmDelete} handleDelete={handleDeletePersonalVideogame} name={videogame.name} place="my games" />}
            {showEditForm && <PersonalVideogameForm gameName={personalVideogame.videogame.name} personalVideogameToEdit={personalVideogame} setShowForm={setShowEditForm} 
                dataRetrieve={getPersonalVideogameInfo}/>}
        </div>
    );
}