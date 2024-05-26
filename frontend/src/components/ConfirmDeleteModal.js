export default function ConfirmDelete({setShowModal, handleDelete, text}) {

    async function onDeleteClick(){
        await handleDelete();
    }
    return (
            <div className='popup'>
                <div className='popup-inner'>
                    <h2 className="text-center d-flex justify-content-center">{text}</h2>
                    <form>
                        <button type="button" className="btn btn-danger" onClick={()=>onDeleteClick()}>Delete</button>
                        <button type="submit" className="btn btn-primary" onClick={()=>setShowModal(false)}>Cancel</button>
                    </form>
                </div>
            </div>
        );    
}