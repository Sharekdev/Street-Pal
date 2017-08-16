package com.sharekeg.streetpal.homefragments;


import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharekeg.streetpal.R;
import com.sharekeg.streetpal.chatcomponents.ChatAdapter;
import com.sharekeg.streetpal.chatcomponents.ChatBlock;
import com.sharekeg.streetpal.chatcomponents.ChatMessage;
import com.sharekeg.streetpal.chatcomponents.UserGuide;
import com.sharekeg.streetpal.chatcomponents.UserOptions;
import com.sharekeg.streetpal.safeplace.SafePlaceActivity;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.viewfilter.core.ViewFilter;
import ir.mirrajabi.viewfilter.renderers.BlurRenderer;


/**
 * Created by MMenem on 8/2/2017.
 */

public class StreetPalGuide extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    public LinearLayoutManager mLinearLayoutManager;
    UserGuide userGuide;
    ChatAdapter adapter;
    List<ChatMessage> chatMessages = new ArrayList<>();
    int positiveButtonID, negativeButtonID, neutralButtonID;
    RecyclerView guideChatList;
    RelativeLayout homeActivity;
    private TextView firstChoice, secondChoice, thirdChoice;
    private LinearLayout infoLayout;
    private RelativeLayout UpperBarlayoutId;


    public StreetPalGuide() {
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View streetPalGuideView = inflater.inflate(R.layout.fragment_street_pal_guide, container, false);
        //Initialize the recycle view
        guideChatList = (RecyclerView) streetPalGuideView.findViewById(R.id.chatList);
        guideChatList.setHasFixedSize(true);

        //Access the items of Parent Activity
        homeActivity = (RelativeLayout) getActivity().findViewById(R.id.activity_home);
        UpperBarlayoutId = (RelativeLayout) getActivity().findViewById(R.id.UpperBarlayoutId);

//        ViewFilter.getInstance(getContext())
//                .setRenderer(new BlurRenderer(16))
//                .applyFilterOnView(infoLayout, homeActivity);


        //find the TextViews (Choices)
        firstChoice = (TextView) streetPalGuideView.findViewById(R.id.user_first_choice);
        secondChoice = (TextView) streetPalGuideView.findViewById(R.id.user_second_choice);
        thirdChoice = (TextView) streetPalGuideView.findViewById(R.id.user_third_choice);


        firstChoice.setOnClickListener(this);
        secondChoice.setOnClickListener(this);
        thirdChoice.setOnClickListener(this);


        guideChatList.setLayoutManager(new LinearLayoutManager(getContext()));
//        mLinearLayoutManager.setStackFromEnd(true);

        userGuide = new UserGuide();
        int id = 0;
        if (getArguments() != null) {
            id = getArguments().getInt("userCase");
        }
        ChatBlock chatBlock = userGuide.guideUserToSafety(id, getContext());

        for (int i = 0; i < chatBlock.getChatMessages().size(); i++) {
            chatMessages.add(chatBlock.getChatMessages().get(i));
        }
        manageOptionsDisplay(chatBlock.getUserOptions());
        setButtonsIDs(chatBlock.getUserOptions());
        // Initialize the adapter for the messages

        adapter = new ChatAdapter(getActivity(), chatMessages);

        guideChatList.setAdapter(adapter);


        return streetPalGuideView;
    }

    @Override
    public void onClick(View v) {
        if (v == firstChoice) {
            Log.i("Button_ID_selected", String.valueOf(positiveButtonID));
            ChatMessage userMessage = new ChatMessage(firstChoice.getText().toString(), true);
            displayNewMessage(userMessage);
            ChatBlock newChatBlock = userGuide.guideUserToSafety(positiveButtonID, getContext());
            manageOptionsDisplay(newChatBlock.getUserOptions());
            setButtonsIDs(newChatBlock.getUserOptions());
            displayNewMessage(newChatBlock.getChatMessages());


        } else if (v == secondChoice) {
            Log.i("Button_ID_selected", String.valueOf(negativeButtonID));
            ChatMessage userMessage = new ChatMessage(secondChoice.getText().toString(), true);
            displayNewMessage(userMessage);
            ChatBlock newChatBlock = userGuide.guideUserToSafety(negativeButtonID, getContext());
            manageOptionsDisplay(newChatBlock.getUserOptions());
            setButtonsIDs(newChatBlock.getUserOptions());
            displayNewMessage(newChatBlock.getChatMessages());
        } else if (v == thirdChoice) {
            Log.i("Button_ID_selected", String.valueOf(neutralButtonID));
            ChatMessage userMessage = new ChatMessage(thirdChoice.getText().toString(), true);
            displayNewMessage(userMessage);
            ChatBlock newChatBlock = userGuide.guideUserToSafety(neutralButtonID, getContext());
            manageOptionsDisplay(newChatBlock.getUserOptions());
            setButtonsIDs(newChatBlock.getUserOptions());
            displayNewMessage(newChatBlock.getChatMessages());
        }
    }

    private void startGuideFragment() {

        GuideTab guide = new GuideTab();
        this.getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.rlFragments, guide)
                .commit();
    }

    private void showNearstSafePlace() {
        Intent startSafePlaceActivity = new Intent(getActivity(), SafePlaceActivity.class);
        startActivity(startSafePlaceActivity);
    }

    private void manageOptionsDisplay(UserOptions userOptions) {
        if (userOptions != null) {
            switch (userOptions.getOptionsCount()) {
                case 3:
                    firstChoice.setText(userOptions.getPositiveButtonText());
                    secondChoice.setText(userOptions.getNegativeButtonText());
                    thirdChoice.setText(userOptions.getNeutralButtonText());
                    break;
                case 2:
                    firstChoice.setText(userOptions.getPositiveButtonText());
                    secondChoice.setText(userOptions.getNegativeButtonText());
                    thirdChoice.setVisibility(View.GONE);
                    break;
                case 1:
                    firstChoice.setText(userOptions.getPositiveButtonText());
                    secondChoice.setVisibility(View.GONE);
                    thirdChoice.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void setButtonsIDs(UserOptions userOptions) {
        if (userOptions != null) {
            switch (userOptions.getOptionsCount()) {
                case 3:
                    positiveButtonID = userOptions.getPositiveButtonId();
                    negativeButtonID = userOptions.getNegativeButtonId();
                    neutralButtonID = userOptions.getNeutralButtonId();
                    break;
                case 2:
                    positiveButtonID = userOptions.getPositiveButtonId();
                    negativeButtonID = userOptions.getNegativeButtonId();
                    break;
                case 1:
                    positiveButtonID = userOptions.getPositiveButtonId();
                    break;
            }
        }
    }

    private void displayNewMessage(ArrayList<ChatMessage> messages) {


        for (int i = 0; i < messages.size(); i++) {
            chatMessages.add(messages.get(i));
        }

        final Handler timerHandler;
        timerHandler = new Handler();

        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                //  update  adapter data
                adapter.notifyDataSetChanged();
                timerHandler.postDelayed(this, 2000); //run every  2 seconds
            }
        };

        timerHandler.postDelayed(timerRunnable, 2000);


    }

    private void displayNewMessage(ChatMessage message) {

        chatMessages.add(message);

        final Handler timerHandler;
        timerHandler = new Handler();

        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                //  update  adapter data
                adapter.notifyDataSetChanged();
                timerHandler.postDelayed(this, 2000); //run every  2 seconds
            }
        };

        timerHandler.postDelayed(timerRunnable, 2000);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
