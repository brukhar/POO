package ca.uqac.gomoku.aspects;

import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JOptionPane;
import ca.uqac.gomoku.core.Player;
import ca.uqac.gomoku.core.model.Spot;
import ca.uqac.gomoku.ui.Board;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ca.uqac.gomoku.core.model.Grid;

public aspect FinJeu {
	
	boolean ended = false;
	GraphicsContext gc;
	double spotSize = 0;
	
	//Je récupère le contexte graphique pour pouvoir mettre en évidence les pierres gagnantes.
	pointcut getBoard(Board b) : target(b);
	after(Board b) : getBoard(b) {
		new Thread(()-> {  //J'attend avant de prendre le contexte, pour que l'application ait le temps de se lancer.
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(gc == null)
				gc = b.getGraphicsContext2D();
		}).start();
	}
	
	//Je récupère la taille des pierres pour les mettre en évidence.
	pointcut getSpotSize(double size) : set(double Board.spotSize)  && args(size);
	after(double size) : getSpotSize(size){
		spotSize = size;
	}
	
	//Quand la partie est terminée, j'informe les joueurs de qui a gagné.
	pointcut gameFinished(Player winner) : execution(void gameOver(Player)) && args(winner);
	after(Player winner) : gameFinished(winner) {
		ended = true;
		new Thread(()-> { //J'attend ici pour que la pierre gagnante s'affiche tout de même.
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "bingoooo! " + winner.getName() + " a gagné.", "Fin de la partie", JOptionPane.INFORMATION_MESSAGE);
			}).start();
		
	}
	
	//Si la partie est terminée, on ne place plus de pierres.
	pointcut playing() : call(void placeStone(int, int, Player));
	void around() : playing() {
		if(!ended)
		{
			proceed();
		}
	}
	
	//Si la liste des pierres gagnantes n'est plus vide, alors je les met en évidence.
	pointcut winningStonesChange(List<Spot> winningStones) : set(List<Spot> Grid.winningStones)  && args(winningStones);
	after(List<Spot> winningStones) : winningStonesChange(winningStones) {
		if(!winningStones.isEmpty() && gc != null)
		{
			winningStones.forEach(s -> {
				gc.setFill(new Color(0, 0, 0, 0.3));
				double x = spotSize + s.x * spotSize; // center x
				double y = spotSize + s.y * spotSize; // center y
				double r = spotSize/1.5; // radius
				gc.fillOval(x - r, y - r, r * 2, r * 2);
			});
		}
	}
}
