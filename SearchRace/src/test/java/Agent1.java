import java.util.ArrayList;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int checkpoints = Integer.parseInt(scanner.nextLine());
        ArrayList<Point> targets = new ArrayList<>();
        for(int i = 0; i < checkpoints; i++){
            String[] line = scanner.nextLine().split(" ");
            System.err.println(line[0] + " " + line[1]);
            targets.add(new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
        }
        double dist = 100000.0;
        int z = 0;
        while (true) {
            String s = scanner.nextLine();
            System.err.println(s);
            String[] input = s.split(" ");
            // id x y vx vy angle
            int target = Integer.parseInt(input[0]);
            int x = Integer.parseInt(input[1]);
            int y = Integer.parseInt(input[2]);
            int vx = Integer.parseInt(input[3]);
            int vy = Integer.parseInt(input[4]);
            Point targ = targets.get(target);

            Point current = new Point(x, y);
            Point in3 = new Point((int)(x+vx*3), (int)(y+vy*3));
            if(dist-in3.distance(targ) < 0 && in3.distance(current) > 150 ||z++ % 5 == 0){
               System.out.println(targ.x + " " + targ.y + " 0");
               // System.out.println("EXPERT 10 150 yolo");
            }else
                System.out.println(targ.x + " " + targ.y + " 150 my message goes here? Or there?");
        }
    }

    private static class Point{
        public int x, y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        double distance(Point p) {
            return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
        }

    }
}
