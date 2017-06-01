


enum FourmiType{Eclaireuse,travailleuse,combattante};

public class Fourmi extends DessinFourmi {
    int X; //Position actuelle de la fourmi en X
    int Y; //Position actuelle de la fourmi en Y

    int curIndex;    //current index of the path when returning to home after finding food     
    int curRoadInd; //Indice actuel de la fourmi de la route suivant
    FourmiType type;  
    boolean rechercheNourri; //true if ant reached home(anthill)
    boolean cheminSuivi;
    Route route;
    
public Fourmi(int x,int y)
{
    X=x;
    Y=y;

    curIndex=0;
    rechercheNourri=false;
    cheminSuivi=false;
    type=FourmiType.Eclaireuse;
    route=new Route();
}

public Fourmi(int x,int y,FourmiType fourmitype)
{
    X=x;
    Y=y;
    
    curIndex=0;
    rechercheNourri=false;
    cheminSuivi=false;
    type=fourmitype;
    route= new Route();
}

//setters and getters
public void setX(int x)
{
    X=x;
}
public int getCurX()
{
    return X;
}

public void setY(int y)
{
    Y=y;
}
public int getY()
{
    return Y;
}

public void setType(FourmiType fourmitype)
{
    type=fourmitype;
}
public FourmiType getType()
{
    return type;
}

//set to true if ant follow another ant's path(if it was the best path)
public void setCheminSuivi(boolean estSuivi)
{
	cheminSuivi=estSuivi;
}

public boolean getCheminSuivi()
{
    return cheminSuivi;
}
void Move(int ind) 
{       
        int dir;      
    
        //Si dépasse les limites de la fenêtre
        if(arrFourmis[ind].Y<=0||arrFourmis[ind].Y>=frameHeight||arrFourmis[ind].X>=frameWidth||arrFourmis[ind].X<=0)
        {         
           //Recommencer à la maison et réinitialiser sa route
            int x=1+random.nextInt(20);
            int y=1+random.nextInt(20);
            arrFourmis[ind].X=xFourmi+x;
            arrFourmis[ind].Y=yFourmi+y;
            
            arrFourmis[ind].route=new Route();
         
        }
        else if(arrFourmis[ind].X<=0)
        {         
            arrFourmis[ind].X=arrFourmis[ind].X+pas;               
           System.out.println("fourmi ind="+ind+"    cur x<=0, = "+arrFourmis[ind].X+"......");
           return;
        }

        //Si atteint la maison 
         if((arrFourmis[ind].X<=nourriture.x+nourriture.width)&&(arrFourmis[ind].X>=nourriture.x)&&(arrFourmis[ind].Y>=nourriture.y)&&(arrFourmis[ind].Y<=(nourriture.y+nourriture.height)))//Vérifie si la fourmi a atteint la nourriture
        {
            arrFourmis[ind].rechercheNourri=true;

            arrFourmis[ind].route.augmenterIntensitePheromones(); //Retour en suivant la même route

            //Indice courant de la fourmi = index du dernier point sur son chemin
            arrFourmis[ind].curIndex=arrFourmis[ind].route.getPath().size()-1;
            MoveToHome(arrFourmis[ind],ind);
            return;
        }
         //Vérifier si les bonnes routes ont été trouvées (sentir des phéromones)
         if(arrFourmis[ind].cheminSuivi)
         {
             
             return;
         }
                   
         else
         {
             moveRandomly(ind);
         }
         
}

private void moveRandomly(int ind)
{
    int dir;
    //Nombre aléatoire spécifiant le pas des fourmis dans chaque direction (1: ouest, 2: sud-ouest, 3: nord-ouest, 4: nord, 5: sud, 6: sud-est, 7: nord-est, 8: est )
         dir=random.nextInt(3)+1;
         System.out.println("dir= "+dir);

        //Modifier les valeurs des points actuels selon la direction
        if(dir==1)
        {         
           arrFourmis[ind].X=arrFourmis[ind].X-pas;  
           
           //Ajouter un nouveau point aux pas de la fourmis
           arrFourmis[ind].route.ajoutPoint(arrFourmis[ind].X,arrFourmis[ind].getY());
        }
        else if(dir==2)
        {
            arrFourmis[ind].X=arrFourmis[ind].X-pas;
            arrFourmis[ind].Y=arrFourmis[ind].Y-pas;  
            
             //Ajouter un nouveau point aux pas de la fourmis
           arrFourmis[ind].route.ajoutPoint(arrFourmis[ind].X,arrFourmis[ind].getY());
        }
        else if(dir==3)
        {
            arrFourmis[ind].X=arrFourmis[ind].X-pas;
            arrFourmis[ind].Y=arrFourmis[ind].Y+pas; 
            
             //Ajouter un nouveau point aux pas de la fourmis
            arrFourmis[ind].route.ajoutPoint(arrFourmis[ind].X,arrFourmis[ind].getY());
         }
	}
}
