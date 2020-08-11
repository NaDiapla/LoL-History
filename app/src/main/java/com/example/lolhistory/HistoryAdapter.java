package com.example.lolhistory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lolhistory.model.ChampionParsing;
import com.example.lolhistory.model.MatchHistory;
import com.example.lolhistory.model.QueueParsing;
import com.example.lolhistory.model.RuneParsing;
import com.example.lolhistory.model.SpellParsing;
import com.example.lolhistory.retrofit.BaseUrl;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<MatchHistory> matchHistory;
    private Context context;
    private String accountId;

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout resultLayout;
        TextView result;
        TextView durationTime;

        TextView gameType;
        ImageView championPortrait;
        ImageView spell1;
        ImageView spell2;
        ImageView keystoneRune;
        ImageView secondaryRune;

        TextView kda;
        ImageView item0;
        ImageView item1;
        ImageView item2;
        ImageView item3;
        ImageView item4;
        ImageView item5;
        ImageView item6;

        ViewHolder(View itemView) {
            super(itemView);

            resultLayout = itemView.findViewById(R.id.result_layout);
            result = itemView.findViewById(R.id.tv_result);
            durationTime = itemView.findViewById(R.id.tv_game_time);

            gameType = itemView.findViewById(R.id.tv_game_type);
            championPortrait = itemView.findViewById(R.id.iv_champion_portrait);
            spell1 = itemView.findViewById(R.id.iv_summoner_spell_1);
            spell2 = itemView.findViewById(R.id.iv_summoner_spell_2);
            keystoneRune = itemView.findViewById(R.id.iv_keystone_rune);
            secondaryRune = itemView.findViewById(R.id.iv_secondary_rune);

            kda = itemView.findViewById(R.id.tv_kda);
            item0 = itemView.findViewById(R.id.iv_item_0);
            item1 = itemView.findViewById(R.id.iv_item_1);
            item2 = itemView.findViewById(R.id.iv_item_2);
            item3 = itemView.findViewById(R.id.iv_item_3);
            item4 = itemView.findViewById(R.id.iv_item_4);
            item5 = itemView.findViewById(R.id.iv_item_5);
            item6 = itemView.findViewById(R.id.iv_item_6);
        }
    }

    HistoryAdapter(ArrayList<MatchHistory> matchHistory, String accountId) {
        Collections.sort(matchHistory, (p1, p2) -> Long.compare(p1.getGameCreation(), p2.getGameCreation()));
        Collections.reverse(matchHistory);
        this.matchHistory = matchHistory;
        this.accountId = accountId;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.match_history_item, parent, false);
        HistoryAdapter.ViewHolder vh = new HistoryAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchHistory match = matchHistory.get(position);
        int playerIndex = getPlayerIndex(match);

        if (match.getParticipants().get(playerIndex).getStats().isWin()) {
            holder.resultLayout.setBackgroundColor(context.getColor(R.color.colorWin));
            holder.result.setText(R.string.win);
        } else {
            holder.resultLayout.setBackgroundColor(context.getColor(R.color.colorDefeat));
            holder.result.setText(R.string.defeat);
        }
        holder.durationTime.setText(getDurationTime(match.getGameDuration()));

        holder.gameType.setText(getQueueType(match.getQueueId()));

        Glide.with(context).load(getChampionPortraitURL(match, playerIndex)).into(holder.championPortrait);
        Glide.with(context).load(getSpellImageURL(match, playerIndex, 0)).into(holder.spell1);
        Glide.with(context).load(getSpellImageURL(match, playerIndex, 1)).into(holder.spell2);
        Glide.with(context).load(getRuneImageURL(match, playerIndex, 0)).into(holder.keystoneRune);
        Glide.with(context).load(getRuneImageURL(match, playerIndex, 1)).into(holder.secondaryRune);

        holder.kda.setText(getKDA(match, playerIndex));
        Glide.with(context).load(getItemImageURL(match, playerIndex, 0)).into(holder.item0);
        Glide.with(context).load(getItemImageURL(match, playerIndex, 1)).into(holder.item1);
        Glide.with(context).load(getItemImageURL(match, playerIndex, 2)).into(holder.item2);
        Glide.with(context).load(getItemImageURL(match, playerIndex, 3)).into(holder.item3);
        Glide.with(context).load(getItemImageURL(match, playerIndex, 4)).into(holder.item4);
        Glide.with(context).load(getItemImageURL(match, playerIndex, 5)).into(holder.item5);
        Glide.with(context).load(getItemImageURL(match, playerIndex, 6)).into(holder.item6);
    }

    @Override
    public int getItemCount() {
        return matchHistory.size();
    }

    private int getPlayerIndex(MatchHistory match) {
        int i = 0;
        for (MatchHistory.ParticipantIdentities participantIdentities : match.getParticipantIdentities()) {
            if (participantIdentities.getPlayer().getAccountId().equals(accountId)) {
                break;
            } else {
                i++;
            }
        }
        return i;
    }

    private String getDurationTime(int secondTime){
        int min = secondTime / 60;
        int sec = secondTime % 60;

        return min + ":" + sec;
    }

    private String getQueueType(int queueId) {
        QueueParsing queueParsing = new QueueParsing(context);
        return queueParsing.getQueueType(queueId);
    }

    private String getChampionPortraitURL(MatchHistory match, int playerIndex) {
        int championId = match.getParticipants().get(playerIndex).getChampionId();
        ChampionParsing championParsing = new ChampionParsing(context);
        String championName = championParsing.getChampionName(championId);

        String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_CHAMPION_PORTRAIT;
        imageUrl += championName + ".png";

        Log.d("TESTLOG", "[getChampionPortraitURL] imageURL: " + imageUrl);
        return imageUrl;
    }

    private String getSpellImageURL(MatchHistory match, int playerIndex, int spellIndex) {
        int spell;
        if (spellIndex == 0) {
            spell = match.getParticipants().get(playerIndex).getSpell1Id();
        } else {
            spell = match.getParticipants().get(playerIndex).getSpell2Id();
        }
        SpellParsing spellParsing = new SpellParsing(context);
        String spellName = spellParsing.getSpellName(spell);

        String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_SPELL_IMAGE;
        imageUrl += spellName + ".png";

        Log.d("TESTLOG", "[getSpellImageURL] imageURL: " + imageUrl);
        return imageUrl;
    }

    private String getRuneImageURL(MatchHistory match, int playerIndex, int runeIndex) {
        int rune;
        if (runeIndex == 0) {
            rune = match.getParticipants().get(playerIndex).getStats().getPerk0();
        } else {
            rune = match.getParticipants().get(playerIndex).getStats().getPerkSubStyle();
        }
        RuneParsing runeParsing = new RuneParsing(context);
        String iconUrl = runeParsing.getRuneIconURL(rune);

        String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_RUNE_IMAGE;
        imageUrl += iconUrl;

        Log.d("TESTLOG", "[getRuneImageURL] imageURL: " + imageUrl);
        return imageUrl;
    }

    private String getKDA(MatchHistory match, int playerIndex) {
        int kill = match.getParticipants().get(playerIndex).getStats().getKills();
        int death = match.getParticipants().get(playerIndex).getStats().getDeath();
        int assist = match.getParticipants().get(playerIndex).getStats().getAssists();

        return kill + " / " + death + " / " + assist;
    }

    private String getItemImageURL(MatchHistory match, int playerIndex, int itemIndex) {
        int itemId = 0;
        switch (itemIndex) {
            case 0:
                itemId = match.getParticipants().get(playerIndex).getStats().getItem0();
                break;
            case 1:
                itemId = match.getParticipants().get(playerIndex).getStats().getItem1();
                break;
            case 2:
                itemId = match.getParticipants().get(playerIndex).getStats().getItem2();
                break;
            case 3:
                itemId = match.getParticipants().get(playerIndex).getStats().getItem3();
                break;
            case 4:
                itemId = match.getParticipants().get(playerIndex).getStats().getItem4();
                break;
            case 5:
                itemId = match.getParticipants().get(playerIndex).getStats().getItem5();
                break;
            case 6:
                itemId = match.getParticipants().get(playerIndex).getStats().getItem6();
                break;
        }
        String imageUrl = BaseUrl.RIOT_DATA_DRAGON_GET_ITEM_IMAGE;
        imageUrl += itemId + ".png";

        return imageUrl;
    }
}
