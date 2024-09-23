package game2048;

import java.awt.*;

// Creating a class Tile
public class Tile {
    int value;

    // Default Constructor
    public Tile() {
        this(0);
    }

    // Parametrized Constructor
    public Tile(int num) {
        value = num;
    }

    // To check for Empty
    public boolean isEmpty() {
        return value == 0;
    }

    // To color the foreground
    public Color getForeground() {
        return value < 16 ? new Color(0xffffff) :  new Color(0xf9f6f2);
    }

    // Coloring the background of tiles according to the value they have
    public Color getBackground() {

        // Settings the colour for different values using switch
        switch (value)
        {
            case 2:    return new Color(0x3bcbff);
            case 4:    return new Color(0x003d99);
            case 8:    return new Color(0x0bb5a1);
            case 16:   return new Color(0x391be3);
            case 32:   return new Color(0x6f00ff);
            case 64:   return new Color(0x0acf7d);
            case 128:  return new Color(0x307569);
            case 256:  return new Color(0x668073);
            case 512:  return new Color(0x0b423a);
            case 1024: return new Color(0xa14df0);
            case 2048: return new Color(0x296cf2);
        }

        return new Color(0x62a5bd);
    }
}
