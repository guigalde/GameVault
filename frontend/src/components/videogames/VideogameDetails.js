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
                    image: image
                });
            } else {
                alert('Error getting videogame info');
            }
        } catch (error) {
            alert("Failed to load videogame:", error);
        }
    }

    console.log(videogame.image)
    return (
        <div>
            <h1>{videogame.name}</h1>
            <img src={videogame.image} alt={videogame.name} />
            <p>{videogame.description}</p>
            <p>Rating: {videogame.rating}</p>
            <p>Genres: {videogame.genres}</p>
            <p>Released: {videogame.released}</p>
            <p>Platforms: {videogame.platforms}</p>
        </div>
    );
}
