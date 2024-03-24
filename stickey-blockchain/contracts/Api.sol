// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

import "./Reword.sol";
import "./Ticket.sol";
import "./Support.sol";
import "./Game.sol";
import "./Item.sol";

contract Api is Support, Ticket, Item, Game {

  Reword reword; // ERC20 구현한 Reword 컨트랙트

  constructor(address _rewordContractAddress) {
    reword = Reword(_rewordContractAddress);
    reword.setCaller(address(this));
  }


   // 티켓 예매
  function _createTicket(uint _number, uint _gameId, uint _stadiumId, uint _zoneId, uint[] calldata _seatNumber) internal {
    require(0 < _number && _number < 5, "Wrong Ticket Number"); // 티켓의 매수는 1 ~ 4
    uint price = _getSeatPrice(_stadiumId, _zoneId); // 가격 확인, 좌석 가격 * 매수

    require(msg.value == price * _number, "Wrong price"); // 가격이 맞지않으면 반려

    for(uint i = 0; i < _number; i++) { // 매수만큼 반복
      if(_getRefundAddress(_gameId, _zoneId, _seatNumber[i]) != address(0)) {
        payable(_getRefundAddress(_gameId, _zoneId, _seatNumber[i])).transfer(price * 30 / 100);
        _setRefundAddress(_gameId, _zoneId, _seatNumber[i], address(0));
      }
      _mintTicket(_gameId, _zoneId, _seatNumber[i], price);
      _setSeatState(_gameId, _zoneId, _seatNumber[i], true);
    }
    reword.mintReword(msg.sender, price * _number * 5 / 100); // 0.05% reword 
  }


  // 티켓 취소 
  function _refundTicket(uint256 _tokenId) internal {
    require(ownerOf(_tokenId) == msg.sender, "you're not owner of this ticket"); // 티켓 소유자 확인
    
    uint256 nowTime = block.timestamp;
    TicketInfo memory t = _getTicket(_tokenId);
    GameInfo memory g = _getGame(t.gameId);

    require(nowTime < g.gameStartTime, "already game started");
    
    uint refundTime = g.gameStartTime - 3 days; 
    uint refundPrice = t.price;

    require(refundPrice * 5 / 100 <= reword.balanceOf(msg.sender), "you already use Reword Token");
    reword.burnReword(msg.sender, refundPrice * 5 / 100, true);

    if(nowTime >= refundTime) {
      refundPrice = refundPrice * 70 / 100;
      _setRefundAddress(t.gameId, t.zoneId, t.seatNumber, msg.sender);
    } 
    payable(msg.sender).transfer(refundPrice);
    _cancleTicket(_tokenId);
    _setSeatState(t.gameId, t.zoneId, t.seatNumber, false);
  }

  // 티켓 조회 호출시 보내는 구조체
  struct TicketDetail {
    uint tokenId;
    uint gameId;
    string stadium;
    string zoneName;
    uint seatNumber;
    uint price;
    uint filterId;
    uint backgroundId;
    Category category;
    string homeTeam;
    string awayTeam;
    string poster;
  }

   // 가진 티켓 조회
  function _getTickets(address _addr) internal view returns (TicketDetail[] memory) {
    uint[] memory ticketIds = _getTicketsByAccount(_addr);
    TicketDetail[] memory tickets = new TicketDetail[](ticketIds.length);

    for(uint i = 0; i < ticketIds.length; i++) {
      TicketInfo memory t = _getTicket(ticketIds[i]);
      GameInfo memory g = _getGame(t.gameId);

      tickets[i] = TicketDetail({
        tokenId: ticketIds[i],
        gameId: t.gameId,
        stadium: g.stadium,
        zoneName: _getZoneName(t.zoneId),
        seatNumber: t.seatNumber,
        price: t.price,
        filterId: t.filterId,
        backgroundId: t.backgroundId,
        category: g.category,
        homeTeam: g.homeTeam,
        awayTeam: g.awayTeam,
        poster: g.poster
      });
    }
    return tickets;
  }

  // 필터 아이템 적용
  function _setFilterOnTicket(uint _tokenId, uint _itemId, uint _supportId) internal {
    require(ownerOf(_tokenId) == msg.sender, "you're not owner of this ticket"); // 티켓 소유자 확인
    
    ItemInfo memory i = _getFilter(_itemId);
    require(reword.balanceOf(msg.sender) >= i.price, "Wrong amount");  // 아이템 가격 만큼 꿈이 있는지 확인

    _ticketInfo[_tokenId].filterId = _itemId;

    _supportInfo[_supportId].balance += i.price / 2;    
    reword.burnReword(msg.sender, i.price, false);
  }

  // 배경색 아이템 적용
  function _setBackgroundOnTicket(uint _tokenId, uint _itemId, uint _supportId) internal {
    require(ownerOf(_tokenId) == msg.sender, "you're not owner of this ticket"); // 티켓 소유자 확인
    
    ItemInfo memory i = _getBackground(_itemId);
    require(reword.balanceOf(msg.sender) >= i.price, "Wrong amount");  // 아이템 가격 만큼 꿈이 있는지 확인

    _ticketInfo[_tokenId].backgroundId = _itemId;

    _supportInfo[_supportId].balance += i.price / 2;    
    reword.burnReword(msg.sender, i.price, false);
  }


}