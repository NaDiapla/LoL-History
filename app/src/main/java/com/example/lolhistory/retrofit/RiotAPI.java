package com.example.lolhistory.retrofit;

import com.example.lolhistory.model.MatchHistory;
import com.example.lolhistory.model.MatchList;
import com.example.lolhistory.model.SummonerIDInfo;
import com.example.lolhistory.model.SummonerRankInfo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RiotAPI {
    // Summoner ID 가져오기
    // accountId: Match History 가져오는데 필요
    // summonerId: Rank 정보 가져오는데 필요
    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_SUMMONER + "{userId}")
    Single<SummonerIDInfo> getSummonerIdInfo(@Path("userId") String userId);

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_SUMMONER_BY_ENCRYPTION_SUMMONER_ID + "{summonerId}")
    Single<SummonerIDInfo> getSummonerIdInfoByAccount(@Path("summonerId") String summonerId);

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_RANK + "{userId}")
    Single<List<SummonerRankInfo>> getSummonerRankInfo(@Path("userId") String userId);

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_MATCH_LIST + "{accountId}")
    Single<MatchList> getMatchHistoryList(@Path("accountId") String accountId);

    @Headers({"Accept: application/json", "X-Riot-Token: " + BaseUrl.RIOT_API_KEY})
    @GET(BaseUrl.RIOT_API_GET_MATCH + "{matchId}")
    Single<MatchHistory> getMatchHistory(@Path("matchId") String matchId);

}
