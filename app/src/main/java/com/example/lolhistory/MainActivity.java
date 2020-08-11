package com.example.lolhistory;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lolhistory.model.SummonerRankInfo;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel viewModel;

    LinearLayout inputLayout;
    EditText editTextSummoner;
    Button buttonSearch;

    ProgressBar progressBar;

    ConstraintLayout infoLayout;
    ImageView tierEmblem;
    TextView summonerName;
    TextView rankType;
    TextView tier;
    TextView lp;
    TextView winRate;
    TextView winLose;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerViewHistory;

    private InputMethodManager inputMethodManager;
    private boolean isVisibleInfoLayout = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getSummonerIDInfoLiveData().observe(this, summonerIDInfo -> {
            if (summonerIDInfo == null) {
                Toast notExistSummoner = Toast.makeText(getApplicationContext(), R.string.not_exist_summoner, Toast.LENGTH_SHORT);
                notExistSummoner.show();
                setVisibleProgressBar(View.GONE);
                setVisibleInputLayout(View.VISIBLE);
            }
        });

        viewModel.getSummonerRankInfoLiveData().observe(this, summonerRankInfo -> {
            if (summonerRankInfo == null) {
                Toast notExistSummoner = Toast.makeText(getApplicationContext(), R.string.not_exist_summoner, Toast.LENGTH_SHORT);
                notExistSummoner.show();
                setVisibleProgressBar(View.GONE);
                setVisibleInputLayout(View.VISIBLE);
            } else {
                setVisibleInputLayout(View.GONE);
                setRankInfo(summonerRankInfo);
            }
        });

        viewModel.getHistoryAdapterLiveData().observe(this, historyAdapter -> {
            setVisibleProgressBar(View.GONE);
            if (historyAdapter == null) {
                Toast notExistSummoner = Toast.makeText(getApplicationContext(), R.string.not_exist_summoner, Toast.LENGTH_SHORT);
                notExistSummoner.show();
                setVisibleProgressBar(View.GONE);
                setVisibleInputLayout(View.VISIBLE);
            } else {
                recyclerViewHistory.setAdapter(historyAdapter);
                swipeRefreshLayout.setRefreshing(false);
                setVisibleInfoLayout(View.VISIBLE);
                isVisibleInfoLayout = true;
            }
        });

        inputLayout = findViewById(R.id.layout_input_summoner);
        editTextSummoner = findViewById(R.id.et_input_summoner);
        buttonSearch = findViewById(R.id.btn_input_summoner);
        buttonSearch.setOnClickListener((v) -> {
            // ID 검색
            searchSummoner(editTextSummoner.getText().toString().trim());
        });

        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.GONE);

        infoLayout = findViewById(R.id.info_layout);
        tierEmblem = findViewById(R.id.iv_tier_emblem);
        summonerName = findViewById(R.id.tv_summoner_name);
        rankType = findViewById(R.id.tv_rank_type);
        tier = findViewById(R.id.tv_tier);
        lp = findViewById(R.id.tv_lp);
        winRate = findViewById(R.id.tv_total_win_rate);
        winLose = findViewById(R.id.tv_total_win_lose);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            searchSummoner(summonerName.getText().toString().trim());
        });
        recyclerViewHistory = findViewById(R.id.rv_history);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setHasFixedSize(true);

        setVisibleInfoLayout(View.GONE);

        inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onBackPressed() {
        if (isVisibleInfoLayout) {
            setVisibleInfoLayout(View.GONE);
            setVisibleInputLayout(View.VISIBLE);
            isVisibleInfoLayout = !isVisibleInfoLayout;
        } else {
            finish();
        }
    }

    private void searchSummoner(String summonerName) {
        setVisibleProgressBar(View.VISIBLE);
        inputMethodManager.hideSoftInputFromWindow(editTextSummoner.getWindowToken(), 0);
        viewModel.searchSummoner(summonerName);
    }

    private void setRankInfo(SummonerRankInfo summonerRankInfo) {
        setTierEmblem(summonerRankInfo.getTier());
        summonerName.setText(summonerRankInfo.getSummonerName());
        if (!summonerRankInfo.getTier().equals("UNRANKED")) {
            String tierText = summonerRankInfo.getTier() + " " +summonerRankInfo.getRank();
            tier.setText(tierText);
            rankType.setText(summonerRankInfo.getQueueType());
            String leaguePoint = summonerRankInfo.getLeaguePoints() + "LP";
            lp.setText(leaguePoint);

            double doubleWinRate = calcWinRate(summonerRankInfo.getWins(), summonerRankInfo.getLosses());
            winRate.setText(String.format(Locale.getDefault(), "%.2f%%", doubleWinRate));
            String winAndLooses = summonerRankInfo.getWins() + getResources().getString(R.string.win)
                    + " " + summonerRankInfo.getLosses() + getResources().getString(R.string.defeat);
            winLose.setText(winAndLooses);
        } else {
            tier.setText(summonerRankInfo.getTier());
            lp.setText("");
        }
    }

    private void setTierEmblem(String tier) {
        switch (tier) {
            case "CHALLENGER":
                tierEmblem.setImageResource(R.drawable.emblem_challenger);
                break;
            case "GRANDMASTER":
                tierEmblem.setImageResource(R.drawable.emblem_grandmaster);
                break;
            case "MASTER":
                tierEmblem.setImageResource(R.drawable.emblem_master);
                break;
            case "DIAMOND":
                tierEmblem.setImageResource(R.drawable.emblem_diamond);
                break;
            case "PLATINUM":
                tierEmblem.setImageResource(R.drawable.emblem_platinum);
                break;
            case "GOLD":
                tierEmblem.setImageResource(R.drawable.emblem_gold);
                break;
            case "SILVER":
                tierEmblem.setImageResource(R.drawable.emblem_silver);
                break;
            case "BRONZE":
                tierEmblem.setImageResource(R.drawable.emblem_bronze);
                break;
            case "IRON":
                tierEmblem.setImageResource(R.drawable.emblem_iron);
                break;
            case "UNRANKED":
                tierEmblem.setImageResource(R.drawable.emblem_unranked);
                break;
        }
    }

    private double calcWinRate(int win, int losses) {
        return (double) win / (double) (win + losses) * 100;
    }

    private void setVisibleProgressBar(int visible) {
        progressBar.setVisibility(visible);
    }

    private void setVisibleInputLayout(int visible) {
        inputLayout.setVisibility(visible);
    }

    private void setVisibleInfoLayout(int visible) {
        infoLayout.setVisibility(visible);
        recyclerViewHistory.setVisibility(visible);
    }
}
