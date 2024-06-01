import { getUserInfo, request } from '../../helpers/axios_helper';
import {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import {Icon} from '@iconify-icon/react';


export default function SyncWithSteam() {

    const user = {
        id: getUserInfo().id,
        username: getUserInfo().sub,
        email: getUserInfo().email,
        role: getUserInfo().role
    };

    const navigate = useNavigate();

    const [steamId, setSteamId] = useState("");

    const [showSteamTip, setShowSteamTip] = useState(false);

    function  handleSyncWithSteam() {
        alert("This procedure may take a few minutes. Please wait.");
        
        request(
          'POST',
          '/api/personalVideogame/syncWithSteam/'+user.id+"/"+steamId, null, navigate
          ).then((response) => {
            alert(response.data);
          }).catch(
            (error) => {navigate("/error")}
        );
      }

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            height: '100%',
            width: '100%',
        }}>
            <h1>Sync with Steam</h1>
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Steam_icon_logo.svg/2048px-Steam_icon_logo.svg.png" alt="Steam logo" width="200" height="200"></img>
            <div style={{
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',}}>
                <p>Enter your Steam ID:
                </p>
                <Icon icon="ph:question-bold"  style={{fontSize: '30px', marginBottom:'15px'}} onClick={()=>setShowSteamTip(!showSteamTip)}
                    title='To get your steam ID, click on your profile on the top right corner, select "Account details" and then check under the account name for a Steam ID '/>
            </div>
            {showSteamTip && <p>
                To get your steam ID, click on your profile on the top right corner, select "Account details" and then check under the account name for a Steam ID
            </p>}
            <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',}}>
                <input type="text" onChange={(e) => setSteamId(e.target.value)}></input>
                <button className="btn btn-primary btn-block mb-4 ml-auto mr-auto"
                            onClick={()=>{handleSyncWithSteam()}}
                            style={{ color: 'white', backgroundColor: '#3CACAE', borderColor: 'black', margin:'3%'}}>
                        <b>Sync</b>
                    </button>
            </div>
            <p></p>
            <p></p>
            <p></p>
            <p></p>
            <p></p>
            <p></p>
            <p></p>
            <p></p>
            <p>
                FYI: This procedure checks on an external Data Base and looks for similar names to the ones of the games you have in your Steam account.
            </p>
            <p>
                This may lead to some games not being found or to the system to add wrong games, so please <b>check the results and add the missing games manually.</b>
            </p>

        </div>
    );
}