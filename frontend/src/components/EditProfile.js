import { useState } from "react";
import { request, setAuthHeader } from '../helpers/axios_helper';
import classNames from 'classnames';
import {useNavigate, Link} from 'react-router-dom';
import { UserContext } from '../helpers/user_context';
import { useContext } from 'react';

export default function EditForm(){
    const {user, setUser} = useContext(UserContext);
    const navigate = useNavigate();
    const [editUser, setEditUser] = useState({
        username: "",
        email: "",
        password: "",
    });

    function handleChange(e) {
        setEditUser({ ...editUser, [e.target.name]: e.target.value });
    }

    function onEdit(event, username, email, password) {
        event.preventDefault();
        request(
            "PUT",
            "/api/users/" + user.id,
            {
                username: username,
                email: email,
                password: password,
                role: user.role
            }).then(
            (response) => {
                setAuthHeader(response.data.token);
                setUser({
                    isLogged: true,
                    id: response.data.id,
                    username: response.data.username,
                    email: response.data.email,
                    role: response.data.role
                });
                navigate('/');
            }).catch(
            (error) => {
                setAuthHeader(null);
            }
            
        );
    }
    function onSubmitEdit(e) {
        onEdit(e, editUser.username, editUser.email, editUser.password);
    };

    return (
        <div className={classNames("tab-pane", "fade", "show active")} id="pills-register">
            <h1 className="text-center">Register</h1>
            <div className="d-flex justify-content-center" style={{ height: '100vh' }}>
                <form onSubmit={onSubmitEdit} className="w-25">
                <div className="form-outline mb-4">
                    <label className="form-label" htmlFor="username"><b>Username</b></label>
                    <input type="text" id="registerUsername" name="username" className="form-control col-md-6" onChange={handleChange}/>

                </div>

                <div className="form-outline mb-4">
                    <label className="form-label" htmlFor="email"><b>Email</b></label>
                    <input type="text" id="email" name="email" className="form-control col-md-6" onChange={handleChange}/>
                </div>

                <div className="form-outline mb-4">
                    <label className="form-label" htmlFor="registerPassword"><b>Password</b></label>
                    <input type="password" id="registerPassword" name="password" className="form-control" onChange={handleChange}/>
                </div>
                <div className="d-flex flex-column align-items-center">
                    <button type="submit" className="btn btn-primary btn-block mb-4" style={{ color: 'black', backgroundColor: '#3CACAE', borderColor: 'black' }}><b>Save changes</b></button>
                </div>
                <div className="d-flex flex-column align-items-center">
                    <Link to="/">
                        <button className="btn btn-primary btn-block mb-4" style={{ color: 'white', backgroundColor: '#AE3C', borderColor: 'black' }}><b>Cancel edit</b></button>
                    </Link>
                </div>
                </form>
            </div>
            
        </div>
    );
}