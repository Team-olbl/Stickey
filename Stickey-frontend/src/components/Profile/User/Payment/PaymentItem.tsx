import { useState } from "react";
import { PaymentItemData } from "../../../../pages/Profile/User/PaymentHistory";
import Down from '../../../../assets/image/FilledDown.png'
import dayjs from 'dayjs';
import { toEther } from "../../../../service/web3/api";
import { AnimatePresence, motion } from "framer-motion";

const PaymentItem = ({ data }: { data: PaymentItemData }) => {
  const [isOpenAccordion, setIsOpenAccordion] = useState<boolean>(false);

  const toggleAccordion = () => {
    setIsOpenAccordion(!isOpenAccordion);
  };
  const time = dayjs(Number(data.time) * 1000).format('YYYY/MM/DD HH:mm:ss');
 
  const bookStatus = () => {
    if (data.paymentType == 0) {
      return (
        <div className="w-[36px] h-[18px] bg-[#545454] rounded-[5px] mr-2">
          <p className="text-white text-[12px] text-center">예매</p>
        </div>
      )
    } else if (data.paymentType == 1) {
      return (
        <div className="w-[36px] h-[18px] bg-[#FF532D] rounded-[5px] mr-2">
          <p className="text-white text-[12px] text-center">환불</p>
        </div>)
    }  else if (data.paymentType == 2) {
      return (
        <div className="w-[36px] h-[18px] bg-[#ff8569] rounded-[5px] mr-2">
          <p className="text-white text-[12px] text-center">반환</p>
        </div>)
    } else {
      return (
        <div className="w-[36px] h-[18px] bg-[#349137] rounded-[5px] mr-2">
          <p className="text-white text-[12px] text-center">후원</p>
        </div>)
    }
  }

  const headContent = () => {
    if (data.paymentType == 0) {
      return (
        <>
          {data.ticketPayment.homeTeam} VS {data.ticketPayment.awayTeam} 예매
        </>
      )
    } else if (data.paymentType == 1) {
      return (
        <>
          {data.ticketPayment.homeTeam} VS {data.ticketPayment.awayTeam} 환불
        </>
      )
    } else if (data.paymentType == 2) {
      return (
        <>
          {data.ticketPayment.homeTeam} VS {data.ticketPayment.awayTeam} 수수료 환불
        </>
      )
    } else {
      return (
        <>
          {data.supportName} 후원
        </>
      )
    }
  }
  
  const bodyContent = () => {
    if (data.paymentType == 3) {
      return (<>
        <div className="flex flex-row gap-1 text-[12px] font-semibold">
          <p>후원한 단체 : {data.supportName}</p> 
        </div>
        <div className="flex flex-row gap-1 text-[12px] font-semibold">
          <p>내가 남긴 말 : {data.supportText}</p>
        </div>
        </>
      )
    } else {
      return (<>
        <div className="flex flex-row gap-1 text-[12px] font-semibold">
              <p>{data.ticketPayment.homeTeam}</p>
              <p>VS</p>
              <p>{data.ticketPayment.awayTeam}</p>
            </div>
            <div className="text-[10px] p-2">
              <div className="flex flex-row gap-5">
                <div className="w-[50px] text-[#969799]">장소</div>
                <div>{data.ticketPayment.stadium}</div>
              </div>
              <div className="flex flex-row gap-5">
                <div className="w-[50px] text-[#969799]">경기시간</div>
                <div>{dayjs(Number(data.ticketPayment.gameStartTime)*1000).format(
                  "YYYY-MM-DD HH:mm"
                )}</div>
              </div>
              <div className="flex flex-row gap-5">
                <div className="w-[50px] text-[#969799]">좌석정보</div>
                <div className="flex flex-col">
                  {data.ticketPayment.seatNumber.map((item) => (
                    <div key={item}>
                      {data.ticketPayment.zoneName} {Number(item)}
                    </div>
                  ))}
                </div>
              </div>
            </div>
            
        </>
      )
    }
  }

  const variants = {
    init: { height: 0, opacity: 0 },
    visible : {height:150, opacity : 1},
  } 

  return (
  <>
    <div className="flex justify-center pb-2 pt-2">
      <div className="flex flex-col">
        <div className="flex flex-row items-center justify-between w-[340px] h-[32px] border border-none rounded-[5px] bg-[#2E2E3D] px-2 gap-2" onClick={toggleAccordion}>
          <div className="flex flex-row items-center">
            {bookStatus()}
            <p className="text-white text-[12px]">
              {headContent()}
            </p>
          </div>
          <div>
            <img
              src={Down}
              className={`w-4 h-4 transform transition-transform ${
                !isOpenAccordion ? "rotate-0" : "rotate-180"
              }`}
            />
          </div>
          </div>
          <AnimatePresence>
        {isOpenAccordion && (
              <motion.div className="w-[340px] bg-white rounded-b-[5px] p-2"
              variants={variants} initial="init" animate="visible" exit="init" transition={{duration : 0.5}}>
                {bodyContent()}

              <div className="pb-2">
                <div className="border-b-[0.5px] border-[#F1F2F4]"></div>
              </div>
              <div className="text-[10px] px-2">
                <div className="flex flex-row gap-5">
                  <div className="w-[50px] text-[#969799]">결제금액</div>
                    <div className={`${data.paymentType == 0 || data.paymentType == 3 ? 'bg-red-300' : 'bg-green-300'} rounded-md`}>
                      {data.paymentType == 0 || data.paymentType == 3 ? '-' : '+'}
                      {toEther(data.amount)} ETH
                    </div>
                </div>
                <div className="flex flex-row gap-5">
                  <div className="w-[50px] text-[#969799]">결제시간</div>
                  <div>{time.toString()}</div>
                </div>
                  </div>
          </motion.div>
        )}</AnimatePresence>
      </div>
      </div>
      </>
  );
};

export default PaymentItem;
