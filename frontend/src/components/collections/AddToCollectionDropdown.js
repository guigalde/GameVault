import {useState} from 'react';
import {request} from '../../helpers/axios_helper';

export default function AddToCollectionDropdown({collections, personalVideogame, setShowCollectionForm}){
    
    const [searchCollection, setSearchCollection] = useState('');


    async function handleAddToCollection(e){
        e.preventDefault();
        const matchingCollection = collections.find(collection => collection.name === searchCollection);
        if(!matchingCollection){
            alert("Collection not found");
            return;
        }
        const response = await request("POST", "/api/collections/addGame/"+matchingCollection.id+"/"+personalVideogame.id, null);
        if(response.status === 200){
            alert(response.data);
            setShowCollectionForm(false);
        }else{
            alert(response.data);
        }
    }
    return (
        <div className='popup'>
             <div className='popup-inner'>
                 <h2 className="text-center d-flex justify-content-center" style={{alignItems: "center"}}>Add {personalVideogame.videogame.name} to a collection</h2>
                 <form className = "form-collections"onSubmit={handleAddToCollection}>
                     <div className="form-group">
                         <datalist id="games" className="scrollable-datalist">
                             {collections.map(collection => 
                                 <option key={collection.id} value={collection.name} />)}
                         </datalist>
                         <input type="text" list="games" className="form-control" value={searchCollection} onChange={(e)=>setSearchCollection(e.target.value)}/>
                     </div>
                     <div style={{display: "flex", justifyContent: "center",alignItems: "center"}}>
                         <button type="submit" className="btn btn-primary">Add</button>
                         <button type="button" className="btn btn-danger" onClick={()=>setShowCollectionForm(false)}>Cancel</button>
                     </div>
                 </form>
             </div>
         </div>
    )
}