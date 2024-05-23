export default function ConfirmDelete({setShowModal, handleDelete, name, place}) {
    return (
            <div className='popup'>
                <div className='popup-inner'>
                    <h2 className="text-center d-flex justify-content-center">Deleting {name} from {place} is permanent. Confirm deletion?</h2>
                    <form>
                        <button type="button" className="btn btn-danger" onClick={()=>handleDelete()}>Delete</button>
                        <button type="submit" className="btn btn-primary" onClick={()=>setShowModal(false)}>Cancel</button>
                    </form>
                </div>
            </div>
        );    
}