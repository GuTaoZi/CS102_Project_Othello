package Elements;
import System.*;
public class PlayerTest {
    public static void main(String[] args) {
        GameSystem gameSystem=new GameSystem();
        gameSystem.addPlayer(new Player("jrx"));
        gameSystem.addPlayer(new Player("jrx"));
        gameSystem.addPlayer(new Player("cym"));
        gameSystem.addPlayer(new Player("jrx"));
        gameSystem.addPlayer(new Player("fst"));
        gameSystem.addPlayer(new Player("jrx"));
        gameSystem.addPlayer(new Player("hhh"));
        for (Player i:gameSystem.playerList){
            System.out.println(i);
            gameSystem.savesPlayer(i);
        }
        FileSystem fileSystem=new FileSystem();
        Player player1=fileSystem.readPlayer("jrx");
        Player player2=fileSystem.readPlayer("cym");
        Player player3=fileSystem.readPlayer("fst");
        Player player4=fileSystem.readPlayer("sy");
        Player player5=fileSystem.readPlayer("hhh");
        System.out.println(player1);
        System.out.println(player2);
        System.out.println(player3);
        System.out.println(player4);
        System.out.println(player5);
    }
}