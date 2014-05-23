package com.mapcoders;

/**
 * Created with IntelliJ IDEA.
 * User: don
 * Date: 23/05/2014
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */

class Point {
    public Point(int initx,int inity) {x=initx;y=inity;}
    public Point() { x=UNDEFINED; }
    public void clear() { x=UNDEFINED; }
    public boolean isDefined() { return (x!=UNDEFINED); }
    public int x;
    public int y;
    private final static int UNDEFINED=361361361;
}


