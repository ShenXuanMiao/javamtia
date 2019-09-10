package io.github.viscent.mtia.ch3;

public class FinalAndObjectInitExample {
    private final Point position;

    public FinalAndObjectInitExample(int x, int y) {
        position = new Point(x, y);
    }

    public Point getPosition() {
        return new Point(position.x, position.y);
    }

    static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
