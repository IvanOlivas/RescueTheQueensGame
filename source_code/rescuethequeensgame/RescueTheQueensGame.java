/*
Ivan Olivas
Program opens a GUI in which a player will play the Rescue the Queens game.
The goal of this game is to find and save all of the Queens hidden on an N x N game board of
cells (or squares) before all of the empty cells on the board are revealed. During the game play,
the player will click on board cells one at a time. If the cell is empty, that cell will be revealed
and all empty cells around the square will also be revealed. If the player clicks on a cell
containing a Queen, the player has successfully rescued a Queen, and the Queen will be
displayed in the cell for the duration of the game play plus the score is updated. The game ends
when either all of the Queens are rescued and the player wins, or all of the empty cells are
revealed causing the player to lose. 
*/

package rescuethequeensgame;

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import java.util.Random;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class RescueTheQueensGame { 
        
        static class GUI extends JFrame{
        //declare all GUI components
        JPanel windowContent;
        JTextField displayField;
        JButton buttons[][];
        JPanel pl;
        int remainQ; 
        int score;
        String[][] vArr;
        boolean won = false;
        boolean lost = false;
        int cntReveal;
        
            public GUI(int n, String vArray[][], int numQ) {
                windowContent = new JPanel();
                buttons = new JButton[n][n];        //set JButtons array to size of vArray from Queens
                remainQ = numQ;                 //tracks the number of queens left on the board
                final int iniQueen = numQ;          //the initial amount of queens when the game started
                score = 0;          //how many queens have been found
                vArr = vArray;                  //array that contains the values of the JButttons
                int blanks = (n*n)-iniQueen;            //number of blank JButton values, used to know when user has lost

                //setup layout manager for panel
                BorderLayout bl = new BorderLayout();
                windowContent.setLayout(bl);

                //create display field and place it in the north area of window
                displayField = new JTextField(30);
                displayField.setText("Score: " +score+ "   Remaining Queens: "+remainQ);
                displayField.setFont(new Font("SanSerif", Font.PLAIN, 14));
                displayField.setEditable(false);
                windowContent.add("North", displayField);

                //create button array and initalize values and logic performed
                for (int i = 0; i < buttons.length; i++) {
                    for (int j = 0; j < buttons[i].length; j++) {

                        //JButton constructor
                        buttons[i][j] = new JButton(String.valueOf("*"));      //set JButton to show *
                        buttons[i][j].setFont(new Font("SanSerif", Font.PLAIN, 24));

                        final JButton fbutton = buttons[i][j];  //create final variable to be able to reference values in actionlistener
                        
                        final String valA = vArr[i][j];
                        final int r = i;        //create final variables to pass to displaySurroundingBlanksActionPerformed
                        final int c = j;    

                        fbutton.addActionListener(
                            new ActionListener() {
                               
                                public void actionPerformed(ActionEvent event) {        //all logic when a JButton is clicked

                                    if(won == false && lost == false) {  //if all queens have not been found yet and all blanks have not been revealed
                                        if(valA.equals("Q") && !fbutton.getText().equals("\u2655")) {          //compute logic if the vArr[i][j] has the queen and the JButton has not been flipped to show the crown
                                            fbutton.setText("\u2655");    //display queen crown on JButton
                                            
                                            //update variables for score board
                                            score++;
                                            remainQ--;

                                            //score board updates
                                            if(score == iniQueen) {     //winning display
                                                displayField.setText("Score: " +score+ "   Remaining Queens: "+remainQ+ "       ** CONGRATS, YOU'VE WON! **");        //winning display
                                                won = true;    //stop performing any actions for clicks  
                                            } else {
                                            displayField.setText("Score: " +score+ "   Remaining Queens: "+remainQ);        //update score board
                                            }
                                        }
                                        
                                        else if(fbutton.getText().equals("*")) {        //only display blank when the button has an *
                                            displaySurroundingBlanksActionPerformed(event, r, c, fbutton);      //call method to display all surrounding blanks next to vArr[r][c]
                                            
                                            if(cntReveal == blanks) {
                                                displayField.setText("Score: " +score+ "   Remaining Queens: "+remainQ+ "       YOU HAVE LOST :( ");        //losing display
                                                lost = true;
                                            }                                            
                                        }
                                    
                                    } //end of if - won == false , lost == false
                                    
                                    else if(won == false && lost == true) {         //still allow user to reveal where queens are after they lost without updating scoreboard
                                        if(valA.equals("Q") && !fbutton.getText().equals("\u2655")) {
                                            fbutton.setText("\u2655");    //display crown
                                        }
                                    }
                                }   //end of performedAction
                            }
                        );  //end of actionlistener                  
                    }   //end of for loop
                }   //end of for loop for buttons 

                //create a panel with a gridLayout that will contain button
                pl = new JPanel();
                GridLayout gl = new GridLayout(n,n);    //row, cols
                pl.setLayout(gl);

                //add the buttons to the panel pl
                for (int i = 0; i < buttons.length; i++) {
                    for (int j = 0; j < buttons[i].length; j++) {
                        pl.add(buttons[i][j]);
                    }
                }

                //add the pl panel to the center of the borderlayout window
                windowContent.add("Center", pl);

                //create the frame and set its content pane
                JFrame frame = new JFrame("Rescue the Queens");
                frame.setContentPane(windowContent);

                //set the size of the window to be just big enough to fit all content
                frame.pack();

                //display the window
                frame.setVisible(true);
        }
            
        //uses recursion to reveal JButtons     
        private int displaySurroundingBlanksActionPerformed(ActionEvent event, int r, int c, JButton fbutton) {
            String valA = vArr[r][c];
            fbutton = buttons[r][c];
            int maxI = vArr.length-1;
            
            if(fbutton.getText().equals("*") && valA.equals("B")) {     //button has not been clicked on before and is a blank value
                cntReveal++;    //count the button revealed
                
                //corners
                if(r == 0 && c == 0) {  //TL
                    fbutton.setText(" ");
                    displaySurroundingBlanksActionPerformed(event, r+1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c+1, fbutton);
                }
                else if(r == 0 && c == maxI) {  //TR
                    fbutton.setText(" ");
                    displaySurroundingBlanksActionPerformed(event, r+1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c-1, fbutton);
                }
                else if(r == maxI && c == 0) {  //BL
                    fbutton.setText(" ");
                    displaySurroundingBlanksActionPerformed(event, r-1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c+1, fbutton);
                }
                else if(r == maxI && c == maxI) {   //BR
                    fbutton.setText(" ");
                    displaySurroundingBlanksActionPerformed(event, r-1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c-1, fbutton);
                }
          
                //row 0
                else if(r == 0) {
                    fbutton.setText(" ");
                    //bottom
                    displaySurroundingBlanksActionPerformed(event, r, c+1, fbutton);

                    //left, right
                    displaySurroundingBlanksActionPerformed(event, r, c-1, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c+1, fbutton);
                    
                    //BL
                    displaySurroundingBlanksActionPerformed(event, r+1, c-1, fbutton);
                    
                    //BR
                    displaySurroundingBlanksActionPerformed(event, r+1, c+1, fbutton); 
                }
                
                //c == 0
                else if(c == 0) {
                    fbutton.setText(" ");
                    //right
                    displaySurroundingBlanksActionPerformed(event, r, c+1, fbutton);

                    //bottom, top
                    displaySurroundingBlanksActionPerformed(event, r-1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r+1, c, fbutton);
                    
                    //TR
                    displaySurroundingBlanksActionPerformed(event, r-1, c+1, fbutton);
                    
                    //BR
                    displaySurroundingBlanksActionPerformed(event, r+1, c+1, fbutton); 
                }
                
                //row == maxI
                else if(r == maxI) {
                    fbutton.setText(" ");
                    //top
                    displaySurroundingBlanksActionPerformed(event, r-1, c, fbutton);

                    //left, right
                    displaySurroundingBlanksActionPerformed(event, r, c-1, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c+1, fbutton);
                    
                    //TL
                    displaySurroundingBlanksActionPerformed(event, r-1, c-1, fbutton);
                    
                    //TR
                    displaySurroundingBlanksActionPerformed(event, r-1, c+1, fbutton); 
                }
                
                //c == maxI
                else if(c == maxI) {
                    fbutton.setText(" ");
                    //left
                    displaySurroundingBlanksActionPerformed(event, r, c-1, fbutton);

                    //bottom, top
                    displaySurroundingBlanksActionPerformed(event, r-1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r+1, c, fbutton);
                    
                    //TL
                    displaySurroundingBlanksActionPerformed(event, r-1, c-1, fbutton);
                    
                    //BL
                    displaySurroundingBlanksActionPerformed(event, r+1, c-1, fbutton); 
                }
                
                //inner buttons, not on the edge, checks 8 other JButtons
                if(r >= 1 && r < maxI && c >= 1 && c < maxI) {
                    fbutton.setText(" ");
                    //right, down
                    displaySurroundingBlanksActionPerformed(event, r+1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c+1, fbutton);

                    //left, up
                    displaySurroundingBlanksActionPerformed(event, r-1, c, fbutton);
                    displaySurroundingBlanksActionPerformed(event, r, c-1, fbutton);

                    //TL
                    displaySurroundingBlanksActionPerformed(event, r-1, c-1, fbutton);
                    
                    //TR
                    displaySurroundingBlanksActionPerformed(event, r-1, c+1, fbutton);
                    
                    //BL
                    displaySurroundingBlanksActionPerformed(event, r+1, c-1, fbutton);
                    
                    //BR
                    displaySurroundingBlanksActionPerformed(event, r+1, c+1, fbutton);   
                }    
            }   //end of if - button has not been clicked & is blank            
                     
            return cntReveal;       //return number of buttons revealed
            
        }   //end of event method
    }
    
    static class Board {
        String[][] vArray;
        int tsize;  // =n*n
        int n;
    
        Board(int n) {
            tsize = n*n;            //total size of the board
            vArray = new String[n][n];   //2D array that represent the data of the grid
        }  
    }
    
    static class Queens extends Board {
        int numOfQueens;
        GUI myGui;
        
        Queens(int n) {
            super(n);      //inherit constructor of board class
            numOfQueens = tsize/4;          //sets the number of queens to 1/4th of the total size given by user
            final int startQueens = tsize/4;
            setBoardValues();
            myGui = new GUI(n, vArray, startQueens);     //call GUI constructor to pass values
        }
        
        void close() {
            myGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        
        void setBoardValues() {     //sets board values
            
            //assign all values in array
            for(int i = 0; i < vArray.length; i++) {
                for (int j = 0; j < vArray[i].length; j++) {
                    vArray[i][j] = "B";
                }
            }
            
            Random r = new Random();    //object of random
            
            //randomly assign queens to array
            while(numOfQueens > 0) {
                int i = (int)(Math.random() * vArray.length);
                int j = (int)(Math.random() * vArray.length);
                
                //System.out.println(i+" "+j);
                
                if(vArray[i][j] != "Q") {       //if value in array has not already been assigned a queen
                    vArray[i][j] = "Q";
                    numOfQueens--;
                } 
            } 
        }
        
        void print() {
            for (int i = 0; i < vArray.length; i++) {
                for (int j = 0; j < vArray[i].length; j++) {
                    System.out.print(vArray[i][j]+ " ");
                }
                
                System.out.println();
            }   
        }      
    }
       
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner in = new Scanner(System.in);
        
        //get size of board from user
        System.out.print("Enter board size: ");
        int n = in.nextInt();
        
        //input validation
        while(n > 20 || n < 2) {
            System.out.println("Invalid size, must be between 2-20");
            System.out.print("Enter board size: ");
            n = in.nextInt(); 
        }
        
        //create queens object
        Queens game = new Queens(n);
        
        //game.print();         // FOR TESTING - prints array values to console
        
        game.close();
        
    }
}   
