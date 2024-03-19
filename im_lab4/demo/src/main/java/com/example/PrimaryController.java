package com.example;

import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

public class PrimaryController {
    GridPane grid = new GridPane();
    final int height = 50, width = 50;
    Model model = null;
    Timeline timeline = null;
    private final double delay = 150;

    Boolean isStartDraw = false;
    Boolean isClearing = false;
    boolean isStart = true;

    HashSet<Pair<Integer, Integer>> newLive = new HashSet<>();
    HashSet<Pair<Integer, Integer>> newNeig = new HashSet<>();
    Pane[][] field = new Pane[height][width];

    @FXML
    Pane mainPane;
    @FXML
    Button btnClear;
    @FXML
    Button btnDraw;
    @FXML
    Button btnStartStop;

    @FXML
    private void clickStartStop(ActionEvent evenr){
        if(model == null){
            model = new Model(height, width, newLive, newNeig);
            newLive.clear();
            newNeig.clear();
        }

        if(timeline == null){
            timeline = new Timeline();
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(delay), e -> {
                    System.out.println("newLive");
                    for(Pair<Integer, Integer> i:newLive){
                        System.out.println(i.getKey() + " " + i.getValue());
                    }

                    System.out.println("newNeig");
                    for(Pair<Integer, Integer> i:newNeig){
                        System.out.println(i.getKey() + " " + i.getValue());
                    }
                    System.out.println("");

                    var state = model.getNextState();
                    for(int i=0;i<height;i++)
                        for(int j=0;j<width;j++){
                            if(state[i][j]==null){
                                field[i][j].setStyle(res.styleDeadCell);
                            } else{
                                if(state[i][j]){
                                    field[i][j].setStyle(res.styleLiveCell);
                                }
                                if(!state[i][j]){
                                    field[i][j].setStyle(res.styleNeighbourCell);
                                }
                            }
                        }
                    //model.addLive(newLive, newNeig);
                    //newLive.clear();
                    //newNeig.clear();
                })
            );
        }
        timeline.setCycleCount(Timeline.INDEFINITE);
        changeStateOfButtons();
    }

    @FXML
    private void changeStateOfButtons(){
        if(isStart){
            timeline.play();
            isStartDraw = false;
            btnClear.setVisible(false);
            btnDraw.setVisible(false);
            btnStartStop.setText("Stop");
        } else {
            timeline.stop();
            //btnClear.setVisible(true);
            //btnDraw.setVisible(true);
            btnStartStop.setText("Start");
        }
        isStart = !isStart;
    }

    @FXML
    private void clickClear(ActionEvent evenr){
        isClearing = true;
        btnClear.setStyle("-fx-background-color: red;");
        btnDraw.setStyle("-fx-background-color: grey;");
    }

    @FXML
    private void clickDraw(ActionEvent evenr){
        isClearing = false;
        btnClear.setStyle("-fx-background-color: grey;");
        btnDraw.setStyle("-fx-background-color: red;");
    }

     @FXML
    public void initialize() {

        mainPane.setOnMouseClicked(e -> {
            if(isStart)isStartDraw = !isStartDraw;
        });

        for(int i=0;i<50; i++){
            for(int j=0;j<50;j++){
                Pair<Integer, Integer> key = new Pair<Integer,Integer>(i,j);
                field[i][j] = new Pane();
                grid.add(field[i][j],i,j);
                field[i][j].setPrefWidth(10);
                field[i][j].setPrefHeight(10);
                field[i][j].setStyle(res.styleDeadCell);
                field[i][j].setOnMouseClicked(e -> {
                    if(isStart){
                        if(!newLive.contains(key)){
                            if(newNeig.contains(key)) newNeig.remove(key);
                            newLive.add(key);

                            field[key.getKey()][key.getValue()].setStyle(res.styleLiveCell);
                            
                            int startR = key.getKey() - 1, 
                            startC = key.getValue()-1, 
                            endR = key.getKey() + 1, 
                            endC = key.getValue()+1; 

                            if(startR < 0) startR++;
                            if(endR >= height) endR--;
                            if(startC < 0) startC++;
                            if(endC >= width) endC--;

                            for(int r = startR; r<=endR; r++)
                                for(int c = startC; c<=endC; c++){
                                    var neighbour = new Pair<Integer, Integer>(r,c);
                                    if(!newLive.contains(neighbour) && !newNeig.contains(neighbour)){
                                        newNeig.add(neighbour);
                                        field[neighbour.getKey()][neighbour.getValue()].setStyle(res.styleNeighbourCell);
                                    }
                                }
                        }
                    }
                });
                field[i][j].setOnMouseEntered(e -> {
                    if(isStartDraw){
                        if(!newLive.contains(key)){
                            if(newNeig.contains(key)) newNeig.remove(key);
                            newLive.add(key);
                            field[key.getKey()][key.getValue()].setStyle(res.styleLiveCell);
                            
                            int startR = key.getKey() - 1, 
                            startC = key.getValue()-1, 
                            endR = key.getKey() + 1, 
                            endC = key.getValue()+1; 

                            if(startR < 0) startR++;
                            if(endR >= height) endR--;
                            if(startC < 0) startC++;
                            if(endC >= width) endC--;

                            for(int r = startR; r<=endR; r++)
                                for(int c = startC; c<=endC; c++){
                                    var neighbour = new Pair<Integer, Integer>(r,c);
                                    if(!newLive.contains(neighbour) && !newNeig.contains(neighbour)){
                                        newNeig.add(neighbour);
                                        field[neighbour.getKey()][neighbour.getValue()].setStyle(res.styleNeighbourCell);
                                    }
                                }
                        }
                    }
                });
            }
        }
        grid.setGridLinesVisible(true);
        mainPane.getChildren().add(grid);
        
    }
    
}
