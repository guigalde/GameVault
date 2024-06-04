import * as React from 'react';
import { getUserInfo, request } from '../helpers/axios_helper';
import {useNavigate} from 'react-router-dom';

export function WelcomeContent(){
  const user = {
      id: getUserInfo().id,
      username: getUserInfo().sub,
      email: getUserInfo().email,
      role: getUserInfo().role
  };
  const navigate = useNavigate();

  const [news , setNews] = React.useState([]);

  async function  getNews(){
    try{
      const response = await request('GET', '/api/user/getNews/'+user.id, null, navigate);
      if(response){
        if(response.status === 200 && response.data.length !== 0){
          setNews(response.data);
        }
      }
    }catch(error){
      navigate('/error');
      console.error(error);
    }
  }

  React.useEffect(() => {
    if(user.id){
      getNews();
    }
  },[]);
    
  return (
    <div className="row justify-content-md-center">
        <div className="jumbotron jumbotron-fluid">
            <div className="container">
                {user.username? <h1 className="display-4">Welcome back, {user.username}!</h1> : <>
                    <h1 className="display-4">Welcome to GameVault, an app made for managing videogame collections!</h1>
                    <p className="lead">Login to get the full experience.</p>
                </>}
                {news && news.length > 0 && <>
                    <h2>News</h2>
                    {news.map((item, index) => (
                        <div key={index} className="card">
                            <div className="card-body d-flex align-items-center justify-content-between">
                                <h5 className="card-title" style={{width:'80%'}}>{item.gameName}: {item.newsTitle}</h5>
                                <a href={item.newsUrl} className="btn btn-primary" style={{width:'10%',backgroundColor: '#EEB5EB', color:'black', borderColor:'black'}}><b>Read more</b></a>
                            </div>
                        </div>
                    ))}
                </>}
            </div>
        </div>
    </div>
);
}