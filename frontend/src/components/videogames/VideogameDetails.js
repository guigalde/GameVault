import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { request } from "../../helpers/axios_helper";
export default function VideogameDetails() {

    const { gameId } = useParams();
    const [videogame, setVideogame] = useState({});

    useEffect(() => {
        getVideogameInfo(gameId);
    }, [gameId]);

    async function getVideogameInfo(gameId) {
        try {
            const response = await request('GET', `/api/videogame/${gameId}`);
            if (response.status === 200) {
                const data = response.data;
                const image = data.image? 'https:' + data.image.replace("t_thumb", "t_original") : "https://www.publicdomainpictures.net/pictures/280000/velka/not-found-image-15383864787lu.jpg";
                setVideogame({
                   ...data,
                    image: image,
                });
            } else {
                alert('Error getting videogame info');
            }
        } catch (error) {
            alert("Failed to load videogame:", error);
        }
    }

    return (
        <div className="container-fluid" style={{height:'100%'}}>
            <div className="row">
                <div className="col-md-12 mb-5">
                    <h1 className="text-left" style={{paddingLeft:'80px', paddingTop:'30px'}}>{videogame.name}</h1>
                </div>
            </div>
            <div className="row" style={{paddingLeft:'80px'}}>
                <div className="col-md-8">
                    <div className="row h-80">
                        <div className="col-md-6 mb-3 d-flex flex-column justify-content-top py-50 px-50">
                            {videogame.publisher?
                            <p style={{fontSize: '1.5rem'}}>Publisher: {videogame.publisher}</p>
                            : 
                            <p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No publisher found</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            {videogame.developer?
                            <p style={{fontSize: '1.5rem'}}>Developer: {videogame.developer}</p>
                            :
                            <p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No developer found</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            {videogame.genres?
                            <p style={{fontSize: '1.5rem'}}>Genres: {videogame.genres}</p>
                            :
                            <p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No genres found</p>}
                        </div>
                        <div className="col-md-6 mb-3 d-flex flex-column justify-content-top py-50 px-50">
                            {videogame.releaseDate?
                            <p style={{fontSize: '1.5rem', color: '#333'}}>Release date: {videogame.releaseDate}</p>
                            :<p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No release date found</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            {videogame.platforms?
                            <p style={{fontSize: '1.5rem', color: '#333'}}>Platforms: {videogame.platforms}</p>
                            :<p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No platforms found</p>}
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            <p></p>
                            
                        </div>
                    </div>
                    <div className="row h-20" style={{paddingTop:'80px'}}>
                        {videogame.description?
                        <div className="col-12 w-50">
                            <h2>Description</h2>
                            <p>{videogame.description}</p>
                        </div>
                        :<p style={{fontSize: '1.5rem', fontWeight:'lighter'}}>No description found</p>}
            </div>
                </div>
                <div className="col-md-4">
                    <img src={videogame.image} alt={videogame.name} className="img-fluid" style={{maxWidth: '90%', height: 'auto'}} />
                </div>
            </div>
        </div>
    );
}
