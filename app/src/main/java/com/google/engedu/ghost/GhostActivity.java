/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    public  TextView text,label;
    //private String userWordNew = null;
    private String computerWord = null;
int whoEndFirst;

    String wordFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        text = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        Button challenge =(Button) findViewById(R.id.challenge);
        Button restart=(Button) findViewById(R.id.restart);
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        challenge.setOnClickListener(this);
        restart.setOnClickListener(this);
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
     //   text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        wordFragment="";
        whoEndFirst=userTurn?1:0;
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
       // label = (TextView) findViewById(R.id.gameStatus);
        label.setText(COMPUTER_TURN);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             computerWord=dictionary.getGoodWordStartingWith(wordFragment,whoEndFirst);

                if(computerWord=="noWord"){
                    createToast("computer wins",1000);
                    onStart(null);

                }
                else if(computerWord=="sameAsPrefix"){
                    createToast("computer wins",1000);
                    onStart(null);

                }
                else
                {
                    if(wordFragment==null || wordFragment=="")
                    {
                       // System.out.println("in the wordfragment"+wordFragment);
                       // wordFragment="";
                        wordFragment=computerWord.substring(0,1);
                    }
                    else{
                        wordFragment=computerWord.substring(0,wordFragment.length()+1);
                    }
                    text.setText(wordFragment);
                }
                userTurn = true;
                label.setText(USER_TURN);

            }

            },3000);
        /*char randch=(char) (random.nextInt(26)+97);
        randch=Character.toLowerCase(randch);
        wordFragment = (String) text.getText();
        wordFragment+=randch;
        text.setText(wordFragment);*/
        // Do computer turn stuff then make it the user's turn again

    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        char c=(char)event.getUnicodeChar();
        c=Character.toLowerCase(c);

        if(c>='a' && c<='z')
        {

            wordFragment=String.valueOf(text.getText());
            wordFragment+=c;
            text.setText(wordFragment);
            computerTurn();
        }
        else
        {
            createToast("invalid input",1000);
        }
        return false;
    }



    private void createToast(String msg,int time){
        final Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        },time);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.challenge:
                  if(wordFragment.length()>=4)
                  {
                      String finalWord=dictionary.getGoodWordStartingWith(wordFragment,whoEndFirst);
                      if(finalWord.equals("noWord")){
                          createToast("you wins",3000);
                          onStart(null);
                      }
                      else if(finalWord.equals("sameAsPrefix"))
                      {
                          createToast("You wins",3000);
                          onStart(null);
                      }
                      else
                      {
                          createToast("computer wins! word Exists",3000);
                          onStart(null);
                      }
                  }
                  else
                  {
                      createToast("computer wins",3000);
                      onStart(null);
                  }
                break;
            case  R.id.restart:
                onStart(null);
                break;
        }
    }

}
