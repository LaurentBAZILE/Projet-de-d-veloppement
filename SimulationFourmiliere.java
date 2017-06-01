


import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;


public class SimulationFourmiliere {
    static JFrame frame;
    static DessinFourmi fourmiPanel;
   
   public static void main(String[] args){          
	   initialiser();
    }

    static void initialiser()
    {
      frame = new JFrame("Simulation d'une fourmilière");
      fourmiPanel = new DessinFourmi();//Jpanel
      
      frame.setSize(500, 500);
      frame.add(fourmiPanel);
      
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }   
}
