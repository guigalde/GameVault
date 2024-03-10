import { createContext } from "react";

export const UserContext = createContext({user: {isLogged: false, id: "", username: "", email: "", role: ""}, setUser: () => {}});
