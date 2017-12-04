/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MainActivity extends AppCompatActivity {
  Button send;
  FloatingActionButton retrive;
  EditText txtName, txtScore;
  String playerName;

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    send = findViewById(R.id.btSend);
    retrive = findViewById(R.id.btRetrive);
    txtName = findViewById(R.id.txtName);
    txtScore = findViewById(R.id.txtScore);

    //ParseAnalytics.trackAppOpenedInBackground(getIntent());

    retrive.setEnabled(false);

    Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("123456")
            .server("http://192.168.0.26:1337/parse/")
            .build());

    send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", Integer.parseInt(txtScore.getText().toString()));
        gameScore.put("playerName", txtName.getText().toString());
        gameScore.put("cheatMode", false);
        gameScore.saveInBackground();
        setPlayerName(txtName.getText().toString());

        txtName.setText("");
        txtScore.setText("");
        retrive.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Se registro tu Player!", Toast.LENGTH_SHORT).show();
      }
    });

    retrive.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.whereEqualTo("playerName", getPlayerName());
        query.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {
            if (e==null){
              AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
              dialogo1.setTitle("Ultimo player registrado");
              dialogo1.setMessage("ObjectId: "+objects.get(0).getObjectId()+"\n"+
                      "PlayerName: "+objects.get(0).getString("playerName")+"\n"+
                      "Score: "+objects.get(0).getInt("score")+"\n"+
                      "CheatMode: "+objects.get(0).getBoolean("cheatMode")+"\n");
              dialogo1.setCancelable(false);
              dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                  dialogo1.cancel();
                }
              });
              dialogo1.show();
            }else{
              Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    });
  }
}
