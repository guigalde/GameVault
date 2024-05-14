import {getUserInfo, request} from '../helpers/axios_helper'
import { useState, useEffect } from 'react';
import { IcomoonFreeBin } from './imported_icons/bin.js';
import CreateCollectionForm from './CreateCollectionForm.js';

export default function ListMyCollections() {

    const user= {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };

    const [collections, setCollections] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [searchTerm, setSearchTerm] = useState('');
    const [definitiveSearchTerm, setDefinitiveSearchTerm] = useState('');
    const [orderBy, setOrderBy] = useState('');
    const [showCreationForm, setShowCreationForm] = useState(false);

    async function getCollections() {
        const formData = new FormData();
        formData.append('searchTerm', definitiveSearchTerm);
        formData.append('orderBy', orderBy);
        const response = await request('POST', '/api/collections/'+user.id + '/'+ page, formData)
        if (response.status === 200) {
            setCollections(response.data[0]);
            setTotalPages(response.data[1]);
        }else{
            alert('Error getting collections');
        }
    }

    async function deleteCollection(collectionId){
        const response = await request('DELETE', '/api/collections/delete/'+collectionId);
        if(response.status === 200){
            alert('Collection deleted successfully');
            getCollections();
        }else{
            alert('Error deleting collection');
        }
    }

    useEffect(() => {
        getCollections();
    }, [page, definitiveSearchTerm, orderBy]);

    useEffect(()=>{
        setPage(0);
    }, [definitiveSearchTerm, orderBy]);

    return (
        <>
        <div style={{display: 'flex', flexDirection: 'row', height:'5%', width:'100%'}}>
            <input type="text" placeholder="Search" onChange={(e)=>setSearchTerm(e.target.value)}></input>
            <button onClick={()=>setDefinitiveSearchTerm(searchTerm)}>Search</button>
            <button onClick={()=>setShowCreationForm(true)}>Create Collection</button>
        </div>
        {collections.length === 0 ? 
            <h2>No collections found</h2>
         : 
        <div style={{display: 'flex', flexDirection: 'row', height:'95%', width:'100%'}}>
            <div className="d-flex justify-content-center" style={{width: '90%', height: '100%', padding: '10%'}}>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>Name
                                {orderBy !== "nameAsc" ? 
                                        
                                        <span className="sort-arrow" onClick={() => setOrderBy("nameAsc")}>
                                            ▲
                                        </span>:null}
                                    {orderBy !== "nameDesc" ?
                                        <span className="sort-arrow" onClick={() => setOrderBy("nameDesc")}>
                                            ▼
                                        </span>:null}
                                </th>
                                <th>Creation date
                                {orderBy !== "creationDateAsc" ? 
                                        
                                        <span className="sort-arrow" onClick={() => setOrderBy("creationDateAsc")}>
                                            ▲
                                        </span>:null}
                                    {orderBy !== "creationDateDesc" ?
                                        <span className="sort-arrow" onClick={() => setOrderBy("creationDateDesc")}>
                                            ▼
                                        </span>:null}
                                </th>
                                <th>Last updated
                                {orderBy !== "lastUpdateAsc" ? 
                                        
                                        <span className="sort-arrow" onClick={() => setOrderBy("lastUpdateAsc")}>
                                            ▲
                                        </span>:null}
                                    {orderBy !== "lastUpdateDesc" ?
                                        <span className="sort-arrow" onClick={() => setOrderBy("lastUpdateDesc")}>
                                            ▼
                                        </span>:null}
                                </th>
                                <th>Description</th>
                                <th>Delete collection</th>
                            </tr>
                        </thead>
                        <tbody>
                            {collections.map((collection) => {
                                return (
                                    <tr key={collection.id}>
                                        <td style={{width: '15%'}}>{collection.name}</td>
                                            <td style={{width: '15%'}}>{collection.creationDate}</td>
                                            <td style={{width: '15%'}}>{collection.lastUpdate}</td>
                                            <td style={{width: '45%'}}>{collection.description.substring(0,99)+"..."}</td>
                                            <td style={{width: '10%'}}>
                                                <IcomoonFreeBin style={{cursor: 'pointer'}} onClick={()=>deleteCollection(collection.id)} />
                                            </td>
                                        </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
        </div>
        }
        {showCreationForm ? <CreateCollectionForm setShowForm={setShowCreationForm} getCollections={getCollections}/> : null}
        </>
    );

}