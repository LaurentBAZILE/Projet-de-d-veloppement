


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;


public class DessinFourmi extends JPanel
{    
	
	 //Fourmilière
     public static final int frameWidth=500;
     public static final int frameHeight=500;
     public static final int homeWidth=60;
     public static final int homeHeight=60;
     public static final int xHome=frameWidth-100;
     public static final int yHome=(frameHeight/2)-homeHeight;
     
     //Nourriture
     public static final int nourritureWidth=60;
     public static final int nourritureheight=70;
     public static final int xNourriture=100;
     public static final int yNourriture=frameHeight-270;
     
     //Fourmi
     public static final int fourmiWidth=10;
     public static final int fourmiheight=7;
     public static final int xFourmi=xHome+20; //point départ fourmi
     public static final int yFourmi=yHome+20; //point arrivee fourmi
     final int arrLength=10; //nbre fourmi 
     Fourmi[] arrFourmis;
     Fourmi fourmi; Nourriture nourriture;
     public Graphics graphics; //Dessin de texte ou forme
     Random random; //nombe aléatoire
 
     final int pas=5; //vitesse fourmi
     boolean premierPas=true;

    public void paintComponent(Graphics g){   
      super.paintComponent(g);
      
     if(premierPas)
     {
       fourmi=createFourmi(g);
       premierPas=false;
     }
      
     for(int i=0;i<arrFourmis.length;i++)
      {
          if(!(arrFourmis[i].route.premierPas()))
          {
              drawPheromones(g);
          }
      }
      //Dessin rectangle maison
      g.setColor(Color.RED);
      g.drawRect(xHome, yHome, homeWidth, homeHeight);
      
      //Dessin rectangle nourriture
      g.setColor(Color.green);
      g.drawRect(xNourriture, yNourriture, nourritureWidth, nourritureheight);
      
      //Dessin des fourmis
       for(int i=0;i<arrLength;i++)
       {                          
            drawFourmi(g,arrFourmis[i].X, arrFourmis[i].Y, Color.BLACK);            
       }
      //Si ce n'est pas le 1er pas, dessiner phéromones 
      
   } 
     //Réglage de la minuterie pour mettre à jour le dessin dans le constructeur

    public DessinFourmi()
      {
          //Créer nourriture
         nourriture=new Nourriture(xNourriture, yNourriture, nourritureWidth, nourritureheight); 
         random=new Random();
        
          Timer timer = new Timer(200, new ActionListener() {
                @Override
                //Time Tick handler
                public void actionPerformed(ActionEvent e) { 
                    for(int i=0;i<arrFourmis.length;i++)
                    {
                        if(arrFourmis[i].curIndex>0)
                        {
                            MoveToHome(arrFourmis[i],i);
                        }
                        else{
                        Move(i);
                        }
                        repaint();
                    }
                }
            });
            timer.start();
      }
    
    void drawFourmi(Graphics g,int x,int y,Color color)
    {     
           g.setColor(color);
           g.fillOval(x,y,fourmiWidth,fourmiheight);   
    }
    
    void MoveToHome(Fourmi fourmi,int ind)
    {

        if(fourmi.rechercheNourri)
        {           
            Point curPoint=(Point)fourmi.route.getPath().get(fourmi.curIndex);
            fourmi.X=curPoint.x;
            fourmi.Y=curPoint.y;
            
            if(fourmi.curIndex>0)
            {
                fourmi.curIndex=fourmi.curIndex-1;
            }else
            {

                fourmi.rechercheNourri=false;

            }
            arrFourmis[ind]=fourmi;
       
        }        
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
                 followRoad(ind);
                 return;
             }
             else if(findBestRoute().IntensitePheromones>1)//right road found 
             {
                 Route route=findBestRoute();
                 arrFourmis[ind].route=route;
                 arrFourmis[ind].cheminSuivi=true;
                 arrFourmis[ind].curRoadInd=0;
                 followRoad(ind);
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
    
    private void followRoad(int fourmiInd)
    {
        if(arrFourmis[fourmiInd].curRoadInd>arrFourmis[fourmiInd].route.path.size()-1)//Chemin achevé
        {
        	arrFourmis[fourmiInd].cheminSuivi=false;
        }
        Point p=(Point)arrFourmis[fourmiInd].route.path.get(arrFourmis[fourmiInd].curRoadInd);
        arrFourmis[fourmiInd].X=p.x;
        arrFourmis[fourmiInd].Y=p.y;
        
        if(arrFourmis[fourmiInd].curRoadInd<arrFourmis[fourmiInd].route.path.size()-1)//Si moins que le dernier index du chemin
        {
        	arrFourmis[fourmiInd].curRoadInd+=1;
            System.out.println("curRiadInd="+arrFourmis[fourmiInd].curRoadInd+"x="+p.x);
        }
        //Faire des déplacements à la maison
    }
    
    //Trouver une route (de la maison) avec une intensité maximale de phéromone
    private Route findBestRoute()
    {
        Route maxPheromonRoute=new Route();
        for(int i=0;i<arrFourmis.length;i++)
        {
            if(arrFourmis[i].route.IntensitePheromones>maxPheromonRoute.IntensitePheromones)
            {
                maxPheromonRoute=arrFourmis[i].route;
            }
        }         
        return maxPheromonRoute;
    }

    private Fourmi createFourmi(Graphics g) {
         //Créer un ensemble de fourmis
         arrFourmis=new Fourmi[arrLength];
         for(int i=0;i<arrLength;i++)
         {            
              int x=1+random.nextInt(20);
              int y=1+random.nextInt(20);
             arrFourmis[i]=new Fourmi(xFourmi+x,yFourmi+y);
         }
        
      Fourmi fourmi=new Fourmi(xFourmi,yFourmi);
      //Dessiner une fourmi à la maison
      fourmi.route.augmenterIntensitePheromones();
      return fourmi;
    }

    private void drawPheromones(Graphics g) {
       Color colorPheromon=new Color(171,234,201,30);
       g.setColor(colorPheromon);
      
       for(int ind=0;ind<arrFourmis.length;ind++)
       {
      //Itérer à la taille 2 (tous les points, à l'exception de celui actuel)
         for(int i=0;i<arrFourmis[ind].route.getPath().size()-2;i++)
         {
           Point p=(Point)(arrFourmis[ind].route.path.get(i));
           g.fillOval(p.x,p.y,10,10);
         }
       }
    }
    
    }
