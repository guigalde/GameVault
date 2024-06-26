import {getUserInfo, request} from '../../helpers/axios_helper.js'
import { useState, useEffect } from 'react';
import CreateCollectionForm from './CollectionForm.js';
import { useNavigate } from 'react-router-dom';
import { Icon } from '@iconify-icon/react';
import ConfirmDelete from '../ConfirmDeleteModal.js';

export default function ListMyCollections() {
    const navigate = useNavigate();

    const user= {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };

    const [collections, setCollections] = useState([]);
    const [steamCollection, setSteamCollection] = useState({});
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const currentPage = page + 1;
    const previousPage = page;
    const nextPage = page + 2;
    const [searchTerm, setSearchTerm] = useState('');
    const [definitiveSearchTerm, setDefinitiveSearchTerm] = useState('');
    const [orderBy, setOrderBy] = useState('');
    const [showCreationForm, setShowCreationForm] = useState(false);
    const [collectionToDelete, setCollectionToDelete] = useState(null);

    const [showModal, setShowModal] = useState(false);


    async function getCollections() {
        try {
            const formData = new FormData();
            formData.append('searchTerm', definitiveSearchTerm);
            formData.append('orderBy', orderBy);
            const response = await request('POST', '/api/collections/'+user.id + '/'+ page, formData, navigate)
            if (response.status === 200) {
                const sortedCollections = response.data[0].sort((a, b) => {
                    if (a.name.toLowerCase() === "steam") return -1;
                    if (b.name.toLowerCase() === "steam") return 1;
                    return 0;
                  });
                setCollections(sortedCollections);
                setTotalPages(response.data[1]);
            }else{
                alert('Error getting collections');
            }
        }catch(error){
            navigate("/error")
        }
    }

    async function handleDeleteCollection(){
        try{
            const response = await request("DELETE", "/api/collections/delete/"+collectionToDelete+"/"+user.id, null, navigate);
            alert(response.data);
            getCollections();
        }catch(error){
            navigate("/error");
        }
        setShowModal(false);
        setCollectionToDelete(null);
    }

    useEffect(() => {
        getCollections();
    }, [page, definitiveSearchTerm, orderBy]);

    useEffect(()=>{
        setPage(0);
    }, [definitiveSearchTerm, orderBy]);

    return (
        <div style={{display: 'flex', flexDirection: 'row', height:'100%', width:'100%'}}>
        <div className="filters d-flex justify-content-center" style={{width: '15%', height: '100%', display:'flex', flexDirection:'column'}}>
            <div className= 'd-flex' style={{width:'100%', flexDirection:'row'}}>
            <input type="text" placeholder="Search"className="form-control" value={searchTerm} style={{height: '100%', width: '85%', marginBottom:'5%', marginRight:'5%'}}onChange={(e)=>setSearchTerm(e.target.value)}></input>
            <button className="btn btn-primary mb-4"style={{ color: 'black', backgroundColor: '#3CACAE', borderColor: 'black'}}
                onClick={()=>setDefinitiveSearchTerm(searchTerm)}>Search</button>
            </div>
            <button className="btn btn-primary mb-4"style={{ color: 'black', backgroundColor: '#EEB5EB', borderColor: 'black'}} 
                onClick={()=>setShowCreationForm(true)}><b>Create Collection</b></button>                 
            {totalPages > 1 ?
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
                    </nav>:null}
        </div>
        {collections.length === 0 ? 
            <div style={{display: 'flex', flexDirection: 'column', height:'100%', width:'85%', alignItems:'center'}}>
            <h2 style={{padding: '10px'}}>No collections found</h2>
            </div>
         : 
        <div style={{display: 'flex', flexDirection: 'column', height:'100%', width:'85%'}}>
            <div className="d-flex justify-content-center" style={{width: '90%', height: '100%', padding: '3%'}}>
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
                                    collection.name.toLowerCase() === "steam" ? 
                                    <tr key={collection.id} onClick={()=>navigate("/collection/"+collection.id)} style={{borderColor:"black"}}>
                                        <td style={{width: '15%'}}>{collection.name}</td>
                                            <td style={{width: '15%'}}>{collection.creationDateString}</td>
                                            <td style={{width: '15%'}}>{collection.lastUpdateString}</td>
                                            <td style={{width: '45%'}}>{collection.description.lenght>100?collection.description.substring(0,99)+"...":collection.description}</td>
                                            <td style={{width: '10%'}}>
                                                <Icon icon="icomoon-free:bin" style={{cursor: 'pointer'}} onClick={(e)=>{e.stopPropagation() ;setCollectionToDelete(collection.id); setShowModal(true) }} />
                                            </td>
                                        </tr>
                                        :
                                        <tr key={collection.id} onClick={()=>navigate("/collection/"+collection.id)}>
                                        <td style={{width: '15%'}}>{collection.name}</td>
                                            <td style={{width: '15%'}}>{collection.creationDateString}</td>
                                            <td style={{width: '15%'}}>{collection.lastUpdateString}</td>
                                            <td style={{width: '45%'}}>{collection.description.lenght>100?collection.description.substring(0,99)+"...":collection.description}</td>
                                            <td style={{width: '10%'}}>
                                                <Icon icon="icomoon-free:bin" style={{cursor: 'pointer'}} onClick={(e)=>{e.stopPropagation() ;setCollectionToDelete(collection.id); setShowModal(true) }} />
                                            </td>
                                        </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
        </div>
        }
        {showCreationForm && <CreateCollectionForm setShowForm={setShowCreationForm} dataRetrieve={getCollections}/>}
        {showModal && <ConfirmDelete setShowModal={setShowModal} handleDelete={handleDeleteCollection} text={"Are you sure you want to delete this collection?"} />}

        </div>
    );

}