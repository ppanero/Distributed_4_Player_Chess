/*
 *  Created by raj.srivastava on 26/03/15
 */

package game;

import baseModel.enums.Color;

import java.util.Stack;

public class Game
{
    private Board         board1;
    private Board         board2;
    private Stack<String> moves;

    public Game()
    {
    }

    public Game(String team1Name, String team2Name, String t1WName, String t1BName, String t2WName, String t2BName)
    {
        Team team1 = new Team(team1Name);
        Team team2 = new Team(team2Name);

        Player t1wp = new Player(Color.WHITE, t1WName, team1);
        Player t1bp = new Player(Color.BLACK, t1BName, team1);
        Player t2wp = new Player(Color.WHITE, t2WName, team2);
        Player t2bp = new Player(Color.BLACK, t2BName, team2);

        board1 = new Board(t1wp, t2bp);
        board2 = new Board(t2wp, t1bp);

        moves = new Stack<String>();
    }

    public void addMove(Color color, String move)
    {
        if(color.equals(Color.WHITE))
            moves.push(moves.size() + 1 + ". " + move);
        else
            moves.push(moves.pop() + " " + move);
    }
}
