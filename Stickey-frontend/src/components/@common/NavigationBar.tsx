import { useLocation, useNavigate } from 'react-router-dom';
import FilledHouse from '../../assets/image/NavigationBar/FilledHouse.png';
import FilledTree from '../../assets/image/NavigationBar/FilledTree.png';
import FilledTicket from '../../assets/image/NavigationBar/FilledTicket.png';
import FilledUser from '../../assets/image/NavigationBar/FilledUser.png';
import House from '../../assets/image/NavigationBar/House.png';
import Tree from '../../assets/image/NavigationBar/Tree.png';
import Ticket from '../../assets/image/NavigationBar/Ticket.png';
import User from '../../assets/image/NavigationBar/User.png';
import userStore from '../../stores/userStore';
import { connect } from '../../service/web3/api';

const NavigationBar = () => {
  const isLogin = userStore(state => state.isLogin);
  const navigate = useNavigate();
  const { pathname } = useLocation();

  const handleTabClick = (tab: string) => {
    switch (tab) {
      case 'home':
        navigate('/home');
        break;
      case 'sponsor':
        navigate('/sponsor');
        break;
      case 'mytickets':
        if (isLogin) {
          connect().then(ret => {
            if (ret) navigate('/mytickets');
          });
        } else {
          navigate('/login');
        }
        break;
      case 'profile':
        navigate('/profile');
        break;
      default:
        navigate('/home');
    }
  };

  return (
    <div className="fixed pb-1 bottom-0 max-w-[500px] z-[2] w-full h-[62px] border-t-[0.5px] bg-Stickey_BGC border-white flex flex-row  justify-center gap-[56px] items-center ">
      <div className="flex flex-col items-center gap-0" onClick={() => handleTabClick('home')}>
        <img src={pathname === '/home' ? FilledHouse : House} className="w-[32px] h-[32px]" />
        <p className="text-white text-center text-[10px]">홈</p>
      </div>
      <div className="flex flex-col items-center" onClick={() => handleTabClick('sponsor')}>
        <img src={pathname === '/sponsor' ? FilledTree : Tree} className="w-[32px] h-[32px]" />
        <p className="text-white text-center text-[10px]">후원</p>
      </div>
      <div className="flex flex-col items-center" onClick={() => handleTabClick('mytickets')}>
        <img src={pathname === '/mytickets' ? FilledTicket : Ticket} className="w-[32px] h-[32px]" />
        <p className="text-white text-center text-[10px]">티켓</p>
      </div>
      <div className="flex flex-col items-center" onClick={() => handleTabClick('profile')}>
        <img src={pathname === '/profile' ? FilledUser : User} className="w-[32px] h-[32px]" />
        <p className="text-white text-center text-[10px]">마이</p>
      </div>
    </div>
  );
};

export default NavigationBar;
