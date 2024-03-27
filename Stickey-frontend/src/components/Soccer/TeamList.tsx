import Star from '../../assets/image/Star.png';
import { useGame } from '../../hooks/Home/useGame';

const TeamList = ({ catg }: { catg: string }) => {

  const { useGetTeamList } = useGame();

  const {
    data: teamListInfo,
  } = useGetTeamList({ catg });

  console.log(teamListInfo?.data);

  return (
    <div className="px-4 flex flex-row z-[2] overflow-x-auto">
      <div className="flex flex-row gap-2">
        {teamListInfo && teamListInfo?.data.map((team) => (
          <div key={team.id} className={`relative w-14 h-16 border border-none flex flex-col shadow-[2px_2px_rgba(0,0,0,0.25)] justify-center items-center gap-1 rounded-[5px] bg-[#2E2E3D] ${team.isPrefer === true ? 'prefer-style' : ''}`}>
            {team.isPrefer === true && <img src={Star} className='absolute w-2 h-2 left-1 top-1'/>}
            <div className="w-7 h-8">
              <img src={team.logo} alt={team.name} />
            </div>
            <p className="text-[6px] text-white">{team.name}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TeamList;
