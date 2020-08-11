package com.example.lolhistory.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MatchHistory {
    @SerializedName("gameId")
    private String gameId;

    @SerializedName("queueId")
    private int queueId;

    @SerializedName("gameCreation")
    private long gameCreation;

    @SerializedName("gameDuration")
    private int gameDuration;

    @SerializedName("mapId")
    private String mapId;

    @SerializedName("participants")
    private ArrayList<Participants> participants = new ArrayList<>();

    @SerializedName("participantIdentities")
    private ArrayList<ParticipantIdentities> participantIdentities = new ArrayList<>();

    public static class Participants {
        @SerializedName("participantId")
        private int participantId;

        @SerializedName("teamId")
        private String teamId;

        @SerializedName("championId")
        private int championId;

        @SerializedName("spell1Id")
        private int spell1Id;

        @SerializedName("spell2Id")
        private int spell2Id;

        @SerializedName("stats")
        private Stats stats;

        public int getParticipantId() {
            return participantId;
        }

        public void setParticipantId(int participantId) {
            this.participantId = participantId;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public int getChampionId() {
            return championId;
        }

        public void setChampionId(int championId) {
            this.championId = championId;
        }

        public int getSpell1Id() {
            return spell1Id;
        }

        public void setSpell1Id(int spell1Id) {
            this.spell1Id = spell1Id;
        }

        public int getSpell2Id() {
            return spell2Id;
        }

        public void setSpell2Id(int spell2Id) {
            this.spell2Id = spell2Id;
        }

        public Stats getStats() {
            return stats;
        }

        public void setStats(Stats stats) {
            this.stats = stats;
        }

        public static class Stats {
            @SerializedName("win")
            private boolean win;

            @SerializedName("item0")
            private int item0;

            @SerializedName("item1")
            private int item1;

            @SerializedName("item2")
            private int item2;

            @SerializedName("item3")
            private int item3;

            @SerializedName("item4")
            private int item4;

            @SerializedName("item5")
            private int item5;

            @SerializedName("item6")
            private int item6;

            @SerializedName("kills")
            private int kills;

            @SerializedName("deaths")
            private int death;

            @SerializedName("assists")
            private int assists;

            @SerializedName("perk0")
            private int perk0;

            @SerializedName("perkSubStyle")
            private int perkSubStyle;

            public boolean isWin() {
                return win;
            }

            public void setWin(boolean win) {
                this.win = win;
            }

            public int getItem0() {
                return item0;
            }

            public void setItem0(int item0) {
                this.item0 = item0;
            }

            public int getItem1() {
                return item1;
            }

            public void setItem1(int item1) {
                this.item1 = item1;
            }

            public int getItem2() {
                return item2;
            }

            public void setItem2(int item2) {
                this.item2 = item2;
            }

            public int getItem3() {
                return item3;
            }

            public void setItem3(int item3) {
                this.item3 = item3;
            }

            public int getItem4() {
                return item4;
            }

            public void setItem4(int item4) {
                this.item4 = item4;
            }

            public int getItem5() {
                return item5;
            }

            public void setItem5(int item5) {
                this.item5 = item5;
            }

            public int getItem6() {
                return item6;
            }

            public void setItem6(int item6) {
                this.item6 = item6;
            }

            public int getKills() {
                return kills;
            }

            public void setKills(int kills) {
                this.kills = kills;
            }

            public int getDeath() {
                return death;
            }

            public void setDeath(int death) {
                this.death = death;
            }

            public int getAssists() {
                return assists;
            }

            public void setAssists(int assists) {
                this.assists = assists;
            }

            public int getPerk0() {
                return perk0;
            }

            public void setPerk0(int perk0) {
                this.perk0 = perk0;
            }

            public int getPerkSubStyle() {
                return perkSubStyle;
            }

            public void setPerkSubStyle(int perkSubStyle) {
                this.perkSubStyle = perkSubStyle;
            }
        }
    }

    public static class ParticipantIdentities {
        @SerializedName("participantId")
        private int participantId;

        @SerializedName("player")
        private Player player;

        public int getParticipantId() {
            return participantId;
        }

        public void setParticipantId(int participantId) {
            this.participantId = participantId;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public static class Player {
            @SerializedName("accountId")
            private String accountId;

            @SerializedName("summonerName")
            private String summonerName;

            public String getAccountId() {
                return accountId;
            }

            public void setAccountId(String accountId) {
                this.accountId = accountId;
            }

            public String getSummonerName() {
                return summonerName;
            }

            public void setSummonerName(String summonerName) {
                this.summonerName = summonerName;
            }
        }
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public long getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(long gameCreation) {
        this.gameCreation = gameCreation;
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public ArrayList<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participants> participants) {
        this.participants = participants;
    }

    public ArrayList<ParticipantIdentities> getParticipantIdentities() {
        return participantIdentities;
    }

    public void setParticipantIdentities(ArrayList<ParticipantIdentities> participantIdentities) {
        this.participantIdentities = participantIdentities;
    }
}
