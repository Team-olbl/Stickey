// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

import "./Api.sol";

contract ApplicationHandler is Api {

  address owner; // 관리자 Address

  constructor(address _rewordContractAddress) Api(_rewordContractAddress) {
    owner = msg.sender;
    initDummy();
  }

  function initDummy() private {
    _setZoneName(1, unicode"S구역 1"); 
    _setZoneName(2, unicode"S구역 2"); 
    _setZoneName(3, unicode"R구역 1"); 
    _setZoneName(4, unicode"R구역 2"); 
    _setZoneName(5, unicode"R구역 3"); 
    _setZoneName(6, unicode"R구역 4"); 
    _setZoneName(7, unicode"W구역 1"); 
    _setZoneName(8, unicode"W구역 2"); 
    _setZoneName(9, unicode"E구역 1"); 
    _setZoneName(10, unicode"E구역 2"); 
    _setZoneName(11, unicode"E구역 3"); 
    _setZoneName(12, unicode"E구역 4"); 

    uint value = 10e10;

    _setSeatPrice(1, 1, 20000*value);
    _setSeatPrice(1, 2, 20000*value);
    _setSeatPrice(1, 3, 15000*value);
    _setSeatPrice(1, 4, 15000*value);
    _setSeatPrice(1, 5, 15000*value);
    _setSeatPrice(1, 6, 15000*value);
    _setSeatPrice(1, 7, 10000*value);
    _setSeatPrice(1, 8, 10000*value);
    _setSeatPrice(1, 9, 10000*value);
    _setSeatPrice(1, 10, 10000*value);
    _setSeatPrice(1, 11, 10000*value);
    _setSeatPrice(1, 12, 10000*value);
  } 

  /*
  ========== 경기 API ==========
  */

  // 게임 정보 설정
  function setGame(uint _id, uint _bookStartTime, uint _gameStartTime, string memory _stadium, string memory _homeTeam, string memory _awayTeam, Category _category, string memory _gameImage) public isOwner {
    _setGame(_id, _bookStartTime, _gameStartTime, _stadium, _homeTeam, _awayTeam, _category, _gameImage);
  }

  // 경기장 구역 가격 설정
  function setSeatPrice(uint _stadiumId, uint _zoneId, uint _price) public isOwner {
    _setSeatPrice(_stadiumId, _zoneId, _price);
  }

  // 구역 이름 설정
  function setZoneName(uint _zoneId, string calldata _zoneName) public isOwner {
    _setZoneName(_zoneId, _zoneName);
  }

  // 좌석 상태 조회
  function getSeatState(uint _gameId, uint _zoneId, uint _seatNum) external view returns (bool) {
    return _getSeatState(_gameId, _zoneId, _seatNum);
  }


  /*
  ========== 아이템 API ==========
  */

  // 아이템 조회  
  function getItemList() external view returns (ItemInfo[] memory, ItemInfo[] memory) {
    return _getItemList();
  }

  // 필터 아이템 추가
  function addFilter(uint _id, uint _price) public isOwner {
    _addFilter(_id, _price);
  }
  
  // 필터 아이템 삭제
  function deleteFilter(uint _id) external isOwner {
    _deleteFilter(_id);
  }

  // 배경색 아이템 추가
  function addBackground(uint _id, uint _price) public isOwner {
    _addBackground(_id, _price);
  }
  
  // 배경색 아이템 삭제
  function deleteBackground(uint _id) external isOwner {
    _deleteBackground(_id);
  }

    // 필터 아이템 적용
  function setFilterOnTicket(uint _tokenId, uint _itemId, uint _supportId) external {
    _setFilterOnTicket(_tokenId, _itemId, _supportId);
  }

  // 배경색 아이템 적용
  function setBackgroundOnTicket(uint _tokenId, uint _itemId, uint _supportId) external {
    _setBackgroundOnTicket(_tokenId, _itemId, _supportId);
  }


  /*
  ========== 티켓 API ==========
  */

  // 티켓 예매
  function createTicket(uint _number, uint _gameId, uint _stadiumId, uint _zoneId, uint _zoneIdx, uint[] calldata _seatNum) external payable {
    _createTicket(_number, _gameId, _stadiumId, _zoneId, _zoneIdx, _seatNum);
  }

  // 티켓 취소 
  function refundTicket(uint256 _tokenId) external {
    _refundTicket(_tokenId);
  }

  // 가진 티켓 조회
  function getTickets(address _addr) external view returns (TicketDetail[] memory) {
    return _getTickets(_addr);
  }


  /*
  ========== 후원 API ==========
  */

  // 후원 글 등록
  function setSupport(uint _id, string calldata _name, address _addr, uint _endTime) external isOwner {
    _setSupport(_id, _name, _addr, _endTime);
  }

  // 후원
  function donate(uint _supportId, string memory _text) external payable {
    _donateWithHistory(_supportId, msg.value, _text);
  }

  // 후원금 수령
  function withdraw(uint _supportId) external {
    _withdraw(_supportId);
  }

  // 후원받은 내역 조회
  function getSupprtedHistory(uint _supportId) external view returns(SupportedHistory[] memory) {
    return _getSupptedHistory(_supportId);
  }

  /*
  ========== 꿈 API ==========
  */

  // 꿈 증감 내역 조회
  function getRewordHistory(address _addr) external view returns (Reword.RewordHistory[] memory) {
    return reword.getRewordHistory(_addr); 
  }

  // 꿈 잔액 조회
  function getReword(address _addr) external view returns (uint) {
    return reword.balanceOf(_addr);
  }

  /*
  ========== 결제 이력 API ==========
  */

  // 결제 이력 조회
  function getPaymentHistory(address _addr) external view returns (PaymentHistory[] memory) {
    return _getPaymentHistory(_addr);
  }


  modifier isOwner() {
    require(msg.sender == owner, "Permission denied.");
    _;
  }
}