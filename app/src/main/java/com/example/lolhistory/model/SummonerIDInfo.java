package com.example.lolhistory.model;

import com.google.gson.annotations.SerializedName;

public class SummonerIDInfo {
    @SerializedName("id")
    private String summonerId;

    @SerializedName("accountId")
    private String accountId;

    @SerializedName("puuid")
    private String puuid;

    @SerializedName("name")
    private String summonerName;

    public String getSummonerId() {
        return summonerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPuuid() {
        return puuid;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }
}