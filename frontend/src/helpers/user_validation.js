import {isEmail} from 'validator';
import { request } from './axios_helper';

export async function userValidation(user){
    let usernames = [];
    let emails = [];
    try{
        await request("GET", "/api/users/usernames").then((response) => usernames = response.data);
        await request("GET", "/api/users/emails").then((response) => emails = response.data);
    }catch(e){
        console.log(e);
    }

    const errors={
        usernameError: "",
        emailError: "",
        passwordError: ""
    };
    if(user.username === ""){
        errors.usernameError = "Username cannot be empty";
    }else if(user.username.length < 4){
        errors.usernameError = "Username must be at least 4 characters";
    }else if(user.username.length > 20){
        errors.usernameError = "Username must contain less than 20 characters";
    } else if(!/^[a-zA-Z0-9_]*$/.test(user.username)){
        errors.usernameError = "Username must contain only letters, numbers and underscores";
    }else if(user.username.includes(" ")){
        errors.usernameError = "Username cannot contain spaces";
    }else if(usernames.includes(user.username)){
        errors.usernameError = "Username already exists";
    }

    if(user.email === ""){
        errors.emailError = "Email cannot be empty";
    }else if(!isEmail(user.email)){
        errors.emailError = "Invalid email";
    }else if(emails.includes(user.email)){
        errors.emailError = "Email already exists";
    }


    if(user.password === ""){
        errors.passwordError = "Password cannot be empty";
    }else if(user.password.length < 8){
        errors.passwordError = "Password must be at least 8 characters";
    }else if(user.password.length > 20){
        errors.passwordError = "Password must contain less than 20 characters";
    }else if(!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{};:,<.>]).{8,20}/.test(user.password)){
        errors.passwordError = "Password must contain at least one lowercase letter, one uppercase letter, one number, and one special character";
    }

    return errors;
}