

const Notice = () => {
  return (
    
    <div className="flex justify-center mt-5">
      <div className="w-[312px] h-[32px] border-none bg-Stickey_Gray rounded flex flex-row items-center gap-1">
        <img src="/src/assets/image/YellowBell.png" className="w-5 h-5 ml-2"/>
        <p className="text-red-600 text-[12px]">[TIP]</p>
        <p className="text-[12px]">IOS 서비스 미지원임.</p>
      </div>
    </div>
  )
}

export default Notice;