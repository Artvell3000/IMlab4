package com.example;

import java.util.HashSet;
import javafx.util.Pair;

public class Model {
    private final int height;
    private final int width;
    private HashSet<Pair<Integer, Integer>> live = new HashSet<>();
    private HashSet<Pair<Integer, Integer>> neig = new HashSet<>();
    private HashSet<Pair<Integer, Integer>> addedLive = new HashSet<>();
    private HashSet<Pair<Integer, Integer>> addedNeig = new HashSet<>();
    Boolean [][] field;

    private Pair<Integer, Integer> copyPair(Pair<Integer, Integer> o){
        return new Pair<Integer, Integer>(o.getKey(), o.getValue());
    }

    private void printInfo(){
        System.out.println("newLive");
        for(Pair<Integer, Integer> i:live){
            System.out.println(i.getKey() + " " + i.getValue());
        }

        System.out.println("newNeig");
        for(Pair<Integer, Integer> i:neig){
            System.out.println(i.getKey() + " " + i.getValue());
        }
        System.out.println("");
    }



    public void addLive(HashSet<Pair<Integer, Integer>> l, HashSet<Pair<Integer, Integer>> n){
        live.addAll(l);
        neig.addAll(n);
    }

    Model(int height, int width, HashSet<Pair<Integer, Integer>> l, HashSet<Pair<Integer, Integer>> n){
        this.height = height;
        this.width = width;
        live.addAll(l);
        neig.addAll(n);

        printInfo();

        field = new Boolean[height][width];

        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                Pair<Integer, Integer> cell = new Pair<Integer, Integer>(i,j);
                if(live.contains(cell)){
                    field[i][j] = true;
                    continue;
                }
                if(neig.contains(cell)){
                    field[i][j] = false;
                    continue;
                }
                field[i][j] = null;
            }

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int getCountOfNeighbour(Pair<Integer, Integer> c, boolean isLiveCell){
        int count = 0;

        Integer startX = c.getKey()-1, endX = c.getKey()+1, startY = c.getValue()-1, endY = c.getValue()+1;

        if(startX < 0) startX++;
        if(endX >= height) endX--;
        if(startY < 0) startY++;
        if(endY >= width) endY--;
        System.out.println("islive" + isLiveCell + " " + c.getKey() + " " + c.getValue() + " : " + field[c.getKey()][c.getValue()]);

        for(int i=startX; i<=endX; i++){
            for(int j=startY; j<=endY; j++){
                if(field[i][j]!=null && field[i][j]) count++;
            }
        }

        if(isLiveCell) count--;
        
        return count;
    }

    public Boolean[][] getNextState(){
        printInfo();
        Boolean[][] newField = new Boolean[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                newField[i][j] = null;
            }
        }

        HashSet<Pair<Integer, Integer>> newLive = new HashSet<>();
        HashSet<Pair<Integer, Integer>> newNeig = new HashSet<>();

        for(Pair<Integer, Integer> el:live){
            int count = getCountOfNeighbour(el, true);
            if((count >= 2 && count <= 3)){
                newLive.add(el);
            }
        }

        for(Pair<Integer, Integer> el:neig){
            int count = getCountOfNeighbour(el, false);
            if(count == 3){
                newLive.add(el);
            }
        }

        for(Pair<Integer, Integer> el:newLive){
            Integer startX = el.getKey()-1, endX = el.getKey()+1, startY = el.getValue()-1, endY = el.getValue()+1;

                if(startX < 0) startX++;
                if(endX >= height) endX--;
                if(startY < 0) startY++;
                if(endY >= width) endY--;

                for(int i=startX; i<=endX; i++){
                    for(int j=startY; j<=endY; j++){
                        Pair<Integer, Integer> p = new Pair<>(i,j);
                        if(!newLive.contains(p))newNeig.add(p);
                    }
                }
        }

        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++){
                Pair<Integer, Integer> cell = new Pair<Integer, Integer>(i,j);
                if(newLive.contains(cell)){
                    newField[i][j] = true;
                    continue;
                }
                if(newNeig.contains(cell)){
                    newField[i][j] = false;
                    continue;
                }
                newField[i][j] = null;
            }

        //newLive.addAll(addedLive);
        //newNeig.addAll(addedNeig);
        //addedLive.clear();
        //addedNeig.clear();

        field = newField;
        live = newLive;
        neig = newNeig;
        return newField;
    }
 }
