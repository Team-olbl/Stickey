import { useNavigate } from 'react-router-dom';
import Bell from '../../assets/image/Bell.png';
import useNotifyReadStore from '../../stores/useNotifyReadStore';

export interface IHeaderInfo {
  left_1: React.ReactNode | null;
  left_2: JSX.Element | null;
  center: string | null;
  right: React.ReactNode | null;
}

const Header = (props: { info: IHeaderInfo }) => {
  const navigate = useNavigate();
  const { isRead } = useNotifyReadStore();

  const { left_1, left_2, center, right } = props.info;

  return (
    <div className="max-w-[500px] font-bold w-full z-[12] h-12 px-4 top-0 flex fixed flex-row justify-between items-center border-b-[0.5px] border-white bg-Stickey_BGC">
      <div className="flex flex-1 justify-start items-center">
        {left_1 && (
          <button className="text-white font-bold w-13">
            <div>{left_1}</div>
          </button>
        )}
        {left_2 && (
          <button onClick={() => navigate(-1)}>
            <div className="w-4 h-4">{left_2}</div>
          </button>
        )}
      </div>
      <div className="flex-1 flex justify-center">{center && <p className="text-white">{center}</p>}</div>
      <div className="flex flex-1 justify-end items-center relative">
        <button onClick={() => navigate('/alarm')}>
          {right && <img src={Bell} alt="Alarm" className="w-[32px] h-[32px]" />}
          {!isRead && <div className="absolute -right-1 top-0 w-2 h-2 rounded-full bg-[#E14246]"></div>} {/* 수정 */}
        </button>
      </div>
    </div>
  );
};

export default Header;
