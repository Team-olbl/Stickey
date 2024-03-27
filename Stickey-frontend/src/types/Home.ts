
export interface IGameSimpleRes {
    id: number;
    gameImage: string;
    category: string;
    stadium: string;
    homeTeam: string;
    homeTeamLogo:string | null;
    awayTeam: string;
    awayTeamLogo:string | null;
    bookStartTime: string;
    bookEndTime: string;
    gameStartTime: string;
}

// 경기 목록 조회 Response
export interface IGameListRes {
    gameResList: IGameSimpleRes[];
}

// 경기 목록 조회 Request
export interface IGameListReq {
    catg?: string;
    club?: string[];
    date?: string;

}

// export interface ITeamSimpleRes {
//     id: number;
//     category: string;
//     name: string;
//     logo: string;
//     isPrefer: boolean;
// }

// 구단 리스트 조회 Response 
export interface ITeamListRes {
    // clubListRes: ITeamSimpleRes;
    id: number;
    category: string;
    name: string;
    logo: string;
    isPrefer: boolean;
}

// 구단 리스트 조회 Request 
export interface ITeamListReq {
    catg?: string;
}