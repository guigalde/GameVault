import { useEffect, useState } from "react";
import { request, setAuthHeader, getUserInfo } from '../../helpers/axios_helper';
import classNames from 'classnames';
import {useNavigate, Link} from 'react-router-dom';
import { userValidation} from '../../helpers/user_validation';
import FormError from '../FormError';


export default function EditForm(){
    const [user, setUser] = useState({
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    });
    const navigate = useNavigate();
    const [editUser, setEditUser] = useState({
        username: "",
        email: "",
        password: "",
    });
    const [errors, setErrors] = useState({})

    function handleChange(e) {
        setEditUser({ ...editUser, [e.target.name]: e.target.value });
    }

    async function onEdit(username, email, password) {
        const validationErrors = await userValidation(editUser);
        setErrors(validationErrors);
        if(errors.usernameError === "" && errors.emailError === "" && errors.passwordError === "" ){
            request(
                "PUT",
                "/api/users/" + user.id,
                {
                    username: username,
                    email: email,
                    password: password,
                    role: user.role
                }, navigate).then(
                (response) => {
                    setAuthHeader(response.data.token);
                    navigate('/');
                }).catch(
                (error) => {
                    setAuthHeader(null);
                    navigate('/error');
                }
                
            );
        }
    }
    async function onSubmitEdit(e) {
        e.preventDefault();
        onEdit( editUser.username, editUser.email, editUser.password);
    };

    useEffect(() => {
        if(!(user.username === "" || user.email === "")){
            setEditUser({
                username: user.username,
                email: user.email,
                password: ""
            });
        }
    }, [user]);

    return (
        <div className={classNames("tab-pane", "fade", "show active")} id="pills-register">
            <h1 className="text-center">Register</h1>
            <div className="d-flex justify-content-center" style={{ height: '100vh' }}>
                <form onSubmit={onSubmitEdit} className="w-25">
                <div className="form-outline mb-4">
                    <label className="form-label" htmlFor="username"><b>Username</b></label>
                    <FormError error={errors.usernameError}/>
                    <input type="text" id="registerUsername" name="username" className="form-control col-md-6" value = {editUser.username} onChange={handleChange}/>

                </div>

                <div className="form-outline mb-4">
                    <label className="form-label" htmlFor="email"><b>Email</b></label>
                    <FormError error={errors.emailError}/>
                    <input type="text" id="email" name="email" className="form-control col-md-6" value = {editUser.email} onChange={handleChange}/>
                </div>

                <div className="form-outline mb-4">
                    <label className="form-label" htmlFor="registerPassword"><b>Password</b></label>
                    <FormError error={errors.passwordError}/>
                    <input type="password" id="registerPassword" name="password" className="form-control" onChange={handleChange}/>
                </div>
                <div className="d-flex flex-column align-items-center">
                    <button type="submit" className="btn btn-primary btn-block mb-4" style={{ color: 'black', backgroundColor: '#3CACAE', borderColor: 'black' }}><b>Save changes</b></button>
                </div>
                <div className="d-flex flex-column align-items-center">
                    <Link to="/">
                        <button className="btn btn-primary btn-block mb-4" style={{ color: 'white', backgroundColor: '#AE3C7A', borderColor: 'black' }}><b>Cancel edit</b></button>
                    </Link>
                </div>
                </form>
            </div>
            
        </div>
    );
}