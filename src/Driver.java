import java.util.ArrayList;

public class Driver {
	public static void main(String[] args) {

//		Hanabi game = new Hanabi(true);
//		game.play();
		
//		 This is the code I will actually use to test your code's behavior.

		int gamesToPlay = 1000;


		int[][] games = new int[gamesToPlay][2];


		int total = 0;
		for (int i = 0; i < gamesToPlay; i++) {

			System.out.println("\n\n\n\nGAME " + i + "--------------------------\n");

			Hanabi next = new Hanabi(false);
			int score = next.play();
//			System.out.println("Game " + i + " score: " + score);  // not helpful rn
			total += score;

			games[i][0] = i;
			games[i][1] = score;
		}
		System.out.println("Final average: " + (total/((float) gamesToPlay)));

		int[] dist = new int[30];
		for (int i = 0; i < games.length; i++) {
//			System.out.println("game " + games[i][0] + ": " + games[i][1]);
			dist[games[i][1]] = dist[games[i][1]] + 1;
		}

		for (int i = 0; i < dist.length; i++) {
			System.out.print(i + ": ");
			for (int j = 0; j < dist[i]; j++) {
				System.out.print(".");
			}
			System.out.println("\n");
		}

	}

}
