package com.codingame.game;

public class Car extends Unit {
    public double angle, prevAngle;
    public String message;
    public boolean debug;
    public int thrust = 0;

    public Point target;

    Car(double x, double y, double angle) {
        super(x, y);
        this.angle = angle;
        this.friction = Constants.CAR_FRICTION;
    }


    public void handleInput(String input, IPlayerManager manager) throws Exception{
        prevAngle = angle;
        target = null;
        message = "";
        Car car = this;
        String[] splitted = input.split(" ");

        if(splitted[0].equals("EXPERT")){
            int angle;
            try {
                angle = Integer.parseInt(splitted[1]);
                thrust = Integer.parseInt(splitted[2]);
            }catch (Exception e){
                throw new Exception("Can't parse expert input integers. " + e.getMessage());
            }
            if(thrust < 0 || thrust > Constants.CAR_MAX_THRUST) {
                manager.addGameSummary("Invalid thrust. Please keep between 0 and " + Constants.CAR_MAX_THRUST);
                throw new Exception( "Invalid thrust");
            }

            if(angle < -18 || angle > 18){
                manager.addGameSummary("Invalid angle. Please keep between -18 and 18.");
                throw new Exception("Invalid angle");
            }

            car.handleExpertInput(angle, thrust);
            if(splitted.length > 3){
                int totalLength = ("EXPERT " +angle+" "+thrust+" ").length();
                car.message = input.substring(totalLength);
                findDebugLines(car.message);
            }else{
                car.message="";
            }

        }
        else{
            int x;
            int y;

            try{
                x = Integer.parseInt(splitted[0]);
                y = Integer.parseInt(splitted[1]);
                target = new Point(x, y);
                thrust = Integer.parseInt(splitted[2]);
            }catch (Exception e){
                throw new Exception("Can't parse action. Has to be X Y THRURST. " + e.getMessage());
            }

            if(thrust < 0 || thrust > Constants.CAR_MAX_THRUST) {
                manager.addGameSummary("Invalid thrust. Please keep between 0 and " + Constants.CAR_MAX_THRUST);
                throw new Exception( "Invalid thrust");
            }

            car.handleInput(x, y, thrust);
            if(splitted.length > 3){
                int totalLength = (x+" "+y+" "+thrust+" ").length();
                car.message = input.substring(totalLength);
                findDebugLines(car.message);
            }else{
                car.message="";
            }
        }

        if(car.message.contains("debug")){
            debug = true;
        }
    }

    private void findDebugLines(String message){
        //Format: DEBUG_LINES: { x0 y0 x1 y1, x0 y0 x1 y1 }
        if(!message.trim().startsWith("DEBUG_LINES:")){
            return;
        }
        // TODO:
    }

    public void handleExpertInput(int angle, int thrust){
        double newAngle = Math.toDegrees(this.angle) + angle;
        this.angle = Math.toRadians(newAngle);
        thrustTowardsHeading(thrust);
    }

    public void handleInput(int x, int y, int thrust){
        if (this.x != x || this.y != y) {
            double angle = this.getAngle(new Point(x, y));
            double relativeAngle = shortAngleDist(this.angle, angle);
            if (Math.abs(relativeAngle) >= Constants.MAX_ROTATION_PER_TURN) {
                angle = this.angle + Constants.MAX_ROTATION_PER_TURN * Math.signum(relativeAngle);
            }

            this.angle = angle;
            thrustTowardsHeading(thrust);
        }
    }


    private void thrustTowardsHeading(int thrust){
        double vx = Math.cos(angle) * thrust;
        double vy = Math.sin(angle) * thrust;

        this.vx += vx;
        this.vy += vy;
    }

    @Override
    public void adjust() {
        super.adjust();
        double degrees = Math.round(Math.toDegrees(angle));
        this.angle = Math.toRadians(degrees);
        while(this.angle > Math.PI*2) this.angle-= Math.PI*2;
        while(this.angle < 0)this.angle+= Math.PI*2;
    }

    private static double shortAngleDist(double a0, double a1) {
        double max = Math.PI * 2;
        double da = (a1 - a0) % max;
        return 2 * da % max - da;
    }
}
