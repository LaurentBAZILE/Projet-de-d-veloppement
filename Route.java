//Classe représentant le chemin suivi par les fourmis

import java.awt.Point;
import java.util.ArrayList;

public class Route {
    ArrayList path;
    int IntensitePheromones;
    
    public Route()
    {
    	IntensitePheromones=0;
        path=new ArrayList();
    }
    public void augmenterIntensitePheromones()
    {
    	IntensitePheromones++;
    }
    public int getIntensitePheromones()
    {
        return IntensitePheromones;
    }
    
    //Ajout d'un point à la route
    public void ajoutPoint(int x,int y)
    {
        Point p=new Point(x, y);
        path.add(p);
    }
    
    //Retour si la route contient un point
    public boolean premierPas()
    {
        if(path.size()<=1)
            return true;
        return false;
    }
    
    //Obtenir le chemin
    public ArrayList getPath()
    {
        return path;
    }
}
