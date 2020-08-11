package com.example.lolhistory;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lolhistory.model.MatchHistory;
import com.example.lolhistory.model.MatchList;
import com.example.lolhistory.model.SummonerIDInfo;
import com.example.lolhistory.model.SummonerRankInfo;
import com.example.lolhistory.retrofit.APIClient;
import com.example.lolhistory.retrofit.RiotAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<SummonerIDInfo> summonerIDInfoLiveData;
    private MutableLiveData<SummonerRankInfo> summonerRankInfoLiveData;
    private MutableLiveData<HistoryAdapter> historyAdapterLiveData;

    private String summonerName = "";

    private SummonerIDInfo summonerIDInfo = null;
    private SummonerRankInfo summonerRankInfo = null;

    private List<MatchList.Match> matchList = new ArrayList<>();
    private ArrayList<MatchHistory> matchHistories = new ArrayList<>();

    private RiotAPI riotAPI = APIClient.getRiotClient().create(RiotAPI.class);

    public MainActivityViewModel() {
        summonerIDInfoLiveData = new MutableLiveData<>();
        summonerRankInfoLiveData = new MutableLiveData<>();
        historyAdapterLiveData = new MutableLiveData<>();
    }

    public void searchSummoner(String summonerName) {
        this.summonerName = summonerName;

        matchHistories.clear();
        getSummonerIdInfo();
    }

    public MutableLiveData<SummonerIDInfo> getSummonerIDInfoLiveData() {
        return summonerIDInfoLiveData;
    }

    public MutableLiveData<SummonerRankInfo> getSummonerRankInfoLiveData() {
        return summonerRankInfoLiveData;
    }

    public MutableLiveData<HistoryAdapter> getHistoryAdapterLiveData() {
        return historyAdapterLiveData;
    }

    private void getSummonerIdInfo() {
        riotAPI.getSummonerIdInfo(summonerName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SummonerIDInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SummonerIDInfo idInfo) {
                        summonerIDInfo = idInfo;
                        summonerName = summonerIDInfo.getSummonerName();
                        getSummonerRankInfo(idInfo.getSummonerId());
                        getMatchHistoryList(idInfo.getAccountId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TESTLOG", "[getSummonerIdInfo] onError: " + e);
                        summonerIDInfoLiveData.setValue(null);
                    }
                });
    }

    private void getSummonerRankInfo(String summonerId) {
        riotAPI.getSummonerRankInfo(summonerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<SummonerRankInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<SummonerRankInfo> summonerRankInfos) {
                        setSummonerRankInfo(summonerRankInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TESTLOG", "[getSummonerRankInfo] onError: " + e);
                        summonerRankInfoLiveData.setValue(null);
                    }
                });
    }

    private void getMatchHistoryList(String accountId) {
        riotAPI.getMatchHistoryList(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MatchList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MatchList list) {
                        matchList = list.getMatch();
                        int count = 0;
                        for (MatchList.Match match : matchList) {
                            if (count < 15) {
                                getMatchHistory(match.getGameId(), accountId);
                                count++;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TESTLOG", "[getMatchHistoryList] onError: " + e);
                        //historyAdapterLiveData.setValue(null);
                    }
                });
    }

    private void getMatchHistory(String matchId, String accountId) {
        riotAPI.getMatchHistory(matchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MatchHistory>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MatchHistory matchHistory) {
                        matchHistories.add(matchHistory);
                        if (matchHistories.size() > 14) {
                            HistoryAdapter historyAdapter = new HistoryAdapter(matchHistories, accountId);
                            historyAdapterLiveData.setValue(historyAdapter);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TESTLOG", "[getMatchHistory] onError: " + e);
                        //historyAdapterLiveData.setValue(null);
                    }
                });
    }

    private void setSummonerRankInfo(List<SummonerRankInfo> summonerRankInfos) {
        int soloRankTier = 0;
        int flexRankTier = 0;
        SummonerRankInfo soloRankInfo = null;
        SummonerRankInfo flexRankInfo = null;

        if (summonerRankInfos.isEmpty()) {
            summonerRankInfo = new SummonerRankInfo();
            summonerRankInfo.setTier("UNRANKED");
            summonerRankInfo.setRank("");
            summonerRankInfo.setSummonerName(summonerName);
        } else {
            for (SummonerRankInfo info : summonerRankInfos) {
                if (info.getQueueType().equals("RANKED_SOLO_5x5")) {
                    soloRankTier = calcTierNum(info.getTier(), info.getRank());
                    soloRankInfo = info;
                } else if(info.getQueueType().equals("RANKED_FLEX_SR")) {
                    flexRankTier = calcTierNum(info.getTier(), info.getRank());
                    flexRankInfo = info;
                }
            }
            if (soloRankTier < flexRankTier) {
                if (soloRankInfo != null)
                    summonerRankInfo = soloRankInfo;
            } else {
                if (flexRankInfo != null)
                    summonerRankInfo = flexRankInfo;
            }
        }
        summonerRankInfoLiveData.setValue(summonerRankInfo);
    }

    private int calcTierNum(String tier, String rank) {
        int tierNum = 0;
        switch (tier) {
            case "CHALLENGER":
                break;
            case "GRANDMASTER":
                tierNum += 10;
                break;
            case "MASTER":
                tierNum += 20;
                break;
            case "DIAMOND":
                tierNum += 30;
                break;
            case "PLATINUM":
                tierNum += 40;
                break;
            case "GOLD":
                tierNum += 50;
                break;
            case "SILVER":
                tierNum += 60;
                break;
            case "BRONZE":
                tierNum += 70;
                break;
            case "IRON":
                tierNum += 80;
                break;
            case "UNRANKED":
                tierNum += 90;
                break;
        }
        switch (rank) {
            case "I":
                tierNum += 1;
                break;
            case "II":
                tierNum += 2;
                break;
            case "III":
                tierNum += 3;
                break;
            case "IV":
                tierNum += 4;
                break;
        }
        return tierNum;
    }
}
