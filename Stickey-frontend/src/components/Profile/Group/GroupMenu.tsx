import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import Next from '../../../assets/image/Next.png';
import LogoutModal from '../../Login/LogoutModal';
import { toast } from 'react-toastify';

const GroupMenu = ({ status }: { status: boolean }) => {
  const [isLogoutModalOpen, setIsLogoutModalOpen] = useState<boolean>(false);
  const navigate = useNavigate();

  return (
    <>
      <div className="max-w-[500px] w-full mt-4 border-t-[0.5px] pb-20">
        <div className="px-4">
          <div
            className="flex flex-row items-center justify-between h-[40px] text-white px-4 cursor-pointer"
            onClick={() => {
              if (status) {
                navigate('/profile/playerlist');
              } else toast.warn('승인을 받아야합니다.');
            }}
          >
            <p>선수 조회 / 수정</p>
            <img src={Next} className="h-[20px]" />
          </div>
          <div
            className="flex flex-row items-center justify-between h-[40px] text-white px-4 cursor-pointer"
            onClick={() => {
              if (status) {
                navigate('/profile/sponlist');
              } else toast.warn('승인을 받아야합니다.');
            }}
          >
            <p>후원글 내역</p>
            <img src={Next} className="h-[20px]" />
          </div>
          <div
            className="flex flex-row items-center justify-between h-[40px] text-white px-4 cursor-pointer"
            onClick={() => navigate('/profile/group/edit')}
          >
            <p>단체 정보 수정</p>
            <img src={Next} className="h-[20px]" />
          </div>
          <div
            className="flex flex-row items-center justify-between h-[40px] text-white px-4 cursor-pointer"
            onClick={() => setIsLogoutModalOpen(true)}
          >
            <p>로그아웃</p>
            <img src={Next} className="h-[20px]" />
          </div>
        </div>
      </div>
      {isLogoutModalOpen && <LogoutModal onClose={() => setIsLogoutModalOpen(false)} />}
    </>
  );
};

export default GroupMenu;
