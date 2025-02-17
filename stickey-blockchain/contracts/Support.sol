// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0 <0.9.0;

// 후원 관련 컨트랙트
contract Support {

  // 후원글에 대한 후원 정보
  struct SupportInfo {
    uint id;              // 후원글 ID
    uint balance;         // 현재 모인 후원금
    uint endTime;         // 후원 마감 시간
    string name;          // 후원단체 이름
    address addr;         // 후원단체의 지갑 주소 
  }

  // 후원받은 내역 구조체
  struct SupportedHistory {
    uint amount;          // 후원한 양
    uint time;     // 후원한 시간
    string text;          // 닉네임, 한 줄 글
  }


  // 후원 글 정보 ( 후원 글 ID => 후원 글 정보 )
  mapping(uint => SupportInfo) internal _supportInfo;



  // 후원받은 내역 ( 후원 글 ID => 후원 내역 )
  mapping(uint => SupportedHistory[]) private _supportedHistory;

  // 후원 글 등록
  function _setSupport(uint _id, string calldata _name, address _addr, uint _endTime) internal {
    _supportInfo[_id] = SupportInfo({
      id: _id,
      name: _name,
      addr: _addr,
      balance: 0,
      endTime: _endTime
    });
  }

  // 후원
  function _donate(uint _supportId, uint _amount, string memory _text) internal {
    // 현재 시간이 endTime보다 작아야 후원 가능
    require(block.timestamp < _supportInfo[_supportId].endTime, "Sponsorship has ended.");

    // 후원받은 내역 저장
    _supportedHistory[_supportId].push(SupportedHistory({
      text: _text,
      amount: _amount,
      time: block.timestamp
    }));

    // 모은 금액 증가
    _supportInfo[_supportId].balance += _amount;
  }
  
  // 후원금 단체 지갑으로 출금
  function _withdraw(uint _supportId) internal {
    SupportInfo memory s = _supportInfo[_supportId];
    require(s.addr == msg.sender, "Permission denied.");
    require(s.endTime <= block.timestamp, "Sponsorship is not over yet.");
    require(s.balance > 0, "Already withdrawn or 0 balance.");
    payable(s.addr).transfer(s.balance);
    _supportInfo[_supportId].balance = 0;
  }

  // 후원 글 정보 조회
  function _getSupport(uint _supportId) internal view returns (SupportInfo memory) {
    return _supportInfo[_supportId];
  }


  // 후원받은 내역 조회
  function _getSupptedHistory(uint _supportId) internal view returns (SupportedHistory[] memory) {
    return _supportedHistory[_supportId];
  }

}